package tech.maiquer.system.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.maiquer.common.model.LayuiQuery4;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.wechat.PayType;
import tech.maiquer.system.mapper.PaymentInfoMapper;
import tech.maiquer.system.service.PaymentInfoService;
import tech.maiquer.system.domain.PaymentInfo;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Resource
    private PaymentInfoMapper paymentInfoMapper;

    /**
     * 记录支付日志
     *
     * @param plainText
     */
    @Override
    public void createPaymentInfo(String plainText) {

        log.info("记录支付日志");

        HashMap plainTextMap = JSON.parseObject(plainText, HashMap.class);

        //订单号
        String orderNo = (String) plainTextMap.get("out_trade_no");
        //业务编号
        String transactionId = (String) plainTextMap.get("transaction_id");
        //支付类型
        String tradeType = (String) plainTextMap.get("trade_type");
        //交易状态
        String tradeState = (String) plainTextMap.get("trade_state");
        //用户实际支付金额
        Map<String, Object> amount = (Map) plainTextMap.get("amount");

        //TODO 可以修改单位分为元 但涉及到重新建表 修改DAO层
        Integer payerTotal = (Integer) amount.get("payer_total");

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentType(PayType.WXPAY.getType());
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setTradeType(tradeType);
        paymentInfo.setTradeState(tradeState);
        paymentInfo.setPayerTotal(payerTotal);
        paymentInfo.setContent(plainText);

        paymentInfoMapper.insert(paymentInfo);
    }

    @Override
    public Result queryAll() {
        log.info("查询所有支付信息");
        List<PaymentInfo> paymentInfos = paymentInfoMapper.queryAll();
        return Result.success(paymentInfos);
    }

    @Override
    public Result deleteById(Long id) {
        paymentInfoMapper.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * layui里模糊查询的方式
     * @param param
     * @return
     */
    @Override
    public List<PaymentInfo> getPaymentInfoList(LayuiQuery4 param) {
        if(param.getOrderNo()!=null) {
            param.setOrderNo("%" + param.getOrderNo() + "%");
            return paymentInfoMapper.getPaymentInfoList(param);
        }
        return paymentInfoMapper.queryAll1(param);
    }

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    @Override
    public Long countPaymentInfoList(LayuiQuery4 param) {
        return paymentInfoMapper.countPaymentList(param);
    }

}
