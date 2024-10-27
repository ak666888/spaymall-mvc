package cn.paradox.controller;

import cn.paradox.common.Contants.Constants;
import cn.paradox.common.responce.Response;
import cn.paradox.controller.dto.CreatePayRequestDTO;
import cn.paradox.domain.Req.ShopCartReq;
import cn.paradox.domain.Res.PayOrderRes;
import cn.paradox.service.IOrderService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.scanner.Constant;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/alipay/")
public class AliPayController {
    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    @Resource
    private IOrderService orderService;


//    http://117.72.38.149:8083/alipay/create_pay_order
    @PostMapping(value = "create_pay_order")
    public Response<String> createPayOrder(@RequestBody CreatePayRequestDTO createPayRequestDTO) {
        try{
            String userId = createPayRequestDTO.getUserId();
            String productId = createPayRequestDTO.getProductId();
            log.info("商品下单，根据商品id创建支付单开始 userId {} productId{}", userId, productId);
            //1.下单
            PayOrderRes payOrderRes = orderService.createOrder(ShopCartReq.builder().productId(productId).userId(userId).build());

            log.info("商品下单，根据商品id创建支付单完成 userId {} productId{}", userId, productId);

            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(payOrderRes.getPayUrl())
                    .build();

        }catch (Exception e){
            log.error("商品下单，根据商品id创建支付单失败 userId {} productId{}", createPayRequestDTO.getUserId(), createPayRequestDTO.getProductId());
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @PostMapping(value = "alipay_notify_url")
    public String payNotify(HttpServletRequest request) throws AlipayApiException {
        log.info("支付回调，消息接收{}", request.getParameter("trade_status"));

        if(!request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            return "false";
        }

        Map<String ,String> params = new HashMap<String ,String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for( String name : requestParams.keySet() ){
            params.put(name, request.getParameter(name));
        }

        String tradeNo = request.getParameter("out_trade_no");
        String gmtPayment = params.get("gmt_payment");
        String alipayTradeNo = params.get("trade_no");

        String sign = params.get("sign");
        String content = AlipaySignature.getSignCheckContentV1(params);
        boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8");

        //支付宝验签
        if(!checkSignature){
            return "false";
        }

        // 验签通过
        log.info("支付回调，交易名称: {}", params.get("subject"));
        log.info("支付回调，交易状态: {}", params.get("trade_status"));
        log.info("支付回调，支付宝交易凭证号: {}", params.get("trade_no"));
        log.info("支付回调，商户订单号: {}", params.get("out_trade_no"));
        log.info("支付回调，交易金额: {}", params.get("total_amount"));
        log.info("支付回调，买家在支付宝唯一id: {}", params.get("buyer_id"));
        log.info("支付回调，买家付款时间: {}", params.get("gmt_payment"));
        log.info("支付回调，买家付款金额: {}", params.get("buyer_pay_amount"));
        log.info("支付回调，支付回调，更新订单 {}", tradeNo);

        return "success";
    }
}
