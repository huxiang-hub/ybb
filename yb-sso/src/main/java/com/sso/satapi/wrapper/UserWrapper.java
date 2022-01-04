package com.sso.satapi.wrapper;

import com.sso.system.entity.SaUser;
import com.sso.system.service.SaIUserService;
import com.sso.system.vo.SaUserVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Jenny wang
 */
public class UserWrapper extends BaseEntityWrapper<SaUser, SaUserVO> {

    private static com.sso.system.service.SaIUserService SaIUserService;

//    private static IDictClient dictClient;

    static {
        SaIUserService = SpringUtil.getBean(SaIUserService.class);
//        dictClient = SpringUtil.getBean(IDictClient.class);
    }

    public static UserWrapper build() {
        return new UserWrapper();
    }

    @Override
    public SaUserVO entityVO(SaUser saUser) {
        SaUserVO userVO = BeanUtil.copy(saUser, SaUserVO.class);
        List<String> roleName = SaIUserService.getRoleName(saUser.getRoleId());
        List<String> deptName = null;
        if (saUser.getDeptId() != null && !saUser.getDeptId().equals("")) {
            deptName = SaIUserService.getDeptName(saUser.getDeptId());
            userVO.setDeptName(Func.join(deptName));
        }
        userVO.setRoleName(Func.join(roleName));
//        R<String> dict = dictClient.getValue("sex", Func.toInt(user.getSex()));
//        if (dict.isSuccess()) {
//            userVO.setSexName(dict.getData());
//        }
        return userVO;
    }

}
