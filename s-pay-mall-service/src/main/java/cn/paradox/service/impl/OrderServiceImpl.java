package cn.paradox.service.impl;

import cn.paradox.common.Contants.Constants;
import cn.paradox.dao.IOrderDao;

import cn.paradox.domain.PO.PayOrder;
import cn.paradox.domain.Req.ShopCartReq;
import cn.paradox.domain.Res.PayOrderRes;
import cn.paradox.domain.VO.ProductVO;
import cn.paradox.service.IOrderService;
import cn.paradox.service.rpc.ProductRPC;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 订单服务
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Value("${alipay.notify_url}")
    private String notifyUrl;
    @Value("${alipay.return_url}")
    private String returnUrl;


    @Resource
    private ProductRPC productRPC;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private AlipayClient alipayClient;
    @Resource
    private EventBus eventBus;


    @Override
    public PayOrderRes createOrder(ShopCartReq shopCartReq) throws AlipayApiException {
        log.info("订单创建服务 userId{} productId {}", shopCartReq.getUserId(), shopCartReq.getProductId());


        //1. 首先查询是否存在未支付订单
        PayOrder UnpayOrder = orderDao.queryOrder(PayOrder.builder()
                .userId(shopCartReq.getUserId())
                .productId(shopCartReq.getProductId())
                .build());
        if(UnpayOrder!=null){
            if(UnpayOrder.getStatus().equals(Constants.OrderStatusEnum.PAY_WAIT.getCode())){
                log.info("订单创建服务 存在未支付订单 userId {} orderId {}", UnpayOrder.getUserId(), UnpayOrder.getOrderId());
                return PayOrderRes.builder()
                        .userId(UnpayOrder.getUserId())
                        .orderId(UnpayOrder.getOrderId())
                        .payUrl(UnpayOrder.getPayUrl())
                        .build();
            }else if(UnpayOrder.getStatus().equals(Constants.OrderStatusEnum.CREATE.getCode())){
                log.info("订单创建服务 存在未创建支付单订单 支付单创建开始 userId {} orderId {} ", UnpayOrder.getUserId(), UnpayOrder.getOrderId());
                PayOrder payOrder = this.doPrepayOrder(UnpayOrder.getProductId(), UnpayOrder.getProductName(), UnpayOrder.getOrderId(), UnpayOrder.getTotalAmount());
                return PayOrderRes.builder()
                        .orderId(payOrder.getOrderId())
                        .payUrl(payOrder.getPayUrl())
                        .build();
            }

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
        PayOrder payOrder = doPrepayOrder(productVO.getProductId(), productVO.getProductName(), orderId, productVO.getPrice());
        return PayOrderRes.builder()
                .orderId(orderId)
                .payUrl(payOrder.getPayUrl())
                .build();
    }

    public void changeOrderPaySuccess(String orderId){
        log.info("支付回调，更新数据库订单状态");
        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setOrderId(orderId);
        payOrderReq.setStatus(Constants.OrderStatusEnum.PAY_SUCCESS.getCode());
        orderDao.changeOrderPaySuccess(payOrderReq);

        //发送MQ消息
        eventBus.post(JSON.toJSONString(payOrderReq));
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return orderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderDao.queryNoPayNotifyOrder();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return orderDao.changeOrderClose(orderId);
    }


    private PayOrder doPrepayOrder(String productId, String productName, String orderId, BigDecimal totalAmount) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", productId);
        bizContent.put("total_amount", totalAmount);
        bizContent.put("subject", productName);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();

        PayOrder payOrder = PayOrder.builder()
                .orderId(orderId)
                .payUrl(form)
                .status(Constants.OrderStatusEnum.PAY_WAIT.getCode())
                .build();

        orderDao.updateOrderPayInfo(payOrder);

        return payOrder;
    }


}
