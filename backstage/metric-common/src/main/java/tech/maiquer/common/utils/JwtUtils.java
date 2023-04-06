package tech.maiquer.common.utils;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tech.maiquer.common.config.property.JwtProperty;
import tech.maiquer.system.domain.SysUser;

import java.util.Date;

public class JwtUtils {

    /**
     * 生成Jwt
     *
     * @param sysUser
     * @return
     */
    public static String createAccessToken(SysUser sysUser) {

        String token = null;

        // 登陆成功生成JWT
        token = Jwts.builder()
                // 放入用户ID
                .setId(String.valueOf(sysUser.getId()))
                // 用户昵称
                .setSubject(sysUser.getUsername())
                // 签发时间
                .setIssuedAt(new Date())
                // 签发者
                .setIssuer("metric")
                // 自定义属性 放入用户拥有权限
                .claim("authorities", JSON.toJSONString(sysUser.getAuthorities()))
                // 失效时间
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperty.expiration * 1000))
                // 签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, JwtProperty.secret)
                .compact();

        return token;
    }

    public static Claims decryptAccessToken(String tokenHeader) {

        // 截取JWT前缀
        String token = tokenHeader.replace(JwtProperty.tokenPrefix, "");
        // 解析JWT
        return Jwts.parser()
                .setSigningKey(JwtProperty.secret)
                .parseClaimsJws(token)
                .getBody();

    }

    public static Long obtainUserId() {

        String jwt = ServletUtil.getHeader("Authorization");
        Claims claims = JwtUtils.decryptAccessToken(jwt);
        return Long.valueOf(claims.getId());

    }

}
