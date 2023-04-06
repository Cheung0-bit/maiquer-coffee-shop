package tech.maiquer.system.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

@EqualsAndHashCode(callSuper = true)
@Data
@Alias("Product")
public class Product extends BaseEntity {

    private Long id; // 产品编号

    private int evaId; // 测评id

    private String title; //商品名称

    private int price; //价格（分）

}
