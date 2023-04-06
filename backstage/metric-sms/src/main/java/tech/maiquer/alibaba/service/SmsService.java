package tech.maiquer.alibaba.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;


@Service
public interface SmsService {

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    Result smsCodeSend(String phone);

    /**
     * 验证码验证
     * @param phone
     * @param code
     * @return
     */
    Result smsCodeVerify(String phone, String code);

    /**
     * 通过手机号登录
     * @param phone
     * @return
     */
    Result smsUserDoLogin(String phone);

    /**
     * 通过手机号绑定用户
     * @param userId
     * @param phone
     * @return
     */
    Result smsUserBind(Long userId, String phone);

    /**
     * 解绑用户
     * @param userId
     * @return
     */
    Result smsUserUnBind(Long userId);

    /**
     * todo 通知服务
     *
     * @param phone
     * @return
     */
    Result smsNotification(String phone);

}

