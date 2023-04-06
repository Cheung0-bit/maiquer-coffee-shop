package tech.maiquer.utils;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.*;
import java.util.Base64;

@Component
public class JsApiPaySignUtils {

    @Resource
    private PrivateKey privateKey;

    public String createSign(String appId, String timeStamp, String nonceStr, String prepayId) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature s = Signature.getInstance("SHA256withRSA");
        s.initSign(privateKey);
        String message = appId + "\n"
                + timeStamp + "\n"
                + nonceStr + "\n"
                + "prepay_id=" + prepayId + "\n";
        s.update(message.getBytes());
        byte[] sign = s.sign();
        return Base64.getEncoder().encodeToString(sign);

    }


}

