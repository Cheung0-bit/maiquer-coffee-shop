package tech.maiquer.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.maiquer.common.model.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author lin
 * @Description 接口记录
 * @Date: 2022/4/28 22:12
 */
@Aspect
@Component
public class ApiCountRedisAspect {

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @Pointcut("execution(* tech.maiquer.api..*.*(..))")
    public void count() {

    }

    @Around("count()")
    public Object redisCount(ProceedingJoinPoint pjp) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        try {
            assert attributes != null;
            request = attributes.getRequest();
        } catch (Exception e) {
            return Result.failure(e.toString());
        }

        String uri = request.getRequestURI();
        if (redisTemplate.opsForValue().get(uri) == null) {
            redisTemplate.opsForValue().set(uri, 1L);
        } else {
            redisTemplate.opsForValue().increment(uri);
        }

        try {
            return pjp.proceed();
        } catch (Throwable e) {
            return Result.failure("接口计数失败");
        }


    }

}