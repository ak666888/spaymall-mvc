package cn.paradox.job;

import cn.paradox.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class TimeoutCloseOrderJob {

    @Resource
    private IOrderService orderService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void exec(){
        try{
            log.info("任务：超时30分钟订单关闭");
            List<String> orderIds = orderService.queryTimeoutCloseOrderList();
            if(null == orderIds || orderIds.isEmpty()){
                return;
            }

            for(String orderId : orderIds){
                boolean status = orderService.changeOrderClose(orderId);
                log.info("定时任务，超时30分钟订单关闭 orderId:{}, status:{}", orderId, status);
            }
        } catch (Exception e){
            log.error("定时任务：超时30分钟订单关闭失败");
        }
    }
}
