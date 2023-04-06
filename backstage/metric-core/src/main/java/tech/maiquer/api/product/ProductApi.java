package tech.maiquer.api.product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.model.LayuiQuery3;
import tech.maiquer.common.model.LayuiResult;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.Product;
import tech.maiquer.system.service.ProductInfoService;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/product")
public class ProductApi {

    @Resource
    private ProductInfoService productInfoService;

    @PostMapping("/addProduct")
    @PreAuthorize("hasPermission('/api/product/addProduct','wechat:admin:addProduct')")
    @ApiOperation(value = "添加", notes = "注意正确传参")
    public Result add(@RequestBody Product product) {
        return productInfoService.insert(product);
    }

    @DeleteMapping("/deleteProduct/{id}")
    @PreAuthorize("hasPermission('/api/product/deleteProduct','wechat:admin:deleteProduct')")
    @ApiOperation(value = "删除", notes = "注意正确传参")
    public Result delete(@ApiParam(name = "id", value = "产品编号ID", required = true) @PathVariable("id") Long id) {
        return productInfoService.deleteById(id);
    }

    @GetMapping("/queryAll")
    @PreAuthorize("hasPermission('/api/product/queryAll','wechat:admin:queryAllProduct')")
    @ApiOperation(value = "查询所有")
    public Result queryAll() {
        return productInfoService.queryAll();
    }

    /**
     * 分页查询
     *
     * @param current 当前页
     * @param size    分页大小
     * @return 查询结果
     */
    @GetMapping("/queryByPage/{page}/{size}")
    @PreAuthorize("hasPermission('/api/product/queryByPage','wechat:admin:queryByPageProduct')")
    @ApiOperation(value = "分页查询", notes = "注意传参格式")
    public Paging<Product> queryByPage(@ApiParam(name = "page", value = "当前页", required = true) @PathVariable("page") Integer current, @ApiParam(name = "size", value = "分页大小", required = true) @PathVariable("size") Integer size) {
        return productInfoService.queryByPage(current, size);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/queryById/{id}")
    @PreAuthorize("hasPermission('/api/product/queryById','wechat:admin:queryByIdProduct')")
    @ApiOperation(value = "产品ID查询", notes = "注意传参格式")
    public Result queryById(@ApiParam(name = "id", value = "用户编号ID", required = true) @PathVariable("id") Long id) {
        return productInfoService.queryById(id);
    }

    /**
     * 编辑商品信息
     *
     * @param product
     * @return Result
     */
    @PostMapping("/editProduct")
    @PreAuthorize("hasPermission('/api/product/editProduct','wechat:admin:editProduct')")
    @ApiOperation(value = "编辑商品信息", notes = "注意正确传参")
    public Result update(@RequestBody Product product) {
        return productInfoService.update(product);
    }

    @GetMapping("/layui/list")
    public LayuiResult<Object> getProductList(LayuiQuery3 param) {
        List<Product> list = productInfoService.getProductList(param);
        Long count = productInfoService.countProductList(param);
        return LayuiResult.success(list,count);
    }
}
