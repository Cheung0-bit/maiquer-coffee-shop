package tech.maiquer.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.maiquer.common.config.property.WxCodeProperty;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class WechatLoginController {

    @Resource
    private WxCodeProperty wxCodeProperty;

    /**
     * 获取code
     *
     * @param userId
     * @return
     */
    @GetMapping({"/api/wechat/login/{userId}", "/api/wechat/login"})
    @ApiOperation(value = "微信登录", notes = "用户在浏览器访问/api/wechat/login接口,返回url，然后在我们在前端请求这个url，调用后端的回调接口得到json")
    public String getWxCode(@PathVariable(value = "userId", required = false) Long userId) {

        String baseUrl = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=%s" +
                "#wechat_redirect";

        // 对redirect_url进行URLEncoder编码
        String redirectUrl = wxCodeProperty.getRedirectUrl();

        try {
            URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format(baseUrl,
                wxCodeProperty.getAppId(),
                userId == null ? redirectUrl : redirectUrl + "?userId=" + userId,
                "maiqu");
        System.out.println(url);
        return "redirect:" + url;
    }


}
