package com.sso.system.dict.wrapper;


import com.sso.system.dict.entity.Dict;
import com.sso.system.dict.service.SaIDictService;
import com.sso.system.dict.vo.DictVO;
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
public class DictWrapper extends BaseEntityWrapper<Dict, DictVO> {

    private static SaIDictService dictService;

    static {
        dictService = SpringUtil.getBean(SaIDictService.class);
    }

    public static DictWrapper build() {
        return new DictWrapper();
    }

    @Override
    public DictVO entityVO(Dict dict) {
        DictVO dictVO = BeanUtil.copy(dict, DictVO.class);
        if (Func.equals(dict.getParentId(), CommonConstant.TOP_PARENT_ID)) {
            dictVO.setParentName(CommonConstant.TOP_PARENT_NAME);
        } else {
            Dict parent = dictService.getById(dict.getParentId());
            dictVO.setParentName(parent.getDictValue());
        }
        return dictVO;
    }

    public List<INode> listNodeVO(List<Dict> list) {
        List<INode> collect = list.stream().map(dict -> BeanUtil.copy(dict, DictVO.class)).collect(Collectors.toList());
        return ForestNodeMerger.merge(collect);
    }

}
