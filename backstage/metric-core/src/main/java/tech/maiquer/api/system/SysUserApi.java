package tech.maiquer.api.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.annotation.UserUnique;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.service.SysUserService;

import javax.annotation.Resource;
import java.util.HashSet;

@RestController
@CrossOrigin
@Api(value = "用户", tags = "用户接口")
@RequestMapping("/api/user")
public class SysUserApi {

    /**
     * 服务对象
     */
    @Resource
    private SysUserService sysUserService;

    @DeleteMapping("/batchRemove")
    @PreAuthorize("hasPermission('/api/user/batchRemove','sys:user:delete')")
    @ApiOperation(value = "通过Ids删除用户", notes = "注意正确传参")
    public Result deleteByIds(@ApiParam(name = "userIds", value = "用户编号IDs", required = true) @RequestParam("userIds") String userIds) {
        return sysUserService.deleteByIds(userIds);
    }

    /**
     * 优雅查询
     *
     * @param page
     * @param limit
     * @param userName
     * @param nickName
     * @param phone
     * @param email
     * @return
     */
    @GetMapping("/queryAll")
    @PreAuthorize("hasPermission('/api/user/queryAll','sys:user:queryAll')")
    @ApiOperation(value = "查询所有")
    public Result queryAll(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "limit", required = false) Integer limit,
                           @RequestParam(value = "userName", required = false) String userName,
                           @RequestParam(value = "nickName", required = false) String nickName,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "email", required = false) String email) {
        return sysUserService.queryAll(page, limit, userName, nickName, phone, email);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/queryById/{id}")
    @PreAuthorize("hasPermission('/api/user/queryById','sys:user:queryById')")
    @ApiOperation(value = "用户ID查询", notes = "注意传参格式")
    @UserUnique("/queryById")
    public Result queryById(@ApiParam(name = "id", value = "用户编号ID", required = true) @PathVariable("id") Long id) {
        return sysUserService.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param sysUser 实体
     * @return 新增结果
     */
    @PostMapping("/addUser")
    @PreAuthorize("hasPermission('/api/user/addUser','sys:user:add')")
    @ApiOperation(value = "添加用户", notes = "注意Body主体")
    public Result add(@RequestBody SysUser sysUser) {

        if (sysUser.getId() == null) {
            return sysUserService.insert(sysUser);
        } else {
            return sysUserService.update(sysUser);
        }

    }

    @PostMapping("/setRole")
    @PreAuthorize("hasPermission('/api/user/setRole','sys:user:setRole')")
    @ApiOperation(value = "分配角色")
    public Result setRole(@RequestParam Long userId, @RequestParam HashSet<Integer> roleIds) {
        return sysUserService.setRole(userId, roleIds);
    }

    /**
     * 编辑数据
     *
     * @param sysUser 实体
     * @return 编辑结果
     */
    @PutMapping("/editUser")
    @PreAuthorize("hasPermission('/api/user/editUser','sys:user:edit')")
    @ApiOperation(value = "编辑用户信息", notes = "注意正确传参")
    public Result edit(@RequestBody SysUser sysUser) {
        return sysUserService.update(sysUser);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasPermission('/api/user/deleteUser','sys:user:delete')")
    @ApiOperation(value = "通过Id删除用户", notes = "注意正确传参")
    public Result deleteById(@ApiParam(name = "id", value = "用户编号ID", required = true) @PathVariable("id") Long id) {
        return sysUserService.deleteById(id);
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @PostMapping("/addMyEva")
    @PreAuthorize("hasPermission('/api/user/addMyEva','sys:user:addMyEva')")
    @ApiOperation(value = "添加我的测评", notes = "注意正确传参")
    public Result addMyEva(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "evaId", value = "测评编号ID", required = true) @RequestParam Integer evaId) {
        Result result = sysUserService.addMyEva(userId, evaId);
        SysUser sysUser = sysUserService.queryById(userId).getData();
        if (result.getCode() == 0) {
            result.setData(sysUser);
        }
        return result;

    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @PostMapping("/deleteMyEva")
    @PreAuthorize("hasPermission('/api/user/deleteMyEva','sys:user:deleteMyEva')")
    @ApiOperation(value = "删除我的测评", notes = "注意正确传参")
    public Result deleteMyEva(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "evaId", value = "测评编号ID", required = true) @RequestParam Integer evaId) {
        Result result = sysUserService.deleteMyEva(userId, evaId);
        SysUser sysUser = sysUserService.queryById(userId).getData();
        if (result.getCode() == 0) {
            result.setData(sysUser);
        }
        return result;
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @PostMapping("/addLikeEva")
    @PreAuthorize("hasPermission('/api/user/addLikeEva','sys:user:addLikeEva')")
    @ApiOperation(value = "添加收藏", notes = "注意正确传参")
    @UserUnique("/addLikeEva")
    public Result addLikeEva(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "evaId", value = "测评编号ID", required = true) @RequestParam Integer evaId) {
        Result result = sysUserService.addLikeEva(userId, evaId);
        SysUser sysUser = sysUserService.queryById(userId).getData();
        if (result.getCode() == 0) {
            result.setData(sysUser);
        }
        return result;
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @PostMapping("/deleteLikeEva")
    @PreAuthorize("hasPermission('/api/user/deleteLikeEva','sys:user:deleteLikeEva')")
    @ApiOperation(value = "删除收藏", notes = "注意正确传参")
    @UserUnique("/deleteLikeEva")
    public Result deleteLikeEva(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "evaId", value = "测评编号ID", required = true) @RequestParam Integer evaId) {
        Result result = sysUserService.deleteLikeEva(userId, evaId);
        SysUser sysUser = sysUserService.queryById(userId).getData();
        if (result.getCode() == 0) {
            result.setData(sysUser);
        }
        return result;
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @PostMapping("/addGiftEva")
    @PreAuthorize("hasPermission('/api/user/addGiftEva','sys:user:addGiftEva')")
    @ApiOperation(value = "添加赠送", notes = "注意正确传参")
    public Result addGiftEva(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "evaId", value = "测评编号ID", required = true) @RequestParam Integer evaId) {
        Result result = sysUserService.addGiftEva(userId, evaId);
        SysUser sysUser = sysUserService.queryById(userId).getData();
        if (result.getCode() == 0) {
            result.setData(sysUser);
        }
        return result;
    }

    /**
     * @param userId
     * @param evaId
     * @return
     */
    @PostMapping("/deleteGiftEva")
    @PreAuthorize("hasPermission('/api/user/deleteGiftEva','sys:user:deleteGiftEva')")
    @ApiOperation(value = "删除赠送", notes = "注意正确传参")
    @UserUnique("/deleteGiftEva")
    public Result deleteGiftEva(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "evaId", value = "测评编号ID", required = true) @RequestParam Integer evaId) {
        Result result = sysUserService.deleteGiftEva(userId, evaId);
        SysUser sysUser = sysUserService.queryById(userId).getData();
        if (result.getCode() == 0) {
            result.setData(sysUser);
        }
        return result;
    }

    /**
     * @param userId
     * @param nickname
     * @return
     */
    @PutMapping("/updateNickName")
    @PreAuthorize("hasPermission('/api/user/updateNickName','sys:user:updateNickName')")
    @ApiOperation(value = "修改用户昵称", notes = "注意正确传参")
    @UserUnique("/updateNickName")
    public Result updateUserName(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "nickname", value = "用户昵称", required = true) @RequestParam String nickname) {
        return sysUserService.updateNickName(userId, nickname);
    }

    /**
     * 管理员修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @PutMapping("/updatePassword")
    @PreAuthorize("hasPermission('/api/user/updatePassword','sys:user:updatePassword')")
    @ApiOperation(value = "修改用户密码", notes = "注意正确传参")
    @UserUnique("/updatePassword")
    public Result updatePassword(@ApiParam(name = "oldPassword", value = "原密码", required = true) @RequestParam String oldPassword, @ApiParam(name = "newPassword", value = "新密码", required = true) @RequestParam String newPassword, @ApiParam(name = "confirmPassword", value = "确认密码", required = true) @RequestParam String confirmPassword) {
        return sysUserService.updatePassword(oldPassword, newPassword, confirmPassword);
    }

    /**
     * @param userId
     * @param status
     * @return
     */
    @PutMapping("/updateStatus")
    @PreAuthorize("hasPermission('/api/user/updateStatus','sys:user:updateStatus')")
    @ApiOperation(value = "修改用户状态", notes = "注意正确传参")
    public Result updateStatus(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "status", value = "测评编号ID", required = true) @RequestParam String status) {
        return sysUserService.updateStatus(userId, status);
    }

    /**
     * @param userId
     * @param avatar
     * @return
     */
    @PutMapping("/updateAvatar")
    @PreAuthorize("hasPermission('/api/user/updateAvatar','sys:user:updateAvatar')")
    @ApiOperation(value = "修改用户头像", notes = "注意正确传参")
    @UserUnique("/updateAvatar")
    public Result updateAvatar(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "avatar", value = "测评编号ID", required = true) @RequestParam String avatar) {
        return sysUserService.updateAvatar(userId, avatar);
    }

    /**
     * @param userId
     * @param backImg
     * @return
     */
    @PutMapping("/updateBackImg")
    @PreAuthorize("hasPermission('/api/user/updateBackImg','sys:user:updateBackImg')")
    @ApiOperation(value = "修改用户背景", notes = "注意正确传参")
    @UserUnique("/updateBackImg")
    public Result updateBackImg(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "backImg", value = "测评编号ID", required = true) @RequestParam String backImg) {
        return sysUserService.updateBackImg(userId, backImg);
    }

    /**
     * @param userId
     * @param signature
     * @return
     */
    @PutMapping("/updateSignature")
    @PreAuthorize("hasPermission('/api/user/updateSignature','sys:user:updateSignature')")
    @ApiOperation(value = "修改用户个性签名", notes = "注意正确传参")
    @UserUnique("/updateSignature")
    public Result updateSignature(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "signature", value = "测评编号ID", required = true) @RequestParam String signature) {
        return sysUserService.updateSignature(userId, signature);
    }

    /**
     * @param userId
     * @param enable
     * @return
     */
    @PutMapping("/updateEnable")
    @PreAuthorize("hasPermission('/api/user/updateEnable','sys:user:updateEnable')")
    @ApiOperation(value = "修改用户启用", notes = "注意正确传参")
    public Result updateEnable(@ApiParam(name = "userId", value = "用户编号ID", required = true) @RequestParam Long userId, @ApiParam(name = "enable", value = "用户是否启用", required = true) @RequestParam Boolean enable) {
        return sysUserService.updateEnable(userId, enable);
    }

    @GetMapping("/account")
    @ApiOperation(value = "获取管理员相关信息")
    public Result getAccount() {
        return sysUserService.getUserAccount();
    }

    @PostMapping("/resetPassword")
    @ApiOperation(value = "重置密码")
    public Result resetPassword(@RequestParam Long userId) {
        return sysUserService.resetPassword(userId);
    }


}
