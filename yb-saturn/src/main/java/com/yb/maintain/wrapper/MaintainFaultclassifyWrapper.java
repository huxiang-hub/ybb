package com.yb.maintain.wrapper;

import com.yb.maintain.entity.MaintainFaultclassify;
import com.yb.maintain.vo.MaintainFaultclassifyVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;


/**
 * 设备故障表分类_yb_machine_faultclassify包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-15
 */
public class MaintainFaultclassifyWrapper extends BaseEntityWrapper<MaintainFaultclassify, MaintainFaultclassifyVO> {

    public static MaintainFaultclassifyWrapper build() {
        return new MaintainFaultclassifyWrapper();
    }

    @Override
    public MaintainFaultclassifyVO entityVO(MaintainFaultclassify maintainFaultclassify) {
            MaintainFaultclassifyVO maintainFaultclassifyVO = BeanUtil.copy(maintainFaultclassify, MaintainFaultclassifyVO.class);

        return maintainFaultclassifyVO;
    }

}
