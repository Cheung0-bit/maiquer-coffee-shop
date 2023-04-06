package tech.maiquer.entity;

import lombok.Data;

@Data
public class WxToken {

    //接口调用凭证
    private String access_token;
    //access_token接口调用凭证超时间，单位(秒)
    private String expires_in;
    //刷新令牌
    private String refresh_token;
    //授权用户唯一标识
    private String openid;
    //用户自己授权的作用域使用（，）分隔
    private String scope;
    //当前亲当该网站应用以获得该用户的userinfo授权时，才会出现该字段
    private String unionid;

}
