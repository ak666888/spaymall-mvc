package cn.paradox.dao;

import cn.paradox.domain.PO.PayOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryOrder(PayOrder order);

    void updateOrderPayInfo(PayOrder payOrder);
}
