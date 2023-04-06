package tech.maiquer.common.config.property;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

@Data
@Slf4j
@Component
@ConfigurationProperties("metric.wechat-pay")
public class WxPayProperty {

    private String mchId;

    private String mchSerialNo;

    private String privateKeyPath;

    private String apiV3Key;

    private String appId;

    private String domain;

    private String notifyDomain;

    /**
     * 加载商户私钥 <br>
     *
     * @return PrivateKey
     */
    @Bean
    public PrivateKey getPrivateKey() throws IOException {

        // 加载商户私钥（privateKey：私钥字符串）
        log.info("开始加载私钥，读取内容...");
        String content = new String(Files.readAllBytes(Paths.get(privateKeyPath)), StandardCharsets.UTF_8);
        return PemUtil.loadPrivateKey(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));

    }

    /**
     * 获取平台证书管理器，定时更新证书（默认值为UPDATE_INTERVAL_MINUTE）
     * <br>
     * 返回验签器实例，注册为bean，在实际业务中使用
     *
     * @return
     */
    @Bean
    public Verifier getVerifier(PrivateKey merchantPrivateKey) throws IOException, NotFoundException {

        log.info("加载证书管理器实例");

        // 获取证书管理器单例实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();

        // 向证书管理器增加需要自动更新平台证书的商户信息
        log.info("向证书管理器增加商户信息，并开启自动更新");
        try {
            // 该方法底层已实现同步线程更新证书
            // 详见beginScheduleUpdate()方法
            certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId,
                    new PrivateKeySigner(mchSerialNo, merchantPrivateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException | HttpCodeException e) {
            e.printStackTrace();
        }

        log.info("从证书管理器中获取验签器");
        return certificatesManager.getVerifier(mchId);
    }

    /**
     * 通过WechatPayHttpClientBuilder构造HttpClient
     *
     * @param verifier
     * @return
     */
    @Bean(name = "wxPayClient")
    public CloseableHttpClient getWxPayClient(Verifier verifier, PrivateKey merchantPrivateKey) throws IOException {

        log.info("构造httpClient");

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier));

        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        CloseableHttpClient httpClient = builder.build();

        log.info("构造httpClient成功");
        return httpClient;
    }

    /**
     * 构建微信支付回调请求处理器
     *
     * @param verifier
     * @return NotificationHandler
     */
    @Bean
    public NotificationHandler notificationHandler(Verifier verifier) {
        return new NotificationHandler(verifier, apiV3Key.getBytes(StandardCharsets.UTF_8));
    }

}
