package tech.maiquer.system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.LayuiQuery5;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.ResultCode;
import tech.maiquer.common.model.wechat.OrderStatus;
import tech.maiquer.common.utils.OrderNoUtils;
import tech.maiquer.system.mapper.OrderInfoMapper;
import tech.maiquer.system.service.OrderInfoService;
import tech.maiquer.system.domain.OrderInfo;
import tech.maiquer.system.domain.Product;
import tech.maiquer.system.mapper.ProductMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Override
    public Result createOrderByProductId(Long productId, Long userId) {


        //获取商品信息
        Product product = null;

        product = productMapper.queryById(productId);

        if (product == null) {
            return Result.failure(ResultCode.QUERY_FAIL);
        }

        //生成订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setTitle(product.getTitle());
        orderInfo.setOrderNo(OrderNoUtils.getOrderNo()); //订单号
        orderInfo.setProductId(productId);
        orderInfo.setTotalFee(product.getPrice()); //分
        orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());
        orderInfoMapper.insert(orderInfo);

        return Result.success(orderInfo);
    }

    /**
     * 存储订单二维码
     *
     * @param orderId
     * @param codeUrl
     */
    @Override
    public Result saveCodeUrl(Long orderId, String codeUrl) {

        OrderInfo orderInfo = orderInfoMapper.queryById(orderId);
        orderInfo.setCodeUrl(codeUrl);
        orderInfoMapper.update(orderInfo);

        return Result.success("存储订单二维码成功");
    }

    @Override
    public Result savePrePayId(Long orderId, String prepayId) {

        OrderInfo orderInfo = orderInfoMapper.queryById(orderId);
        orderInfo.setPrepayId(prepayId);
        orderInfoMapper.update(orderInfo);

        return Result.success("存储预订单号成功");
    }

    /**
     * 根据订单号获取订单状态
     *
     * @param orderNo
     * @return
     */
    @Override
    public Result getOrderStatus(String orderNo) {

        OrderInfo orderInfo = orderInfoMapper.queryByOrderNoOrderInfo(orderNo);
        if (orderInfo == null) {
            return null;
        }
        return Result.success(orderInfo.getOrderStatus());
    }

    /**
     * 根据订单号更新订单状态
     *
     * @param orderNo
     * @param orderStatus
     */
    @Override
    public Result updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {

        log.info("更新订单状态 ===> {}", orderStatus.getType());
        OrderInfo orderInfo = orderInfoMapper.queryByOrderNoOrderInfo(orderNo);
        orderInfo.setOrderStatus(orderStatus.getType());
        orderInfoMapper.update(orderInfo);

        return Result.success("更新订单状态成功");
    }

    @Override
    public Result queryAll() {
        log.info("查询所有订单");
        List<OrderInfo> orderInfos = orderInfoMapper.queryAll();
        return Result.success(orderInfos);
    }

    @Override
    public Result queryByUserId(Long userId) {
        List<OrderInfo> orderInfos = orderInfoMapper.queryByUserId(userId);
        return Result.success(orderInfos);
    }

    @Override
    public Result deleteById(Long id) {
        orderInfoMapper.deleteById(id);
        return Result.success("删除订单成功");
    }

    /**
     * layui里模糊查询的方式
     * @param param
     * @return
     */
    @Override
    public List<OrderInfo> getOrderInfoList(LayuiQuery5 param) {
        if(param.getOrderNo()!=null) {
            param.setOrderNo("%" + param.getOrderNo() + "%");
            return orderInfoMapper.getOrderInfoList(param);
        }
        return orderInfoMapper.queryAll1(param);
    }

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    @Override
    public Long countOrderInfoList(LayuiQuery5 param) {
        return orderInfoMapper.countOrderInfoList(param);
    }

}
