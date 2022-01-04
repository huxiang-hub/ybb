package com.yb.base.vo;

import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.process.vo.ProcessClassifyVO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeptModelVO  implements Serializable {

    /**
     * 二级车间
     */
    private Integer dpId;//部门
    private String pyName;// pyName  后台
    private String pdName;// dpName  后台
    private Integer prId; // pr_id  后台

    /**
     * 后台给前端组件的字段 对应
     */
    private Integer id;//id
    private Integer value; // pr_id
    private String label;// dpName
    /**
     * 三级工序
     */
    private List<MachineMainfoVO> children = new ArrayList<>();
}
