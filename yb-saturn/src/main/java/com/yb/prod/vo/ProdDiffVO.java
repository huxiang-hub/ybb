package com.yb.prod.vo;
import com.yb.process.entity.ProcessClassify;
import com.yb.prod.entity.ProdClassify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

@Data
@ApiModel(value = "procdif", description = " 工艺难易程度")
public class ProdDiffVO extends ProdClassify implements Serializable {
    //难易程度id
    Integer diffId;
    //产品分类Id
    private Integer pcId;
    //工序ID
    private Integer prId;
    //难度
    private Double diff;
    /**包含工序数量种类' */
    @ApiModelProperty(value = "包含工序数量种类'")
    private Integer prNum;
}
