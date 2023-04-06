package tech.maiquer.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "Evaluation", description = "测评类")
@Alias("Evaluation")
public class Evaluation extends BaseEntity implements Serializable {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号", name = "id")
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "测评名称", name = "name", example = "恋爱人格")
    private String name;

    /**
     * 测评分类
     */
    @ApiModelProperty(value = "测评分类", name = "type", example = "1")
    private int type;

    /**
     * 目标地址
     */
    @ApiModelProperty(value = "目标地址", name = "realUrl", example = "https://jinshuju.net/f/tJduAB")
    private String realUrl = "https://jinshuju.net/f/tJduAB" ;

    /**
     * 预览图
     */
    @ApiModelProperty(value = "封面图", name = "coverPic", example = "https://images.maiquer.tech/images/bad6364a.jpg")
    private String coverPic = "https://images.maiquer.tech/images/bad6364a.jpg" ;

    /**
     * 背景图
     */
    @ApiModelProperty(value = "背景图", name = "bgPic", example = "https://images.maiquer.tech/images/bad6364a.jpg")
    private String bgPic = "https://images.maiquer.tech/images/bad6364a.jpg" ;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格", name = "price", example = "5.00")
    private BigDecimal price = BigDecimal.valueOf(0.01);

    /**
     * 点击量
     */
    @ApiModelProperty(value = "点击量", name = "clickCount", example = "666")
    private Integer clickCount;

    /**
     * 购买量
     */
    @ApiModelProperty(value = "购买量", name = "buyCount", example = "66")
    private Integer buyCount;

    /**
     * 收藏量
     */
    @ApiModelProperty(value = "收藏量", name = "likeCount", example = "6")
    private Integer likeCount;

}
