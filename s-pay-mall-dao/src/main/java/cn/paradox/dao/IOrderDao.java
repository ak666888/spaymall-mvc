package cn.paradox.dao;

import cn.paradox.domain.PO.PayOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryOrder(PayOrder order);

    void updateOrderPayInfo(PayOrder payOrder);

    void changeOrderPaySuccess(PayOrder payOrder);

    List<String> queryTimeoutCloseOrderList();

    List<String> queryNoPayNotifyOrder();

    boolean changeOrderClose(String orderId);


}
