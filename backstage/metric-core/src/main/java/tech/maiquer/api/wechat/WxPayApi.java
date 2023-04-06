package tech.maiquer.api.wechat;

import com.alibaba.fastjson.JSON;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.ResultCode;
import tech.maiquer.service.WxPayService;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.service.SysUserService;
import tech.maiquer.utils.HttpUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lin
 */
@RestController
@RequestMapping("/api/wx-pay")
@CrossOrigin
@Api(tags = "网站微信支付APIv3")
@Slf4j
public class WxPayApi {

    @Resource
    private WxPayService wxPayService;

    @Resource
    private NotificationHandler notificationHandler;

    @Resource
    private SysUserService sysUserService;

    @ApiOperation("前端支付后回调地址")
    @PreAuthorize("hasPermission('/api/wx-pay/fontNotify','wechat:payment:fontNotify')")
    @PostMapping("/fontNotify")
    public Result fontNotify(@ApiParam("orderNo") @RequestParam String orderNo, @ApiParam("userId") @RequestParam Long userId, @ApiParam("evaId") @RequestParam Integer evaId) throws Exception {

        Map res = JSON.parseObject(wxPayService.queryOrder(orderNo), HashMap.class);

        if (res.get("trade_state_desc").equals("支付成功")) {
            sysUserService.addMyEva(userId, evaId);
            return Result.success();
        }
        return Result.failure("订单未支付");

    }

    /**
     * Native下单
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("调用NATIVE下单API，生成支付二维码，二维码样式请前端根据返回code_url自行优化")
    @PreAuthorize("hasPermission('/api/wx-pay/native','wechat:payment:native')")
    @PostMapping("/native/{evaId}")
    public Result nativePay(@PathVariable Integer evaId, @RequestParam Long userId) throws Exception {

        log.info("发起NATIVE支付请求 v3");

        //返回支付二维码连接和订单号
        return wxPayService.nativePay(evaId, userId);

    }

    /**
     * Native下单
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("调用JSAPI下单API，生成预支付订单号，前端使用JSAPI支付下单")
    @PreAuthorize("hasPermission('/api/wx-pay/jsapi','wechat:payment:jsapi')")
    @PostMapping("/jsapi/{evaId}")
    public Result jsApiPay(@PathVariable Integer evaId, @RequestParam Long userId) throws Exception {

        log.info("发起JSAPI支付请求 v3");

        //返回支付二维码连接和订单号
        return wxPayService.jsApiPay(evaId, userId);
    }

    /**
     * 支付通知<br>
     * 微信支付通过支付通知接口将用户支付成功消息通知给商户<br>
     * 商户应返回应答<br>
     * 若商户收到的商户的应答不符合规范或者超时 微信则认为通知失败<br>
     * 若通知失败 微信会通过一定的策略定期重新发起通知<br>
     * 加密不能保证通知请求来自微信<br>
     * 微信会对发送给商户的通知进行签名<br>
     * 并将签名值放在通知的HTTP头Wechatpay-Signature<br>
     *
     * @param request
     * @param response
     * @return 响应map
     */
    @ApiOperation("支付通知")
    @PostMapping("/notify")
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) {

        // 应答对象
        Map<String, String> map = new HashMap<>();

        try {

            // 处理参数
            String serialNumber = request.getHeader("Wechatpay-Serial");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String signature = request.getHeader("Wechatpay-Signature");// 请求头Wechatpay-Signature
            // 获取请求体
            String body = HttpUtils.readData(request);

            // 构造微信请求体
            NotificationRequest wxRequest = new NotificationRequest.Builder().withSerialNumber(serialNumber)
                    .withNonce(nonce)
                    .withTimestamp(timestamp)
                    .withSignature(signature)
                    .withBody(body)
                    .build();
            Notification notification = null;
            try {

                /**
                 * 使用微信支付回调请求处理器解析构造的微信请求体
                 * 在这个过程中会进行签名验证，并解密加密过的内容
                 * 签名源码：  com.wechat.pay.contrib.apache.httpclient.cert; 271行开始
                 * 解密源码：  com.wechat.pay.contrib.apache.httpclient.notification 76行
                 *           com.wechat.pay.contrib.apache.httpclient.notification 147行 使用私钥获取AesUtil
                 *           com.wechat.pay.contrib.apache.httpclient.notification 147行 使用Aes对称解密获得原文
                 */
                notification = notificationHandler.parse(wxRequest);
            } catch (Exception e) {
                log.error("通知验签失败");
                //失败应答
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "通知验签失败");
                return JSON.toJSONString(map);
            }

            // 从notification中获取解密报文,并解析为HashMap
            String plainText = notification.getDecryptData();
            log.info("通知验签成功");

            //处理订单
            wxPayService.processOrder(plainText);

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "成功");
            return JSON.toJSONString(map);

        } catch (Exception e) {
            e.printStackTrace();
            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return JSON.toJSONString(map);
        }

    }

    /**
     * 用户取消订单
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    @ApiOperation("用户取消订单")
    @PreAuthorize("hasPermission('/api/wx-pay/cancel','wechat:payment:cancel')")
    @PostMapping("/cancel/{orderNo}")
    public Result cancel(@PathVariable String orderNo) throws Exception {

        log.info("取消订单");

        String res = wxPayService.cancelOrder(orderNo);
        if (res.equals("该订单已成功关闭")) {
            return Result.success("订单已取消");
        } else {
            return Result.failure(res);
        }

    }

    /**
     * 查询订单
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    @ApiOperation("查询订单：测试订单状态用")
    @PreAuthorize("hasPermission('/api/wx-pay/query','wechat:payment:query')")
    @GetMapping("/query/{orderNo}")
    public Result queryOrder(@PathVariable String orderNo) throws Exception {

        log.info("查询订单");

        String result = wxPayService.queryOrder(orderNo);
        return Result.success(JSON.parse(result));

    }

}

