package tech.maiquer.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import tech.maiquer.common.model.LayuiQuery4;
import tech.maiquer.system.domain.PaymentInfo;

import java.util.List;

@Mapper
public interface PaymentInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PaymentInfo queryById(Long id);

    /**
     * 新增数据
     *
     * @param paymentInfo 实例对象
     * @return 影响行数
     */
    int insert(PaymentInfo paymentInfo);

    /**
     * 修改数据
     *
     * @param paymentInfo 实例对象
     * @return 影响行数
     */
    int update(PaymentInfo paymentInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 查询所有
     *
     * @return
     */
    List<PaymentInfo> queryAll();

    /**
     * layui里面模糊查询方法--查订单
     * @param param
     * @return
     */
    List<PaymentInfo> getPaymentInfoList(LayuiQuery4 param);

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    Long countPaymentList(LayuiQuery4 param);

    /**
     * 根据商品名进行模糊查询
     * @param param
     * @return
     */
    List<PaymentInfo> queryAll1(LayuiQuery4 param);

}
