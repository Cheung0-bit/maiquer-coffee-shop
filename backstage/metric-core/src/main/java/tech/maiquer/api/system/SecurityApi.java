package tech.maiquer.api.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Api(tags = "安全机制接口")
public class SecurityApi {

    @PostMapping("/api/login")
    @ApiOperation(value = "登录", notes = "form-data类型")
    public void login(@ApiParam(name = "username", value = "账户", required = true) @RequestParam String username, @ApiParam(name = "password", value = "密码", required = true) @RequestParam String password, @ApiParam(name = "captcha", value = "验证码", required = true) @RequestParam String captcha) {

    }

    @GetMapping("/api/logout")
    @ApiOperation(value = "注销", notes = "直接请求")
    public void logout() {

    }

}
