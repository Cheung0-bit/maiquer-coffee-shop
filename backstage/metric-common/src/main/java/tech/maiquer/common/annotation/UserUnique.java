package tech.maiquer.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author lin
 * @Description 鉴别用户信息伪造
 * @Date: 2022/4/27 14:28
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface UserUnique {

    String value() default "";

}
