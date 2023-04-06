package tech.maiquer.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.LayuiQuery3;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.service.ProductInfoService;
import tech.maiquer.system.domain.Evaluation;
import tech.maiquer.system.domain.Product;
import tech.maiquer.system.mapper.EvaluationMapper;
import tech.maiquer.system.mapper.ProductMapper;

import javax.annotation.Resource;
import java.util.List;

import static tech.maiquer.common.model.ResultCode.*;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private EvaluationMapper evaluationMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id
     * @return 实例对象
     */
    @Override
    public Result queryById(Long id) {
        Product product = productMapper.queryById(id);
        if (product == null) {
            return Result.failure(QUERY_FAIL);
        }
        return Result.success(product);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public Result queryAll() {
        List<Product> products = productMapper.queryAll();
        return Result.success(products);
    }

    /**
     * 分页查询
     *
     * @param current 当前页
     * @param size    分页大小
     * @return 查询结果
     */
    @Override
    public Paging<Product> queryByPage(Integer current, Integer size) {
        Page<Product> productPage = PageHelper.startPage(current, size).doSelectPage(() -> productMapper.queryAll());
        Paging<Product> productPaging = new Paging<>(productPage);
        return productPaging;
    }

    /**
     * 新增数据
     *
     * @param product
     * @return 实例对象
     */
    @Override
    public Result insert(Product product) {

        Evaluation evaluation = null;
        evaluation = evaluationMapper.queryById(product.getEvaId());
        if (evaluation == null) {
            return Result.failure(EVA_NOT_EXIST);
        }
        product.setTitle(evaluation.getName());
        product.setPrice((int) (evaluation.getPrice().doubleValue() * 100));
        productMapper.insert(product);
        return Result.success(product);

    }

    /**
     * 修改数据
     *
     * @param product
     * @return 实例对象
     */
    @Override
    public Result update(Product product) {
        Evaluation evaluation = evaluationMapper.queryById(product.getEvaId());
        product.setTitle(evaluation.getName());
        product.setPrice((int) (evaluation.getPrice().doubleValue() * 100));
        productMapper.update(product);
        return Result.success(product);
    }

    /**
     * 通过主键删除数据
     *
     * @param id
     * @return 是否成功
     */
    @Override
    public Result deleteById(Long id) {
        productMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 通过测评编号删除数据
     *
     * @param evaId
     */
    @Override
    public Result deleteByEvaIdInt(Integer evaId) {
        productMapper.deleteByEvaIdInt(evaId);
        return Result.success();
    }

    /**
     * layui里模糊查询的方式
     * @param param
     * @return
     */
    @Override
    public List<Product> getProductList(LayuiQuery3 param) {
        if(param.getTitle()!=null) {
            param.setTitle("%" + param.getTitle() + "%");
            return productMapper.getProductList(param);
        }
        return productMapper.queryAll1(param);
    }

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    @Override
    public Long countProductList(LayuiQuery3 param) {
        return productMapper.countProductList(param);
    }


}
