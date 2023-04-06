package tech.maiquer.common.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tech.maiquer.common.model.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SecureAuthFailureHandler implements AuthenticationFailureHandler {

    /**
     * 配置登录失败
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 设定响应状态码为200
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        // 设定响应内容是utf-8编码的json类型
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        // 序列化结果对象为JSON
        String resultJSON = null;
        if (e instanceof UsernameNotFoundException) {
            resultJSON = JSON.toJSONString(Result.failure("用户名不存在"));
        }
        if (e instanceof LockedException) {
            resultJSON = JSON.toJSONString(Result.failure("用户被冻结"));
        }
        if (e instanceof BadCredentialsException) {
            resultJSON = JSON.toJSONString(Result.failure("账户不存在或密码错误"));
        }
        if (e instanceof DisabledException) {
            resultJSON = JSON.toJSONString(Result.failure("用户未启用"));
        }
        // 写入响应体
        PrintWriter writer = httpServletResponse.getWriter();
        assert resultJSON != null;
        writer.write(resultJSON);
        writer.flush();
        writer.close();
    }

}
