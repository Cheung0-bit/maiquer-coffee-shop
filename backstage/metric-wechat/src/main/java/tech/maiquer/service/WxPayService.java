package tech.maiquer.service;

import org.springframework.stereotype.Service;
import tech.maiquer.common.model.Result;

import java.security.GeneralSecurityException;

@Service
public interface WxPayService {

    Result nativePay(Integer evaId, Long userId) throws Exception;

    Result jsApiPay(Integer evaId,Long userId) throws Exception;

    void processOrder(String plainText) throws GeneralSecurityException;

    String cancelOrder(String orderNo) throws Exception;

    String queryOrder(String orderNo) throws Exception;
}
