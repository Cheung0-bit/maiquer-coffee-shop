package tech.maiquer.common.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tech.maiquer.common.config.property.SecurityProperty;
import tech.maiquer.common.filter.JwtAuthenticationTokenFilter;
import tech.maiquer.common.handler.*;
import tech.maiquer.common.support.SecureCaptchaSupport;

import javax.annotation.Resource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
public class SecurityCoreConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private SecurityProperty securityProperty;

    /**
     * 自定义登录成功处理器
     */
    @Resource
    private SecureAuthSuccessHandler secureAuthSuccessHandler;

    /**
     * 自定义登录失败处理器
     */
    @Resource
    private SecureAuthFailureHandler secureAuthFailureHandler;
    /**
     * 自定义注销成功处理器
     */
    @Resource
    private SecureLogoutSuccessHandler secureLogoutSuccessHandler;

    /**
     * 自定义暂无权限处理器
     */
    @Resource
    private SecureAccessDeniedHandler secureAccessDeniedHandler;

    /**
     * 自定义未登录的处理器
     */
    @Resource
    private SecureAuthEntryPointHandler secureAuthEntryPointHandler;

    /**
     * 安全用户组
     */
    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 自定义验证码验证
     */
    @Resource
    private SecureCaptchaSupport securityCaptchaSupport;

    /**
     * 配置登录验证逻辑
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //这里可启用我们自己的登陆验证逻辑
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * 配置跨域访问资源
     * @return
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 配置security的控制逻辑
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(securityProperty.getBackResource()).hasAnyRole("SUPER_ADMIN", "SYS_ADMIN")
                .antMatchers(securityProperty.getOpenApi()).permitAll()
                .anyRequest().authenticated()
                .and()
                // 验证码验证类
                .addFilterBefore(securityCaptchaSupport, UsernamePasswordAuthenticationFilter.class)
                // 配置未登录自定义处理类
                .httpBasic().authenticationEntryPoint(secureAuthEntryPointHandler)
                .and()

                // 配置登录地址
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/api/login")
                // 配置登录成功自定义处理类
                .successHandler(secureAuthSuccessHandler)
                // 配置登录失败自定义处理类
                .failureHandler(secureAuthFailureHandler)
                .permitAll()
                .and()

                // 配置登出地址
                .logout()
                .logoutUrl("/api/logout")
                // 配置用户登出自定义处理类
                .logoutSuccessHandler(secureLogoutSuccessHandler)
                .and()

                // 配置没有权限自定义处理类
                .exceptionHandling().accessDeniedHandler(secureAccessDeniedHandler)
                .and()

                // 开启跨域
                .cors().configurationSource(corsConfigurationSource())
                .and()
                // 取消跨站请求伪造防护
                .csrf().disable();

        // 基于Token不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 允许iframe
        http.headers().frameOptions().disable();
        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT过滤器
        http.addFilter(new JwtAuthenticationTokenFilter(authenticationManager()));
    }


}
