package tech.maiquer.api.wechat;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.maiquer.common.config.property.JwtProperty;
import tech.maiquer.common.config.property.WxCodeProperty;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.ResultCode;
import tech.maiquer.common.utils.JwtUtils;
import tech.maiquer.entity.WxMember;
import tech.maiquer.entity.WxToken;
import tech.maiquer.service.WxUserService;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.SysUserMapper;
import tech.maiquer.system.service.SysUserService;
import tech.maiquer.utils.WeChatHttpUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "微信登录接口")
public class WechatLoginApi {

    @Resource
    private WxCodeProperty wxCodeProperty;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private WxUserService wxUserService;

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 通过code参数并通过code获取access_token
     *
     * @param code
     * @param state
     * @param httpServletResponse
     * @return
     */
    @GetMapping("/callback")
    @ApiOperation(value = "微信登录", notes = "该接口为回调接口，可以直接在浏览器访问/api/wechat/login接口,并登录得到用户信息(该接口的结果)")
    public Result callback(@ApiParam(name = "code", value = "授权码", required = false) Long userId, @ApiParam(name = "code", value = "授权码", required = true) String code, @ApiParam(name = "state", value = "状态") String state, HttpServletResponse httpServletResponse) {
        log.info("微信服务器回调...");
        //获取code值
        if (code == null) {
            throw new RuntimeException("用户禁止授权...");
        }

        String baseAccessTokenUrl =
                "https://api.weixin.qq.com/sns/oauth2/access_token" +
                        "?appid=%s" +
                        "&secret=%s" +
                        "&code=%s" +
                        "&grant_type=authorization_code";
        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                wxCodeProperty.getAppId(),
                wxCodeProperty.getAppSecret(),
                code
        );
        //使用java代码发http请求
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;

        httpGet = new HttpGet(accessTokenUrl);

        try {
            response = WeChatHttpUtils.getClient().execute(httpGet);
            String tokenContent = EntityUtils.toString(response.getEntity());
            WxToken token = JSON.parseObject(tokenContent, WxToken.class);

            //获取个人信息
            String baseUserUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userUrl = String.format(
                    baseUserUrl,
                    token.getAccess_token(),
                    token.getOpenid()
            );
            httpGet = new HttpGet(userUrl);
            response = WeChatHttpUtils.getClient().execute(httpGet);
            String userContent = EntityUtils.toString(response.getEntity());
            WxMember wxMember = JSON.parseObject(userContent, WxMember.class);


            if (null!=wxMember&&null!=wxMember.getOpenid()) {
                String nickName = String.valueOf(wxMember.getNickname());
                /**进行编码*/
                nickName = new String(nickName.getBytes("ISO-8859-1"), "UTF-8");
                log.info("登录用户昵称：{}",nickName);
                wxMember.setNickname(nickName);
            }

            /**
             * 逻辑：
             *
             *  - 是否提供了userId
             *      - 若提供，则说明属于绑定业务场景 用户未使用微信登陆过 则通过userid查询出用户，再将微信信息塞入
             *      - 若无提供 获取openid，去用户表查询
             *          - 存在则已使用微信登陆过了 跳过塞入信息的业务 重新生成JWT塞入 返回结果
             *          - 不存在 则是无身份登录，创建新用户
             *
             */
            SysUser sysUser = null;
            SysUser getSysUser = null; // 声明一个替代容器
            if (userId == null) {

                String openId = wxMember.getOpenid();
                Result<SysUser> result = wxUserService.queryUserByOpenId(openId);
                if (result.getCode() != 0) {
                    getSysUser = wxUserService.createWxUser(wxMember).getData();
                    if (getSysUser == null) {
                        return Result.failure(ResultCode.INSERT_FAIL, "创建微信用户失败");
                    }
                } else {
                    getSysUser = result.getData();
                }
            } else {
                sysUser = sysUserService.queryById(userId).getData();
                if (sysUser == null) {
                    return Result.failure(ResultCode.QUERY_FAIL, "查找不到用户");
                } else {
                    Result<SysUser> result = wxUserService.bindWxUser(sysUser, wxMember);
                    if (result.getCode() != 0) {
                        return Result.failure(ResultCode.INSERT_FAIL, result.getData());
                    }
                    getSysUser = result.getData();
                    if (getSysUser == null) {
                        return Result.failure(ResultCode.INSERT_FAIL, "绑定微信用户失败");
                    }
                }
            }
            String jwtToken = JwtProperty.tokenPrefix + JwtUtils.createAccessToken(sysUserMapper.queryByNameSysUser(getSysUser.getUsername()));
            // 返回结果
            Map<String, Object> map = new HashMap<>();
            map.put("userId", getSysUser.getId());
            map.put("jwt", jwtToken);
            return new Result(ResultCode.SUCCESS, map);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("微信扫描登录异常");
        }
    }
}
