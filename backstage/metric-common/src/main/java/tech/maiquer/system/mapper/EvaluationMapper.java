package tech.maiquer.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import tech.maiquer.common.model.LayuiQuery2;
import tech.maiquer.system.domain.Evaluation;

import java.util.List;

@Mapper
public interface EvaluationMapper {


    /**
     * 获取全部测评
     *
     * @return List
     */
    List<Evaluation> queryAll();

    /**
     * 通过类型获取全部测评
     *
     * @return List
     */
    List<Evaluation> queryAllByType(int type);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Evaluation queryById(Integer id);

    /**
     * 批量查询测评
     * @param evaIds
     * @return
     */
    List<Evaluation> queryByIds(int[] evaIds);

    /**
     * 通过USERID查MyEva
     *
     * @param id
     * @return List
     */
    List<Evaluation> queryMyEvaByUserId(Long id);

    /**
     * 通过USERID查MyEva
     *
     * @param id
     * @return List
     */
    List<Evaluation> queryLikeEvaByUserId(Long id);

    /**
     * 通过USERID查MyEva
     *
     * @param id
     * @return List
     */
    List<Evaluation> queryGiftEvaByUserId(Long id);

    /**
     * 新增数据
     *
     * @param evaluation 实例对象
     * @return 影响行数
     */
    int insert(Evaluation evaluation);

    /**
     * 修改数据
     *
     * @param evaluation 实例对象
     * @return 影响行数
     */
    int update(Evaluation evaluation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 批量删除角色
     * @param evaIds
     * @return
     */
    int deleteByIds(int[] evaIds);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    int addMyEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    int deleteMyEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    int addLikeEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    int deleteLikeEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    int addGiftEva(Long userId, Integer evaId);

    /**
     * @param userId
     * @param evaId
     * @return
     */
    int deleteGiftEva(Long userId, Integer evaId);

    /**
     * layui里面模糊查询方法--查测评
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

    /**
     * 根据测聘名称进行模糊查询
     * @param param
     * @return
     */
    List<Evaluation> queryAll1(LayuiQuery2 param);

}
