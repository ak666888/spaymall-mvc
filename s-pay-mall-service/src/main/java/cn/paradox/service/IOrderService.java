package cn.paradox.service;

import cn.paradox.domain.Req.ShopCartReq;
import cn.paradox.domain.Res.PayOrderRes;

/**
 * 订单服务接口
 */
public interface IOrderService {

    PayOrderRes createOrder(ShopCartReq shopCartReq);

}
