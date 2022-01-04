package com.yb.execute.vo;

import com.yb.base.entity.BasePicture;
import com.yb.execute.entity.ExecuteScrap;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 审核清单_yb_execute_scrap视图实体类
 *
 * @author BladeX
 * @since 2021-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteScrapVO对象", description = "审核清单_yb_execute_scrap")
public class ExecuteScrapVO extends ExecuteScrap {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "产品名")
	private String pdName;

	@ApiModelProperty(value = "设备名")
	private String maName;

	@ApiModelProperty(value = "工序名")
	private String prName;

	@ApiModelProperty(value = "提交人")
	private String usName;

	@ApiModelProperty(value = "上报人")
	private String reportName;

	@ApiModelProperty(value = "白班夜班")
	private String ckName;

	@ApiModelProperty(value = "客户名")
	private String cmName;

	@ApiModelProperty(value = "审核状态：1待审核，2已审核，3驳回")
	private String exStatus;

	@ApiModelProperty(value = "钉钉审核流程一id")
	private String processInstanceId;

	@ApiModelProperty(value = "图片")
	private List<BasePicture> basePictureList;
}
