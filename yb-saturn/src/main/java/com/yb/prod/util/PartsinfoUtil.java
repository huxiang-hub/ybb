package com.yb.prod.util;

import com.yb.prod.vo.ProdPartsinfoVo;

import java.util.ArrayList;
import java.util.List;

public class PartsinfoUtil {
    //    List<ProdPartsinfoVo>转树结构
    public static List<ProdPartsinfoVo> listGetStree(List<ProdPartsinfoVo> list) {
        List<ProdPartsinfoVo> treeList = new ArrayList<ProdPartsinfoVo>();
        for (ProdPartsinfoVo tree : list) {
            //找到根
            if (tree.getPid() == null) {
                treeList.add(tree);
            }
            //找到子
            for (ProdPartsinfoVo treeNode : list) {
                if (treeNode.getPid() != null && treeNode.getPid().intValue() == tree.getId().intValue()) {
                    System.out.println("进入梳妆转换 部门树形转化");
                    System.out.println(treeNode.getPid());
                    System.out.println(tree.getId());
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<ProdPartsinfoVo>());
                    }
                    tree.getChildren().add(treeNode);
                }
            }
        }
        return treeList;
    }
}
