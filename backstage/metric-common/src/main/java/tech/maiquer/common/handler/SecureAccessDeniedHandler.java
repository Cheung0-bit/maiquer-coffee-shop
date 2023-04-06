package tech.maiquer.common.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SecureAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 自定义权限不足处理器
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        // 设定响应状态码为403
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // 设定响应内容是utf-8编码的json类型
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        // 序列化结果对象为JSON
        String resultJSON = JSON.toJSONString(Result.failure(ResultCode.INSUFFICIENT_PERMISSIONS));
        // 写入响应体
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(resultJSON);
        writer.flush();
        writer.close();
    }
}
