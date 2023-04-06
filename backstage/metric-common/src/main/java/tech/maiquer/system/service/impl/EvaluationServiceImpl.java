package tech.maiquer.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.LayuiQuery;
import tech.maiquer.common.model.LayuiQuery2;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.Evaluation;
import tech.maiquer.system.domain.Product;
import tech.maiquer.system.mapper.EvaluationMapper;
import tech.maiquer.system.service.ProductInfoService;
import tech.maiquer.system.service.EvaluationService;

import javax.annotation.Resource;

import java.util.List;

import static tech.maiquer.common.model.ResultCode.*;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Resource
    private EvaluationMapper evaluationMapper;

    @Resource
    private ProductInfoService productInfoService;

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public Result queryAll() {
        return Result.success(evaluationMapper.queryAll());
    }

    /**
     * 通过类型查询所有
     *
     * @return
     */
    @Override
    public Result queryAllByType(int type) {
        return Result.success(evaluationMapper.queryAllByType(type));
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Result queryById(Integer id) {
        Evaluation evaluation = evaluationMapper.queryById(id);
        if (evaluation == null) {
            return Result.failure(QUERY_FAIL);
        }
        return Result.success(evaluation);
    }

    /**
     * 分页查询
     *
     * @param current 当前页
     * @param size    分页大小
     * @return 查询结果
     */
    @Override
    public Paging<Evaluation> queryByPage(int current, int size) {
        Page<Evaluation> evaluationPage = PageHelper.startPage(current, size).doSelectPage(() -> evaluationMapper.queryAll());
        Paging<Evaluation> evaluationPaging = new Paging<>(evaluationPage);
        return evaluationPaging;
    }

    /**
     * 新增数据
     *
     * @param evaluation 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(Evaluation evaluation) {

        if (evaluationMapper.insert(evaluation) != 1) {
            return Result.failure(INSERT_FAIL);
        }
        // 同时也增加product
        Product product = new Product();
        product.setEvaId(evaluation.getId());
        productInfoService.insert(product);
        return Result.success(evaluation);

    }

    /**
     * 修改数据
     *
     * @param evaluation 实例对象
     * @return 实例对象
     */
    @Override
    public Result update(Evaluation evaluation) {
        Evaluation getEvaluation = evaluationMapper.queryById(evaluation.getId());
        if (getEvaluation == null) {
            return Result.failure(QUERY_FAIL);
        }
        if (evaluationMapper.update(evaluation) != 1) {
            return Result.failure(UPDATE_FAIL);
        }
        // 同时也更新product
        Product product = new Product();
        product.setEvaId(evaluation.getId());
        productInfoService.update(product);
        return Result.success(evaluation);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public Result deleteById(Integer id) {
        Evaluation getEvaluation = evaluationMapper.queryById(id);
        if (getEvaluation == null) {
            return Result.failure(QUERY_FAIL);
        }
        if (evaluationMapper.deleteById(id) != 1) {
            return Result.failure(DELETE_FAIL);
        }
        // 同时也删除product
        productInfoService.deleteByEvaIdInt(id);
        return Result.success();
    }

    /**
     * 批量删除
     * @param ids  主键
     * @return
     */
    @Override
    public Result deleteByIds(String ids) {
        int[] nums = this.processStr(ids);
        List<Evaluation> evaluations = evaluationMapper.queryByIds(nums);
        if(evaluations==null||evaluations.size()==0) {
            return Result.failure(QUERY_FAIL);
        }
        if(evaluationMapper.deleteByIds(nums)!=1) {
            return Result.failure(DELETE_FAIL);
        }
        return Result.success();
    }

    /**
     * 处理前端传的字符串解析成数组
     * @param ids
     * @return
     */
    private int[] processStr(String ids) {
        String[] arrays = ids.split(",");

        int[] evaIds = new int[arrays.length];

        for(int i=0;i<arrays.length;i++) {
            evaIds[i] = Integer.parseInt(arrays[i]);
        }
        return evaIds;
    }

    /**
     * layui里模糊查询的方法
     * @param param
     * @return
     */
    @Override
    public List<Evaluation> getEvaluationList(LayuiQuery2 param) {
        if(param.getName()!=null) {
            param.setName("%" + param.getName() + "%");
            return evaluationMapper.getEvaluationList(param);
        }
        return evaluationMapper.queryAll1(param);
    }

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    @Override
    public Long countEvaluationList(LayuiQuery2 param) {
        return evaluationMapper.countEvaluationList(param);
    }

}
