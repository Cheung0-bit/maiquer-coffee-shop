package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.LayuiQuery2;
import tech.maiquer.common.model.Paging;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.Evaluation;

import java.util.List;

@Service
public interface EvaluationService {

    /**
     * 查询所有
     *
     * @return
     */
    Result queryAll();

    /**
     * 通过类型查询所有
     *
     * @return
     */
    Result queryAllByType(int type);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Result queryById(Integer id);

    /**
     * 分页查询
     *
     * @param current 当前页
     * @param size    分页大小
     * @return 查询结果
     */
    Paging<Evaluation> queryByPage(int current, int size);

    /**
     * 新增数据
     *
     * @param evaluation 实例对象
     * @return 实例对象
     */
    Result insert(Evaluation evaluation);

    /**
     * 修改数据
     *
     * @param evaluation 实例对象
     * @return 实例对象
     */
    Result update(Evaluation evaluation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    Result deleteById(Integer id);


    /**
     * 批量删除
     * @param ids
     * @return
     */
    Result deleteByIds(String ids);

    /**
     * layui里模糊查询的方法
     * @param param
     * @return
     */
    List<Evaluation> getEvaluationList(LayuiQuery2 param);

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    Long countEvaluationList(LayuiQuery2 param);
}
