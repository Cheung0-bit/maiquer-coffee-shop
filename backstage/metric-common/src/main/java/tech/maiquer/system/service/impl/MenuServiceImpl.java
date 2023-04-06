package tech.maiquer.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.Menu;
import tech.maiquer.system.mapper.MenuMapper;
import tech.maiquer.system.service.MenuService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;


/**
 * @Author lin
 * @Description Menu集合实现类
 * @Date: 2022/5/3 10:18
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    /**
     * 返回所有Menu集合
     *
     * @return
     */
    @Override
    public Result<List<Menu>> queryAll() {

        try {
            List<Menu> menus = menuMapper.queryAll();
            return Result.success(menus);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("MongoDB异常");
        }

    }

    /**
     * 根据角色返回符合的Menu集合
     *
     * @param roleIds
     * @return
     */
    @Override
    public Result<List<Menu>> queryAllByRoleIds(HashSet<Integer> roleIds) {
        try {
            List<Menu> menus = menuMapper.queryByRoleIds(roleIds);
            return Result.success(menus);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.failure("MongoDB异常");
        }
    }
}
