package tech.maiquer.common.filter;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tech.maiquer.common.config.property.JwtProperty;
import tech.maiquer.common.utils.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationTokenFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 获取请求头中JWT的Token
        String tokenHeader = request.getHeader(JwtProperty.tokenHeader);
        try {
            if (tokenHeader == null)
                tokenHeader = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt")).collect(Collectors.toList()).get(0).getValue();
        } catch (Exception e) {
            log.debug("Token为空");
        }

        // 判断是否为空或是否以Metric前缀开头
        if (tokenHeader != null && tokenHeader.startsWith(JwtProperty.tokenPrefix)) {
            try {
                // 解析JWT
                Claims claims = JwtUtils.decryptAccessToken(tokenHeader);
                // 获取用户名
                String username = claims.getSubject();
                String userId = claims.getId();
                Set<GrantedAuthority> authorities = new HashSet<>();
                String authority = claims.get("authorities").toString();
                if (!authority.isEmpty()) {
                    HashSet<Map<String, String>> authorityMap = JSONObject.parseObject(authority, HashSet.class);
                    for (Map<String, String> sysRole : authorityMap) {
                        if (!sysRole.toString().isEmpty()) {
                            authorities.add(new SimpleGrantedAuthority(sysRole.get("authority")));
                        }
                    }
                }
                if (!username.isEmpty() && !userId.isEmpty()) {
                    // Jwt过滤器中使用我们自己的权限集合 不适用Security的Authorities
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                log.error("Token过期");
            } catch (Exception e) {
                log.error("Token无效");
            }
        }
        filterChain.doFilter(request, response);
    }

}
