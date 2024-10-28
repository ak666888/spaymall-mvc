package cn.paradox.job;

import cn.paradox.service.IOrderService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class NoPayNotifyOrderJob {

    @Resource
    private IOrderService orderService;

    @Resource
    private AlipayClient alipayClient;

    @Scheduled(cron = "0/3 * * * * ?")
    public void exec(){
        try{
            log.info("任务：检测未收到或未正确的支付回调通知");
            List<String> orderIds = orderService.queryNoPayNotifyOrder();
            if(null == orderIds || orderIds.isEmpty()) return;

            for(String orderId : orderIds){
                AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
                AlipayTradePagePayModel model = new AlipayTradePagePayModel();
                model.setOutTradeNo(orderId);
                alipayRequest.setBizModel(model);

                AlipayTradePagePayResponse alipayTradeQueryResponse = alipayClient.execute(alipayRequest);

                String code = alipayTradeQueryResponse.getCode();

                if (code.equals("10000")){
                    orderService.changeOrderPaySuccess(orderId);
                }
            }

        } catch (AlipayApiException e) {
            log.error("检测未接收或未正确处理的支付回调消息失败", e);
        }
    }
}
