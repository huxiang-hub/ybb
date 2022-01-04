package com.sso.system.dept.wrapper;

import com.sso.base.entity.BaseDeptinfo;
import com.sso.system.dept.service.SaIDeptService;
import com.sso.system.dept.vo.SaDeptVO;
import org.springblade.common.constant.CommonConstant;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.node.INode;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class DeptWrapper extends BaseEntityWrapper<BaseDeptinfo, SaDeptVO> {

    private static SaIDeptService deptService;

    static {
        deptService = SpringUtil.getBean(SaIDeptService.class);
    }

    public static DeptWrapper build() {
        return new DeptWrapper();
    }

    @Override
    public SaDeptVO entityVO(BaseDeptinfo dept) {
        SaDeptVO deptVO = BeanUtil.copy(dept, SaDeptVO.class);
        if (Func.equals(dept.getPId(), CommonConstant.TOP_PARENT_ID)) {
            deptVO.setParentName(CommonConstant.TOP_PARENT_NAME);
        } else {
            BaseDeptinfo parent = deptService.getById(dept.getPId());
            deptVO.setParentName(parent.getDpName());
        }
        return deptVO;
    }

    public List<INode> listNodeVO(List<BaseDeptinfo> list) {
        List<INode> collect = list.stream().map(dept -> BeanUtil.copy(dept, SaDeptVO.class)).collect(Collectors.toList());
        return ForestNodeMerger.merge(collect);
    }

}
