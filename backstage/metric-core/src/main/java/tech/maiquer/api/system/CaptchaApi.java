package tech.maiquer.api.system;

import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.maiquer.common.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@Api(tags = {"验证生成"})
@RequestMapping("/system/captcha")
public class CaptchaApi {

    /**
     * 验证码生成
     *
     * @param request  请求报文
     * @param response 响应报文
     */
    @RequestMapping("generate")
    public void generate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.out(request, response);
    }

    /**
     * 异步验证
     *
     * @param request 请求报文
     * @param captcha 验证码
     * @return 验证结果
     */
    @RequestMapping("verify")
    public Result verify(HttpServletRequest request, String captcha) {
        if (CaptchaUtil.ver(captcha, request)) {
            return Result.success("验证成功");
        }
        return Result.failure("验证失败");
    }

}



