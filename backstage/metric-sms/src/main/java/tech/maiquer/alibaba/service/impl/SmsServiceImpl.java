package tech.maiquer.alibaba.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tech.maiquer.alibaba.service.SmsService;
import tech.maiquer.common.config.property.SmsProperty;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.utils.UUIDUtils;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    // 逻辑的魅力！

    @Resource
    private SmsProperty smsProperty;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RedisTemplate redisTemplate;

    public Client createClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(smsProperty.getAccessKeyId())
                .setAccessKeySecret(smsProperty.getAccessKeySecret());
        config.endpoint = smsProperty.getDomain();
        return new Client(config);
    }

    /**
     * 生成4位数验证码
     *
     * @return
     */
    private String createCode() {
        String numStr = "123456789";
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(numStr.charAt(new Random().nextInt(numStr.length())));
        }
        return String.valueOf(sb);
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    @Override
    public Result smsCodeSend(String phone) {
        // 先判断上一次发送是否过期
        String getCode = (String) redisTemplate.opsForValue().get(phone);
        if (getCode != null) {
            return Result.failure("验证码未过期！");
        }
        // 生成验证码
        Map codeMap = new HashMap<>();
        String code = this.createCode();
        codeMap.put("code", code);
        String codeJson = JSON.toJSONString(codeMap);

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(smsProperty.getSignName())
                .setTemplateCode(smsProperty.getTemplateCode())
                .setTemplateParam(codeJson);
        Client client = null;
        try {
            client = this.createClient();
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("client构建失败！");
        }
        SendSmsResponse res = null;
        try {
            res = client.sendSms(sendSmsRequest);
        } catch (Exception e) {
            log.error("验证码发送失败----" + e);
            return Result.failure("验证码发送失败！");
        }
        String resCode = res.getBody().getCode();
        if (resCode.equals("OK")) {
            // 发送成功，存入redis数据库，设置过期时间为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return Result.success("验证码发送成功！");
        }
        return Result.failure(resCode);
    }

    /**
     * 验证码验证
     *
     * @param phone
     * @param code
     * @return
     */
    @Override
    public Result smsCodeVerify(String phone, String code) {
        // 从redis数据库中取出验证码
        String getCode = (String) redisTemplate.opsForValue().get(phone);
        if (getCode == null) {
            return Result.failure("验证码过期已经过期,请重新申请！");
        }
        if (!getCode.equals(code)) {
            return Result.failure("验证码错误！");
        }
        // 匹配成功，从redis数据库中删除该键
        redisTemplate.delete(phone);
        return Result.success();
    }

    /**
     * 通过手机号登录
     *
     * @param phone
     * @return
     */
    @Override
    public Result smsUserDoLogin(String phone) {

        SysUser getMyUser = null;
        // 先判断该手机号是否已被存在的用户绑定
        try {
            getMyUser = sysUserMapper.queryByPhoneSysUser(phone);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("手机号登录时通过手机号查找用户发生异常，请联系后端开发师！");
        }
        if (getMyUser == null) {
            // 构造用户
            SysUser sysUser = new SysUser();
            sysUser.setUsername(UUIDUtils.createName());
            sysUser.setNickname("手机号" + phone + "用户");
            sysUser.setPhone(phone);
            try {
                sysUserMapper.insert(sysUser);
                // 首次SMS登录 默认普通用户
                try {
                    sysUserMapper.addRole(sysUser.getId(), 4);
                } catch (Exception e) {
                    log.error("SQL Exception");
                }
            } catch (Exception e) {
                log.error(e.toString());
                return Result.failure("添加手机用户失败！");
            }
            return Result.success(sysUser);
        }
        return Result.success(getMyUser);
    }

    /**
     * 通过手机号绑定用户
     *
     * @param userId
     * @param phone
     * @return
     */
    @Override
    public Result smsUserBind(Long userId, String phone) {

        SysUser sysUser = null;
        // 先通过手机号查询是否已绑定用户
        try {
            sysUser = sysUserMapper.queryByPhoneSysUser(phone);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("绑定用户时通过手机号查找用户发生异常，请联系后端开发师！");
        }
        if (sysUser != null) {
            return Result.failure("该手机号已被绑定！");
        }
        try {
            sysUser = sysUserMapper.queryById(userId);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("查询用户SQL异常，请联系后端开发工程师！");
        }
        if (sysUser == null) {
            return Result.failure("用户不存在！");
        }
        sysUser.setPhone(phone);
        try {
            sysUserMapper.update(sysUser);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("绑定手机号时更新用户失败！");
        }
        return Result.success(sysUser);

    }

    /**
     * 解绑用户
     *
     * @param userId
     * @return
     */
    @Override
    public Result smsUserUnBind(Long userId) {
        SysUser sysUser = null;
        try {
            sysUser = sysUserMapper.queryById(userId);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("查询用户SQL异常，请联系后端开发工程师！");
        }
        if (sysUser == null) {
            return Result.failure("用户不存在！");
        }
        sysUser.setPhone(null);
        try {
            sysUserMapper.update(sysUser);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("解绑手机号时更新用户失败！");
        }
        return Result.success(sysUser);
    }

    /**
     * todo 通知服务
     *
     * @param phone
     * @return
     */
    @Override
    public Result smsNotification(String phone) {
        return null;
    }
}
