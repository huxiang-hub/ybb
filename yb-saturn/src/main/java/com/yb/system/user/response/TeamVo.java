package com.yb.system.user.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lzb
 * @Date 2021/3/5 9:36
 **/
@Data
@TableName("yb_base_classinfo")
@ApiModel(value = "BaseDeptinfo对象", description = "班组信息_yb_ba_dept")
public class TeamVo implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private int id;

	@ApiModelProperty("车间id")
	private int dpId;

	@ApiModelProperty("班组名称")
	private String bcName;

	@ApiModelProperty("班组人数")
	private int bcNum;

	@ApiModelProperty("顺序默认100")
	private int sort;

	@ApiModelProperty("班次id")
	private int wsId;

	@ApiModelProperty("是否停用：1.启用0.停用")
	private int isUsed;

}
