package cn.paradox.service.impl;

import cn.paradox.common.Contants.Constants;
import cn.paradox.dao.IOrderDao;

import cn.paradox.domain.PO.PayOrder;
import cn.paradox.domain.Req.ShopCartReq;
import cn.paradox.domain.Res.PayOrderRes;
import cn.paradox.domain.VO.ProductVO;
import cn.paradox.service.IOrderService;
import cn.paradox.service.rpc.ProductRPC;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 订单服务
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    private ProductRPC productRPC;
    @Resource
    private IOrderDao orderDao;

    @Override
    public PayOrderRes createOrder(ShopCartReq shopCartReq) {
        log.info("订单创建服务 userId{} productId {}", shopCartReq.getUserId(), shopCartReq.getProductId());


        //1. 首先查询是否存在未支付订单
        PayOrder UnpayOrder = orderDao.queryOrder(PayOrder.builder()
                .userId(shopCartReq.getUserId())
                .productId(shopCartReq.getProductId())
                .build());
        if(UnpayOrder!=null){
            log.info("订单创建服务 存在未支付订单 userId {} orderId {}", UnpayOrder.getUserId(), UnpayOrder.getOrderId());
            return PayOrderRes.builder()
                    .userId(UnpayOrder.getUserId())
                    .orderId(UnpayOrder.getOrderId())
                    .payUrl(UnpayOrder.getPayUrl())
                    .build();
        }

        //2.商品查询创建订单
        ProductVO productVO = productRPC.queryProductByProductId(shopCartReq.getProductId());
        log.info("订单创建服务 无未支付订单 创建订单 userId {}", shopCartReq.getUserId());
        String orderId = RandomStringUtils.randomNumeric(16);
        PayOrder newOrder = PayOrder.builder()
                .userId(shopCartReq.getUserId())
                .productId(shopCartReq.getProductId())
                .productName(productVO.getProductName())
                .orderId(orderId)
                .orderTime(new Date())
                .totalAmount(productVO.getPrice())
                .status(Constants.OrderStatusEnum.CREATE.getCode())
                .build();
        orderDao.insert(newOrder);

        //3.创建支付单

        return PayOrderRes.builder()
                .orderId(orderId)
                .payUrl("无")
                .build();
    }
}
