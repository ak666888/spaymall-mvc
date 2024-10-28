package cn.paradox.service;

import cn.paradox.domain.Req.ShopCartReq;
import cn.paradox.domain.Res.PayOrderRes;
import com.alipay.api.AlipayApiException;

import java.util.List;

/**
 * 订单服务接口
 */
public interface IOrderService {

    PayOrderRes createOrder(ShopCartReq shopCartReq) throws AlipayApiException;

    void changeOrderPaySuccess(String orderId);

    List<String> queryTimeoutCloseOrderList();

    List<String> queryNoPayNotifyOrder();

    boolean changeOrderClose(String orderId);
}
