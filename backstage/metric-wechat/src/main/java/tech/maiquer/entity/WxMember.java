package tech.maiquer.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class WxMember {

    /**
     * 用户openid
     */
    private String openid;

    /**
     * 用户unionid
     */
    private String unionid;

    /**
     * 微信名称
     */
    private String nickname;

    /**
     * 微信用户性别
     */
    private Integer sex;

    /**
     * 微信头像
     */
    private String headimgurl;

    /**
     * 用户省份
     */
    private String province;

    /**
     * 用户城市
     */
    private String city;

    /**
     * 用户所属国家
     */
    private String country;

    /**
     * 创建时间
     */
    private Timestamp createTime;

}