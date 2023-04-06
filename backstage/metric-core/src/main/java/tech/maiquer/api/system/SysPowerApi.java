package tech.maiquer.api.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.SysPower;
import tech.maiquer.system.service.SysPowerService;

import javax.annotation.Resource;
import java.util.HashSet;

/**
 * @Author lin
 * @Description 权限接口
 * @Date: 2022/5/13 20:33
 */
@RestController
@CrossOrigin
@Api(value = "权限", tags = "权限接口")
@RequestMapping("/api/power")
public class SysPowerApi {

    @Resource
    private SysPowerService sysPowerService;

    @GetMapping("/queryAll")
    @PreAuthorize("hasPermission('/api/power/queryAll','sys:power:queryAll')")
    @ApiOperation(value = "查询所有")
    public Result queryAll(@RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "limit", required = false) Integer limit,
                           @RequestParam(value = "powerName", required = false) String powerName,
                           @RequestParam(value = "powerCode", required = false) String powerCode,
                           @RequestParam(value = "powerUrl", required = false) String powerUrl) {
        return sysPowerService.queryAll(page, limit, powerName, powerCode, powerUrl);
    }

    @GetMapping("/queryByRole/{roleId}")
    @PreAuthorize("hasPermission('/api/power/queryByRole','sys:power:queryByRole')")
    @ApiOperation(value = "通过角色查询")
    public Result queryByRole(@PathVariable("roleId") Integer roleId) {
        return sysPowerService.queryByRole(roleId);
    }


    @PostMapping("/addPower")
    @PreAuthorize("hasPermission('/api/power/addPower','sys:power:add')")
    @ApiOperation(value = "添加权限", notes = "注意Body主体")
    public Result add(@RequestBody SysPower sysPower) {

        if (sysPower.getPowerId() == null) {
            return sysPowerService.insert(sysPower);
        } else {
            return sysPowerService.update(sysPower);
        }

    }

    @DeleteMapping("/deletePower/{powerId}")
    @PreAuthorize("hasPermission('/api/power/deletePower','sys:power:delete')")
    @ApiOperation(value = "通过Id删除权限", notes = "注意正确传参")
    public Result deleteById(@PathVariable("powerId") Integer powerId) {
        return sysPowerService.delete(powerId);
    }

    @PostMapping("/setPower")
    @PreAuthorize("hasPermission('/api/power/setPower','sys:power:setPower')")
    @ApiOperation(value = "分配权限", notes = "注意正确传参")
    public Result setPower(@RequestParam Integer roleId, @RequestParam HashSet<Integer> powerIds) {
        return sysPowerService.setPower(roleId, powerIds);
    }

    @PutMapping("/updateEnable")
    @PreAuthorize("hasPermission('/api/power/updateEnable','sys:power:updateEnable')")
    @ApiOperation(value = "修改权限启用", notes = "注意正确传参")
    public Result updateEnable(@RequestParam Integer powerId,
                               @RequestParam Boolean enable) {
        return sysPowerService.updateEnable(powerId, enable);
    }

}
