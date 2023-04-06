package tech.maiquer.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.maiquer.common.config.property.WxPayProperty;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.wechat.OrderStatus;
import tech.maiquer.common.model.wechat.wxpay.WxApiType;
import tech.maiquer.common.model.wechat.wxpay.WxNotifyType;
import tech.maiquer.service.WxPayService;
import tech.maiquer.system.domain.Evaluation;
import tech.maiquer.system.domain.OrderInfo;
import tech.maiquer.system.domain.Product;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.EvaluationMapper;
import tech.maiquer.system.mapper.OrderInfoMapper;
import tech.maiquer.system.mapper.ProductMapper;
import tech.maiquer.system.mapper.SysUserMapper;
import tech.maiquer.system.service.OrderInfoService;
import tech.maiquer.system.service.PaymentInfoService;
import tech.maiquer.utils.JsApiPaySignUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private EvaluationMapper evaluationMapper;

    @Resource
    private WxPayProperty wxPayProperty;

    @Resource(name = "wxPayClient")
    private CloseableHttpClient httpClient;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private PaymentInfoService paymentInfoService;

    @Resource
    private JsApiPaySignUtils jsApiPaySignUtils;

    private boolean judgeIsPay(Integer evaId, Long userId) {

        List<Evaluation> evaluationList = evaluationMapper.queryMyEvaByUserId(userId);
        return evaluationList.contains(evaluationMapper.queryById(evaId));

    }


    // todo 请将同样功能的代码抽成一个函数

    private Map<String, String> putBodyAndPost(HttpPost httpPost, OrderInfo orderInfo, String openId, boolean isNative) {

        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        // 请求body参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("mchid", wxPayProperty.getMchId());
        paramsMap.put("out_trade_no", orderInfo.getOrderNo());
        paramsMap.put("appid", wxPayProperty.getAppId());
        paramsMap.put("description", orderInfo.getTitle());
        paramsMap.put("notify_url", wxPayProperty.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        if (!isNative) {
            // 组装payer
            Map<String, String> payer = new HashMap<>();
            payer.put("openid", openId);
            paramsMap.put("payer", payer);
        }
        // 组装amount
        Map<String, Object> amountMap = new HashMap<>();
        amountMap.put("total", orderInfo.getTotalFee());
        amountMap.put("currency", "CNY");
        paramsMap.put("amount", amountMap);

        //将参数转换成json字符串
        String jsonParams = JSON.toJSONString(paramsMap);
        log.info("请求参数 ===> {}" + jsonParams);

        // 配置请求体
        httpPost.setEntity(new StringEntity(jsonParams, "UTF-8"));

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("Native下单失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }
            //响应结果
            Map<String, String> resultMap = JSON.parseObject(bodyAsString, HashMap.class);
            return resultMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Result createOrderInfo(Integer evaId, Long userId) {
        // 先去数据库查找该用户的openId
        SysUser sysUser = null;
        try {
            sysUser = sysUserMapper.queryById(userId);
        } catch (Exception e) {
            log.error("在NATIVE支付过程中查找用户失败！");
            return Result.failure("在NATIVE支付过程中查找用户失败！");
        }
        if (sysUser == null) {
            return Result.failure("用户不存在！");
        }
        String openId = sysUser.getOpenid();

        Product product = null;
        try {
            product = productMapper.queryByEvaId(evaId);
        } catch (Exception e) {
            log.error("NATIVE支付过程中查询测评失败！");
            return Result.failure("NATIVE支付过程中查询测评失败！");
        }
        if (product == null) {
            return Result.failure("测评产品不存在！");
        }
        Long productId = product.getId();
        //生成订单
        OrderInfo orderInfo = orderInfoService.createOrderByProductId(productId, userId).getData();
        Map<String, Object> map = new HashMap<>();
        map.put("openId", openId);
        map.put("orderInfo", orderInfo);
        return Result.success(map);
    }

    /**
     * 创建订单，调用Native支付接口
     *
     * @param evaId
     * @return code_url 和 订单号
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result nativePay(Integer evaId, Long userId) throws Exception {

        log.info("生成订单");

        Map<String, String> map = new HashMap<>();
        Result result = this.createOrderInfo(evaId, userId);
        if (result.getCode() != 0) {
            return result;
        }

        Map res = (Map) result.getData();
        OrderInfo orderInfo = (OrderInfo) res.get("orderInfo");
        String codeUrl = orderInfo.getCodeUrl();

        if (codeUrl != null) {
            log.info("订单已存在，二维码已保存");
            //返回二维码
            map.put("codeUrl", codeUrl);
            map.put("orderNo", orderInfo.getOrderNo());
            return Result.success(map);
        }

        log.info("调用NATIVE下单API");
        String openId = (String) res.get("openId");
        //调用统一下单API
        HttpPost httpPost = new HttpPost(wxPayProperty.getDomain().concat(WxApiType.NATIVE_PAY.getType()));
        Map<String, String> resultMap = this.putBodyAndPost(httpPost, orderInfo, openId, true);

        if (resultMap != null) {
            //二维码
            codeUrl = resultMap.get("code_url");

            //保存二维码链接
            Long orderId = orderInfo.getId();
            orderInfoService.saveCodeUrl(orderId, codeUrl);

            //返回二维码
            map.put("codeUrl", codeUrl);
            map.put("orderNo", orderInfo.getOrderNo());
        }

        return Result.success(map);
    }

    /**
     * 创建订单，调用JSAPI支付接口
     *
     * @param evaId
     * @return prepay_id 和 订单号
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result jsApiPay(Integer evaId, Long userId) throws Exception {

        log.info("生成订单");
        Map<String, Object> map = new HashMap<>();

        // 先去数据库查找该用户的openId 若无OpenId 则需要绑定微信号
        SysUser sysUser = null;
        try {
            sysUser = sysUserMapper.queryById(userId);
        } catch (Exception e) {
            log.error("在JSAPI过程中查找用户失败！");
            return Result.failure("在JSAPI过程中查找用户失败！");
        }
        if (sysUser == null) {
            return Result.failure("用户不存在！");
        }
        String openId = sysUser.getOpenid();
        if (openId == null) {
            return Result.failure("用户未绑定微信号！");
        }

        Product product;
        try {
            product = productMapper.queryByEvaId(evaId);
        } catch (Exception e) {
            log.error("JSAPI支付过程中查询测评失败！");
            return Result.failure("JSAPI支付过程中查询测评失败！");
        }
        if (product == null) {
            return Result.failure("不存在该产品！");
        }
        Long productId = product.getId();

        //生成订单
        OrderInfo orderInfo = orderInfoService.createOrderByProductId(productId, userId).getData();

        // 查询是否已购买 不可重复支付
        if (judgeIsPay(evaId, userId)) {
            return Result.failure("该产品已购买，请勿重复支付！");
        }

        log.info("调用JSAPI下单API");

        //调用统一下单API
        HttpPost httpPost = new HttpPost(wxPayProperty.getDomain().concat(WxApiType.JSAPI_PAY.getType()));
        Map<String, String> resultMap = this.putBodyAndPost(httpPost, orderInfo, openId, false);

        if (resultMap != null) {
            // prepay订单号
            String prepayId = resultMap.get("prepay_id");

            //保存二维码链接
            Long orderId = orderInfo.getId();
            orderInfoService.savePrePayId(orderId, prepayId);

            String appId = wxPayProperty.getAppId();
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            String nonceStr = UUID.randomUUID().toString().substring(0, 8);
            String ppid = "prepay_id=" + prepayId;
            String signType = "RSA";
            String paySign = jsApiPaySignUtils.createSign(appId, timeStamp, nonceStr, prepayId);
            //返回prepayId
            map.put("appId", appId);
            map.put("timeStamp", timeStamp);
            map.put("nonceStr", nonceStr);
            map.put("package", ppid);
            map.put("signType", signType);
            map.put("orderNo", orderInfo.getOrderNo());
            map.put("paySign", paySign);

        }

        return Result.success(map);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processOrder(String plainText) throws GeneralSecurityException {

        log.info("处理订单");

        HashMap plainTextMap = JSON.parseObject(plainText, HashMap.class);
        String orderNo = (String) plainTextMap.get("out_trade_no");

        // TODO 上锁
        try {

            String orderStatus = orderInfoService.getOrderStatus(orderNo).getData();
            if (!OrderStatus.NOTPAY.getType().equals(orderStatus)) {
                return;
            }

            //更新订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);

            //记录支付日志
            paymentInfoService.createPaymentInfo(plainText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String cancelOrder(String orderNo) throws Exception {

        OrderInfo orderInfo = orderInfoMapper.queryByOrderNoOrderInfo(orderNo);
        if (orderInfo == null) {
            return "订单不存在！";
        }

        //调用微信支付的关单接口
        HashMap<String, String> res = this.closeOrder(orderNo);

        if (res.get("code").equals("SUCCESS")) {
            //更新商户端的订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);
        }

        return res.get("message");


    }

    /**
     * 关单接口的调用
     * <p>
     * API字典： https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_3.shtml
     *
     * @param orderNo
     */
    private HashMap<String, String> closeOrder(String orderNo) throws Exception {

        log.info("关单接口的调用，订单号 ===> {}", orderNo);

        //创建远程请求对象
        String url = String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(), orderNo);
        url = wxPayProperty.getDomain().concat(url);
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        // 请求body参数
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mchid", wxPayProperty.getMchId());
        String jsonParams = JSON.toJSONString(paramsMap);
        log.info("请求参数 ===> {}", jsonParams);

        StringEntity entity = new StringEntity(jsonParams, "UTF-8");
        entity.setContentType("application/json");

        //将请求参数设置到请求对象中
        httpPost.setEntity(entity);

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        HashMap<String, String> res = new HashMap<>();

        try {
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 204) {
                res.put("code", "SUCCESS");
                res.put("message", "该订单已成功关闭");
                return res;
            }

            String bodyAsString = EntityUtils.toString(response.getEntity());
            res = JSON.parseObject(bodyAsString, HashMap.class);
            return res;
        } catch (IOException | ParseException e) {
            res.put("code", "ERROR");
            if (e.toString() != null && !e.toString().equals("")) {
                res.put("message", e.toString());
            } else {
                res.put("message", "发生未知错误");
            }
            return res;
        }
    }

    /**
     * 可通过“微信支付订单号查询”和“商户订单号查询”两种方式查询订单详情
     * <p>
     * 这里通过后者进行查询
     * <p>
     * API字典： https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_2.shtml
     *
     * @param orderNo
     * @return
     * @throws Exception
     */
    @Override
    public String queryOrder(String orderNo) throws Exception {

        OrderInfo orderInfo = orderInfoMapper.queryByOrderNoOrderInfo(orderNo);
        if (orderInfo == null) {
            log.error(String.format("订单号%s不存在", orderNo));
            return JSON.toJSONString(Result.failure("订单不存在！"));
        }

        log.info("查单接口调用 ===> {}", orderNo);

        //拼接请求的第三方API
        String url = String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderNo);
        url = wxPayProperty.getDomain().concat(url).concat("?mchid=").concat(wxPayProperty.getMchId());

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("查单接口调用,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }

            return bodyAsString;

        } finally {
            response.close();
        }

    }

}

