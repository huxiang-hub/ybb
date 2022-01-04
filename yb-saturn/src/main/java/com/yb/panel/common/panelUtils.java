package com.yb.panel.common;

import com.yb.panel.vo.PanelMenuVO;
import com.yb.prod.vo.ProdPartsinfoVo;

import java.util.ArrayList;
import java.util.List;

public class panelUtils {
    //    List<ProdPartsinfoVo>转树结构
    public static List<PanelMenuVO> listGetStree(List<PanelMenuVO> list) {
        List<PanelMenuVO> treeList = new ArrayList<PanelMenuVO>();
        for (PanelMenuVO tree : list) {
            //找到根
            if (tree.getPanelPId() == null) {
                treeList.add(tree);
            }
            //找到子
            for (PanelMenuVO treeNode : list) {
                if (treeNode.getPanelPId() != null && treeNode.getPanelPId().intValue() == tree.getPanelId().intValue()) {
                    System.out.println("进入梳妆转换 菜单按钮功能");
                    System.out.println(treeNode.getPanelPId());
                    System.out.println(tree.getPanelId());
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<PanelMenuVO>());
                    }
                    tree.getChildren().add(treeNode);
                }
            }
        }
        return treeList;
    }
}
