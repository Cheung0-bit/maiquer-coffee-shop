package tech.maiquer.common.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tech.maiquer.common.config.property.JwtProperty;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.utils.JwtUtils;
import tech.maiquer.system.domain.SysUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecureAuthSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 配置登录成功 封装JWT
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        // 设定响应状态码为200
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        // 设定响应内容是utf-8编码的json类型
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        //组装JWT
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        //走MyAuthSuccessHandler为正常用户登录
        String token = JwtProperty.tokenPrefix + JwtUtils.createAccessToken(sysUser);
        //封装返回体
        Map<String, Object> resData = new HashMap<>();
        resData.put("userId", sysUser.getId());
        resData.put("username", sysUser.getUsername());
        resData.put("jwt", token);
        // 序列化结果对象为JSON
        String resultJSON = JSON.toJSONString(Result.success(resData));
        // 写入响应体
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(resultJSON);
        writer.flush();
        writer.close();

    }

}
