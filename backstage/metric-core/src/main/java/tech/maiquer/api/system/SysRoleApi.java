package tech.maiquer.api.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.SysRole;
import tech.maiquer.system.service.SysRoleService;

import javax.annotation.Resource;

/**
 * @Author lin
 * @Description
 * @Date: 2022/5/13 16:32
 */
@RestController
@CrossOrigin
@Api(value = "角色", tags = "角色接口")
@RequestMapping("/api/role")
public class SysRoleApi {

    @Resource
    private SysRoleService sysRoleService;

    @GetMapping("/queryAll")
    @PreAuthorize("hasPermission('/api/role/queryAll','sys:role:queryAll')")
    @ApiOperation(value = "查询所有角色")
    public Result queryAll(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "limit", required = false) Integer limit,
                           @RequestParam(value = "roleName", required = false) String roleName,
                           @RequestParam(value = "description", required = false) String description) {

        return sysRoleService.queryAll(page, limit, roleName, description);
    }

    @PutMapping("/updateEnable")
    @PreAuthorize("hasPermission('/api/role/updateEnable','sys:role:updateEnable')")
    @ApiOperation(value = "修改角色启用", notes = "注意正确传参")
    public Result updateEnable(@RequestParam Integer roleId,
                               @RequestParam Boolean enable) {
        return sysRoleService.updateEnable(roleId, enable);
    }

    @PostMapping("/addRole")
    @PreAuthorize("hasPermission('/api/role/addRole','sys:role:add')")
    @ApiOperation(value = "添加角色", notes = "注意Body主体")
    public Result insertRole(@RequestBody SysRole sysRole) {

        if (sysRole.getRoleId() == null) {
            return sysRoleService.insert(sysRole);
        } else {
            return sysRoleService.update(sysRole);
        }

    }

    @DeleteMapping("/deleteRole/{roleId}")
    @PreAuthorize("hasPermission('/api/role/deleteRole','sys:role:delete')")
    @ApiOperation(value = "通过Id删除角色", notes = "注意正确传参")
    public Result deleteById(@PathVariable("roleId") Integer roleId) {
        return sysRoleService.deleteById(roleId);
    }


}
