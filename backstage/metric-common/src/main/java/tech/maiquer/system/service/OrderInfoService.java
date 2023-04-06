package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.LayuiQuery5;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.wechat.OrderStatus;
import tech.maiquer.system.domain.OrderInfo;
import tech.maiquer.system.domain.PaymentInfo;

import java.util.List;

@Service
public interface OrderInfoService {

    Result<OrderInfo> createOrderByProductId(Long productId, Long userId);

    Result saveCodeUrl(Long orderNo, String codeUrl);

    Result savePrePayId(Long orderNo, String prepayId);

    Result<String> getOrderStatus(String orderNo);

    Result updateStatusByOrderNo(String orderNo, OrderStatus success);

    Result queryAll();

    Result queryByUserId(Long userId);

    Result deleteById(Long id);

    /**
     * layui里模糊查询方法
     * @param param
     * @return
     */
    List<OrderInfo> getOrderInfoList(LayuiQuery5 param);

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    Long countOrderInfoList(LayuiQuery5 param);
}
