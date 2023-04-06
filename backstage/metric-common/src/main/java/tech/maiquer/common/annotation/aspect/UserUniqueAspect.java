package tech.maiquer.common.annotation.aspect;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.maiquer.common.annotation.UserUnique;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.system.Role;
import tech.maiquer.common.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @Author lin
 * @Description
 * @Date: 2022/4/27 14:31
 */
@Aspect
@Component
@Slf4j
public class UserUniqueAspect {

    @Pointcut(value = "@annotation(tech.maiquer.common.annotation.UserUnique)")
    public void access() {

    }

    @Around("@annotation(userUnique)")
    public Object identify(ProceedingJoinPoint pjp, UserUnique userUnique) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        try {
            request = attributes.getRequest();
        } catch (Exception e) {
            return Result.failure(e.toString());
        }

        UsernamePasswordAuthenticationToken userPrincipal = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
        Collection<GrantedAuthority> authorities = userPrincipal.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (!authority.toString().equals(Role.SUPER_ADMIN.getRoleName()) && !authority.toString().equals(Role.SYS_ADMIN.getRoleName())) {
                String jwt = request.getHeader("Authorization");
                Claims claims = JwtUtils.decryptAccessToken(jwt);
                Long realId = Long.valueOf(claims.getId());
                Long receivedId = (Long) pjp.getArgs()[0];
                if (!realId.equals(receivedId)) {
                    log.error("{}号用户,疑似在破坏系统 详见 {}", realId, userUnique.value());
                    return Result.failure("哦...发生了意外");
                }
            }
        }

        try {
            return pjp.proceed();
        } catch (Throwable e) {
            return Result.failure(e.toString());
        }

    }


}
