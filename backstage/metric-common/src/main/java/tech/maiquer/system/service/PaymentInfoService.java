package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.LayuiQuery4;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.PaymentInfo;

import java.util.List;

@Service
public interface PaymentInfoService {

    void createPaymentInfo(String plainText);

    Result queryAll();

    Result deleteById(Long id);

    /**
     * layui里模糊查询方法
     * @param param
     * @return
     */
    List<PaymentInfo> getPaymentInfoList(LayuiQuery4 param);

    /**
     * layui里统计返回数据count
     * @param param
     * @return
     */
    Long countPaymentInfoList(LayuiQuery4 param);


}
