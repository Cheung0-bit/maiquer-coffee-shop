package tech.maiquer.api.wechat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.model.LayuiQuery4;
import tech.maiquer.common.model.LayuiResult;
import tech.maiquer.common.model.Result;
import tech.maiquer.system.domain.PaymentInfo;
import tech.maiquer.system.service.PaymentInfoService;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "支付管理")
@RestController
@RequestMapping("/api/payment")
public class PaymentApi {

    @Resource
    private PaymentInfoService paymentInfoService;

    @GetMapping("/queryAll")
    @PreAuthorize("hasPermission('/api/payment/queryAll','wechat:payment:queryAll')")
    @ApiOperation(value = "查询所有")
    public Result queryAll() {
        return paymentInfoService.queryAll();
    }

    @DeleteMapping("/deletePayment/{id}")
    @PreAuthorize("hasPermission('/api/payment/deletePayment','wechat:payment:deletePayment')")
    @ApiOperation(value = "删除", notes = "注意正确传参")
    public Result delete(@ApiParam(name = "id", value = "支付编号ID", required = true) @PathVariable("id") Long id) {
        return paymentInfoService.deleteById(id);
    }


    @GetMapping("/layui/list")
    public LayuiResult<Object> getPaymentInfoList(LayuiQuery4 param) {
        List<PaymentInfo> list = paymentInfoService.getPaymentInfoList(param);
        Long count = paymentInfoService.countPaymentInfoList(param);
        return LayuiResult.success(list,count);
    }


}
