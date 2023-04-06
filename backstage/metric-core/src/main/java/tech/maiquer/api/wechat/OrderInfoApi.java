package tech.maiquer.api.wechat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.maiquer.common.model.LayuiQuery5;
import tech.maiquer.common.model.LayuiResult;
import tech.maiquer.common.model.Result;
import tech.maiquer.common.model.wechat.OrderStatus;
import tech.maiquer.system.domain.OrderInfo;
import tech.maiquer.system.service.OrderInfoService;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "订单管理")
@RestController
@CrossOrigin
@RequestMapping("/api/order")
public class OrderInfoApi {

    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 根据订单号更新订单状态
     *
     * @param orderNo
     * @param orderStatus
     * @return Result
     */
    @PostMapping("/updateOrderStatus/{orderNo}")
    @PreAuthorize("hasPermission('/api/order/updateOrderStatus','wechat:order:updateOrderStatus')")
    @ApiOperation(value = "更新", notes = "注意正确传参")
    public Result updateOrderStatus(@PathVariable String orderNo, @RequestBody OrderStatus orderStatus) {
        return orderInfoService.updateStatusByOrderNo(orderNo, orderStatus);
    }

    /**
     * 根据订单号获取订单状态
     *
     * @param orderNo 订单号
     * @return Result
     */
    @GetMapping("/getOrderStatus/{orderNo}")
    @PreAuthorize("hasPermission('/api/order/getOrderStatus','wechat:order:getOrderStatus')")
    @ApiOperation(value = "订单状态查询", notes = "注意传参格式")
    public Result getOrderStatus(@ApiParam(name = "orderNo", value = "订单号", required = true) @PathVariable("orderNo") String orderNo) {
        return orderInfoService.getOrderStatus(orderNo);
    }

    @GetMapping("/queryAll")
    @PreAuthorize("hasPermission('/api/order/queryAll','wechat:order:queryAll')")
    @ApiOperation(value = "查询所有")
    public Result queryAll() {
        return orderInfoService.queryAll();
    }

    @DeleteMapping("/deleteOrder/{id}")
    @PreAuthorize("hasPermission('/api/order/deleteOrder','wechat:order:deleteOrder')")
    @ApiOperation(value = "删除", notes = "注意正确传参")
    public Result delete(@ApiParam(name = "id", value = "订单编号ID", required = true) @PathVariable("id") Long id) {
        return orderInfoService.deleteById(id);
    }

    @PostMapping("/queryPersonal/{userId}")
    @PreAuthorize("hasPermission('/api/order/queryPersonal','wechat:order:queryPersonal')")
    @ApiOperation(value = "查询个人订单历史", notes = "注意正确传参")
    public Result queryByUserId(@ApiParam(name = "userId", value = "用户ID", required = true) @PathVariable("userId") Long userId) {
        return orderInfoService.queryByUserId(userId);
    }


    @GetMapping("/layui/list")
    public LayuiResult<Object> getOrderInfoList(LayuiQuery5 param) {
        List<OrderInfo> list = orderInfoService.getOrderInfoList(param);
        Long count = orderInfoService.countOrderInfoList(param);
        return LayuiResult.success(list,count);
    }

}
