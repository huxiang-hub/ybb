package com.yb.quality.vo;

import com.yb.machine.vo.MachineMainfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/31 14:24
 **/
@Data
@ApiModel(value = "质量检查查询对象")
public class ProcessClassifyVo {

	@ApiModelProperty("ID主键")
	private int id;

	@ApiModelProperty("分类名称")
	private String pyName;

	@ApiModelProperty("分类编号")
	private String pyNum;

	@ApiModelProperty("显示顺序")
	private int sort;

	@ApiModelProperty("状态 1启用0停用")
	private int status;

	@ApiModelProperty("创建时间")
	private Date createAt;

	@ApiModelProperty("华博erp的uuid")
	private String erpId;

	@ApiModelProperty("树的子：设备")
	private List<MachineMainfoVO> machines;

}
