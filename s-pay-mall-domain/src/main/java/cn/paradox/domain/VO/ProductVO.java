package cn.paradox.domain.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {
    /**
     * 商品id
     */
    private String productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品描述
     */
    private String productDesc;
    /**
     * 商品价格
     */
    private BigDecimal price;
}
