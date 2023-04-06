package tech.maiquer.api.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.service.MenuService;

import javax.annotation.Resource;
import java.util.HashSet;


/**
 * @Author lin
 * @Description
 * @Date: 2022/5/3 10:24
 */
@RequestMapping("/api/menu")
@RestController
@Api(value = "菜单", tags = "菜单接口")
@CrossOrigin
public class MenuApi {

    @Resource
    private MenuService menuService;

    @GetMapping("/queryAll")
    @PreAuthorize("hasPermission('/api/menu/queryAll','sys:menu:queryAll')")
    @ApiOperation(value = "查询所有菜单", notes = "注意正确传参")
    public Result queryAll() {
        return menuService.queryAll();
    }

    @PostMapping("/queryAllByRoleIds")
    @PreAuthorize("hasPermission('/api/menu/queryAllByRoleIds','sys:menu:queryByRole')")
    @ApiOperation(value = "依据角色查询所有菜单", notes = "注意正确传参")
    public Result queryAllByRoleIds(@RequestParam HashSet<Integer> roleIds) {
        return menuService.queryAllByRoleIds(roleIds);
    }


}
