package cn.paradox.test;

import cn.paradox.domain.Req.ShopCartReq;
import cn.paradox.domain.Res.PayOrderRes;
import cn.paradox.service.IOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {


    @Resource
    private IOrderService orderService;

    @Test
    public void test_createOrder(){
        ShopCartReq shopCartReq = ShopCartReq.builder()
                .userId("paradox")
                .productId("10010")
                .build();
        PayOrderRes payOrderRes = orderService.createOrder(shopCartReq);
        log.info("请求参数：{}", JSON.toJSONString(shopCartReq));
        log.info("测试结果：{}", JSON.toJSONString(payOrderRes));
    }
}
