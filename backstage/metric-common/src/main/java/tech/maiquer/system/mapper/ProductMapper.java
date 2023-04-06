package tech.maiquer.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import tech.maiquer.common.model.LayuiQuery3;
import tech.maiquer.system.domain.Product;

import java.util.List;

@Mapper
public interface ProductMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Product queryById(Long id);

    /**
     * 通过测评id查询
     * @param id
     * @return
     */
    Product queryByEvaId(Integer id);

    /**
     * 新增数据
     *
     * @param product 实例对象
     * @return 影响行数
     */
    int insert(Product product);

    /**
     * 修改数据
     *
     * @param product 实例对象
     * @return 影响行数
     */
    int update(Product product);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 通过测评ID删除
     * @param evaId
     * @return
     */
    int deleteByEvaIdInt(Integer evaId);

    /**
     * 查询所有
     * @return
     */
    List<Product> queryAll();

    /**
     * layui里面模糊查询方法--查商品
     * @param param
     * @return
     */
    List<Product> getProductList(LayuiQuery3 param);

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    Long countProductList(LayuiQuery3 param);

    /**
     * 根据商品名进行模糊查询
     * @param param
     * @return
     */
    List<Product> queryAll1(LayuiQuery3 param);

}
