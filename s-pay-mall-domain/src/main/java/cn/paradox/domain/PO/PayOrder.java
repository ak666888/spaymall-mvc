package cn.paradox.domain.PO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单持久化对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrder {

    /**
     * id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 总数
     */
    private BigDecimal totalAmount;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 支付链接
     */
    private String payUrl;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


}
