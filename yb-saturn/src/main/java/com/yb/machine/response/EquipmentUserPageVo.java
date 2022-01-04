package com.yb.machine.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yb.system.role.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author lzb
 * @Date 2021/2/23 15:02
 **/

@ApiModel("系统管理用户分页VO")
@Data
public class EquipmentUserPageVo {

	@ApiModelProperty("id")
	private Integer id;

	@ApiModelProperty("部门名称")
	private String deptName;

	@ApiModelProperty("账号")
	private String account;

	@ApiModelProperty("真实姓名")
	private String realName;

	@ApiModelProperty("性别:(1:男，0女)")
	private Integer sex;

	@ApiModelProperty("手机号")
	private String phone;

	@ApiModelProperty("设备名称")
	private String  maName;

	@ApiModelProperty("头像")
	private String avatar;

	@ApiModelProperty("角色相关信息")
	private List<Role> role;

	@ApiModelProperty("角色id集合")
	@JsonIgnore
	private String roleIds;
}
