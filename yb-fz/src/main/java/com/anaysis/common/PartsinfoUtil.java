package com.anaysis.common;

import com.anaysis.entity.ProdPartsinfoVO;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class PartsinfoUtil {
    //    List<ProdPartsinfoVo>转树结构
    public static List<ProdPartsinfoVO> listGetStree(List<ProdPartsinfoVO> list) {
        List<ProdPartsinfoVO> treeList = new ArrayList<ProdPartsinfoVO>();
        for (ProdPartsinfoVO tree : list) {
            //找到根
            if (tree.getIDAPComp() == null) {
                treeList.add(tree);
            }
            //找到子
            for (ProdPartsinfoVO treeNode : list) {
                if (treeNode.getIDAPComp() != null && treeNode.getIDAPComp().intValue() == tree.getIDAComp().intValue()) {
                    System.out.println("进入树状转换");
                    System.out.println(treeNode.getIDAPComp());
                    System.out.println(tree.getIDAComp());
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<ProdPartsinfoVO>());
                    }
                    tree.getChildren().add(treeNode);
                }
            }
        }
        return treeGetPartsinfo(treeList);
    }

    public static List<ProdPartsinfoVO> treeGetPartsinfo(List<ProdPartsinfoVO> list){
        for (ProdPartsinfoVO partsinfoVO : list){
            if ((!Func.isEmpty(partsinfoVO.getChildren())) && (!Func.isEmpty(partsinfoVO.getProdProcelinks()))){
//                作为一个部件
                ProdPartsinfoVO prod= new ProdPartsinfoVO();
                BeanUtils.copyProperties(partsinfoVO, prod);
                prod.setChildren(null);
//              将父部件的物料和工序置空
                partsinfoVO.setProdProcelinks(null);
                partsinfoVO.setMaterProdlinks(null);
//                加入子部件中
                List<ProdPartsinfoVO> ttLits = partsinfoVO.getChildren();
                ttLits.add(prod);
                partsinfoVO.setChildren(ttLits);

                treeGetPartsinfo(partsinfoVO.getChildren());
            }
        }
        return list;
    }
}
