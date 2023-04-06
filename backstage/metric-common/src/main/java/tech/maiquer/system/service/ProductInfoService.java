package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.LayuiQuery3;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.Product;

import java.util.List;

@Service
public interface ProductInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id
     * @return 实例对象
     */
    Result queryById(Long id);

    /**
     * 查询所有
     * @return
     */
    Result queryAll();

    /**
     * 分页查询
     *
     * @param current 当前页
     * @param size    分页大小
     * @return 查询结果
     */
    Paging<Product> queryByPage(Integer current, Integer size);

    /**
     * 新增数据
     *
     * @param product
     * @return 实例对象
     */
    Result insert(Product product);

    /**
     * 修改数据
     *
     * @param product
     * @return 实例对象
     */
    Result update(Product product);

    /**
     * 通过主键删除数据
     *
     * @param id
     * @return 是否成功
     */
    Result deleteById(Long id);

    /**
     * 通过测评编号删除数据
     * @param evaId
     */
    Result deleteByEvaIdInt(Integer evaId);


    /**
     * layui里模糊查询方法
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


}
