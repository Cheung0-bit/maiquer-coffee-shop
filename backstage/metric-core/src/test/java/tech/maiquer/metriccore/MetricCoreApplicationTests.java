package tech.maiquer.metriccore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.maiquer.common.config.property.*;
import tech.maiquer.system.domain.Menu;
import tech.maiquer.system.domain.Meta;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class MetricCoreApplicationTests {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private SwaggerProperty documentAutoProperties;

    @Resource
    private SecurityProperty securityProperty;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private SmsProperty smsProperty;

    @Resource
    private WxCodeProperty wxCodeProperty;

    @Resource
    private WxPayProperty wxPayProperty;

    @Test
    void swaggerConfigTest() {
        System.out.println(documentAutoProperties.toString());
    }

    @Test
    void jwtConfigTest() {
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date(System.currentTimeMillis()));
        System.out.println(new Date(System.currentTimeMillis() + JwtProperty.expiration * 1000));
    }

    @Test
    void securityConfigTest() {
        System.out.println(securityProperty.toString());
    }

    @Test
    void getPassword() {
        System.out.println(passwordEncoder.encode("admin"));
    }

    @Test
    void getSmsTest() {
        System.out.println(smsProperty.toString());
    }

    @Test
    void getWxCodeTest() {
        System.out.println(wxCodeProperty.toString());
    }

    @Test
    void getWxPayTest() {
        System.out.println(wxPayProperty.toString());
    }

    /**
     * 新增菜单
     */
    @Test
    void createMenu() {

        Menu menu = new Menu();
        HashSet<Integer> roleIds = new HashSet<>();
        roleIds.add(1);
        roleIds.add(2);
        menu.setRoleIds(roleIds);
        menu.setPath("/system");
        menu.setComponent("layout");
        menu.setName("system");
        Meta meta = new Meta();
        meta.setTitle("系统管理");
        meta.setIcon("system");
        menu.setMeta(meta);
        Menu menu1 = mongoTemplate.insert(menu);
        System.out.println(menu1);

    }

    /**
     * 返回该角色拥有的路由表
     */
    @Test
    void queryByRoleIds() {
        // 假定roleIds
        HashSet<Integer> roleIds = new HashSet<>();
        roleIds.add(2);
        List<Menu> menus = this.queryAll();
        menus = this.traverseRoutes(menus, roleIds);
        System.out.println(menus);
    }


    /**
     * 深度搜索处理路由表
     *
     * @param menus
     * @param roleIds
     * @return
     */
    private List<Menu> traverseRoutes(List<Menu> menus, HashSet<Integer> roleIds) {

        return menus.stream().map(menu -> {
            if (!menu.getRoleIds().containsAll(roleIds)) {
                menu = null;
            }
            if (menu != null && menu.getChildren() != null && menu.getChildren().size() != 0) {
                menu.setChildren(traverseRoutes(menu.getChildren(), roleIds));
            }
            return menu;
        }).collect(Collectors.toList());

    }

    /**
     * 查询集合中所有内容
     */
    private List<Menu> queryAll() {
        return mongoTemplate.findAll(Menu.class);
    }

    @Test
    void setTest() {

        HashSet<Integer> roleIds = new HashSet<>();
        roleIds.add(1);
        roleIds.add(2);
        System.out.println(roleIds);
        HashSet<Integer> checkedRoleIds = new HashSet<>();
        checkedRoleIds.add(1);
        checkedRoleIds.add(2);
        checkedRoleIds.add(3);
        System.out.println(checkedRoleIds);
        System.out.println(checkedRoleIds.contains(1));
        System.out.println(checkedRoleIds.containsAll(roleIds));

    }

}
