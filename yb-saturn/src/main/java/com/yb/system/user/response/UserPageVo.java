package com.yb.system.user.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author lzb
 * @Date 2021/2/26 10:15
 **/
@ApiModel("车间用户分页VO")
@Data
public class UserPageVo {

	@ApiModelProperty("id")
	private int ID;

	@ApiModelProperty("姓名")
	private String name;

	@ApiModelProperty("工号")
	private String jobnum;

	@ApiModelProperty("联系电话")
	private String phone;

	@ApiModelProperty("部门名称ID--车间")
	private int dpId;

	@ApiModelProperty("部门名称：deptName")
	private String deptName;

	@ApiModelProperty("类型：1.管理/2.生产")
	private int mold;

	@ApiModelProperty("岗位：1.机长2.班长3.车间主管4排产员")
	private int jobs;

	@ApiModelProperty("劳动类型：1正式员工2临时员工3试用期4实习员工")
	private int laborer;

	@ApiModelProperty("工龄（入职时间）")
	private Date hireTime;

	@ApiModelProperty("生产型：工种|竖线分隔")
	private String processes;

	@ApiModelProperty("班组id")
	private int bcId;

	@ApiModelProperty("班组名称")
	private String bcName;

	@ApiModelProperty("是否启用1、停用0")
	private int isUsed;

	@ApiModelProperty("用户id")
	private int userId;
}
