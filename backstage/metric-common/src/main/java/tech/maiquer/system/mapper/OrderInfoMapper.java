package tech.maiquer.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import tech.maiquer.common.model.LayuiQuery5;
import tech.maiquer.system.domain.OrderInfo;

import java.util.List;

@Mapper
public interface OrderInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OrderInfo queryById(Long id);

    /**
     * 痛通过订单号查询
     *
     * @param orderNo
     * @return
     */
    OrderInfo queryByOrderNoOrderInfo(String orderNo);

    /**
     * 新增数据
     *
     * @param orderInfo 实例对象
     * @return 影响行数
     */
    int insert(OrderInfo orderInfo);

    /**
     * 修改数据
     *
     * @param orderInfo 实例对象
     * @return 影响行数
     */
    int update(OrderInfo orderInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 通过用户ID查询订单
     *
     * @param userId
     * @return
     */
    List<OrderInfo> queryByUserId(Long userId);

    /**
     * 查询所有
     *
     * @return
     */
    List<OrderInfo> queryAll();


    /**
     * layui里面模糊查询方式--查商品订单
     * @param param
     * @return
     */
    List<OrderInfo> getOrderInfoList(LayuiQuery5 param);

    /**
     * layui里面统计返回数据count
     * @param param
     * @return
     */
    Long countOrderInfoList(LayuiQuery5 param);


    /**
     * 根据商户订单编号进行模糊查询
     * @param param
     * @return
     */
    List<OrderInfo> queryAll1(LayuiQuery5 param);


}
