package tech.maiquer.common.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.ResultCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SecureAuthEntryPointHandler implements AuthenticationEntryPoint {

    /**
     * 用户未登录
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        // 设定响应状态码为404
        httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        // 设定响应内容是utf-8编码的json类型
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        // 序列化结果对象为JSON
        String resultJSON = JSON.toJSONString(Result.failure(ResultCode.USER_NOT_LOGGED_IN));
        // 写入响应体
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(resultJSON);
        writer.flush();
        writer.close();


    }
}
