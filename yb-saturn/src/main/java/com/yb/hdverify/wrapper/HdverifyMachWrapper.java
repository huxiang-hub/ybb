package com.yb.hdverify.wrapper;
import com.yb.hdverify.entity.HdverifyMach;
import com.yb.hdverify.vo.HdverifyMachVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 执行表_yb_execute_info包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-10
 */
public class HdverifyMachWrapper extends BaseEntityWrapper<HdverifyMach, HdverifyMachVO> {

    public static HdverifyMachWrapper build() {
        return new HdverifyMachWrapper();
    }

    @Override
    public HdverifyMachVO entityVO(HdverifyMach hdverifyMach) {
        HdverifyMachVO hdverifyMachVO = BeanUtil.copy(hdverifyMach, HdverifyMachVO.class);

        return hdverifyMachVO;
    }

}
