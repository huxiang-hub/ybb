package com.yb.panel.request;

import com.yb.machine.vo.MachineMainfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/18 9:22
 **/
@ApiModel("设备菜单")
@Data
public class PanelCustomizeRequest extends Query {

	@ApiModelProperty("选择的设备ID集合")
	private List<Integer> maIdList;

	@ApiModelProperty("选择的设备")
	private List<MachineMainfoVO> maList;

	@ApiModelProperty("修改的设备名称")
	private String maName;

	@ApiModelProperty("修改选择的菜单")
	private List<Integer> selectMenu;

	@ApiModelProperty("批量修改的设备名称")
	private List<String > maNameList;

	@ApiModelProperty("批量修改的菜单ID")
	private List<Integer> checkList;
}
