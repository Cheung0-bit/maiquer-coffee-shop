package tech.maiquer.api.alibaba;

import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.alibaba.service.SmsService;
import tech.maiquer.common.config.property.JwtProperty;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.utils.JwtUtils;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@Api(tags = "SMS服务接口")
@RequestMapping("/api/sms")
public class SmsApi {

    @Resource
    private SmsService smsService;

    @Resource
    private SysUserMapper sysUserMapper;

    @PostMapping("/smsCodeSend")
    public Result smsSend(@RequestParam String phone) {
        return smsService.smsCodeSend(phone);
    }

    @PostMapping({"/login", "/login/{userId}"})
    public Result smsLogin(@RequestParam String phone, @RequestParam String code, @PathVariable(value = "userId", required = false) Long userId) {

        Result result = null;
        result = smsService.smsCodeVerify(phone, code);
        if (result.getCode() != 0) {
            return result;
        }
        // 根据不同的场景进行不同的业务逻辑
        result = userId == null ? smsService.smsUserDoLogin(phone) : smsService.smsUserBind(userId, phone);

        // 构建jwt
        if (result.getCode() == 0) {
            SysUser sysUser = (SysUser) result.getData();
            String jwtToken = JwtProperty.tokenPrefix + JwtUtils.createAccessToken(sysUserMapper.queryByNameSysUser(sysUser.getUsername()));
            Map<String, Object> map = new HashMap<>();
            map.put("userId", sysUser.getId());
            map.put("jwt", jwtToken);
            result.setData(map);
        }
        return result;

    }

    @PutMapping("/unbind")
    @PreAuthorize("hasPermission('/api/sms/unbind','alibaba:sms:unbind')")
    public Result unBind(@RequestParam Long userId) {
        return smsService.smsUserUnBind(userId);
    }


}
