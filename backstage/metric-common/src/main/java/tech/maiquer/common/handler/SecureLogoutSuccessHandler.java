package tech.maiquer.common.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import tech.maiquer.common.model.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SecureLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 设定响应状态码为200
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        // 设定响应内容是utf-8编码的json类型
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        // 序列化结果对象为JSON
        String resultJSON = JSON.toJSONString(Result.success());
        // 写入响应体
        PrintWriter writer = httpServletResponse.getWriter();
        SecurityContextHolder.clearContext();
        writer.write(resultJSON);
        writer.flush();
        writer.close();
    }

}
