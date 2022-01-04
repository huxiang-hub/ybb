package com.anaysis.common;

import com.anaysis.entity.BaseDeptInfoVo;
import com.anaysis.entity.ProdPartsinfoVO;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class DeptUtil {

    public static List<BaseDeptInfoVo> getTree(List<BaseDeptInfoVo> list){
        List<BaseDeptInfoVo> treeList = new ArrayList<BaseDeptInfoVo>();
        for (BaseDeptInfoVo tree : list) {
            //找到根
            if (tree.getErpPid().equals("0")) {
                treeList.add(tree);
            }
            //找到子
            for (BaseDeptInfoVo treeNode : list) {
                if (!treeNode.getErpPid().equals("0") && treeNode.getErpPid().equals(tree.getErpId())) {
                    System.out.println("进入梳妆转换  部门转化");
                    //System.out.println(treeNode.getErpPid());
                    //System.out.println(tree.getErpId());
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<BaseDeptInfoVo>());
                    }
                    tree.getChildren().add(treeNode);
                }
            }
        }
        return treeList;
    }
}
