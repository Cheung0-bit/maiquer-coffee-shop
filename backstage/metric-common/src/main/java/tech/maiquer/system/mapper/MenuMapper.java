package tech.maiquer.system.mapper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import tech.maiquer.system.domain.Menu;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lin
 * @Description
 * @Date: 2022/5/3 9:39
 */
@Component
public class MenuMapper {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 寻找Menu集合下所有数据
     *
     * @return
     */
    public List<Menu> queryAll() {
        return mongoTemplate.findAll(Menu.class);
    }

    public List<Menu> queryByRoleIds(HashSet<Integer> roleIds) {
        List<Menu> menus = this.queryAll();
        return this.traverseRoutes(menus, roleIds);

    }

    private List<Menu> traverseRoutes(List<Menu> menus, HashSet<Integer> roleIds) {

        return menus.stream().map(menu -> {
            if (!menu.getRoleIds().containsAll(roleIds)) {
                menu.setHidden(true);
            }
            if (menu.getChildren() != null && menu.getChildren().size() != 0) {
                menu.setChildren(traverseRoutes(menu.getChildren(), roleIds));
            }
            return menu;
        }).collect(Collectors.toList());

    }


}
