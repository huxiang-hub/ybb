package com.anaysis.service.impl;

import com.anaysis.sqlservermapper.domain.Tree;
import com.anaysis.sqlservermapper.domain.BuildTree;
import com.anaysis.sqlservermapper.FzProcedureDao;
import com.anaysis.sqlservermapper.domain.FzMachineDO;
import com.anaysis.sqlservermapper.domain.FzProcedureDO;

import com.anaysis.service.FzMachineService;
import com.anaysis.service.FzProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FzProcedureServiceImpl implements FzProcedureService {
    @Autowired
    FzProcedureDao fzProcedureDao;
    @Autowired
    FzMachineService fzmachineservice;

    @Override
    public Tree<FzProcedureDO> getTree() {
        //获取工序的列表，根据工序获得工序下面的设备信息
        List<Tree<FzProcedureDO>> trees = new ArrayList<Tree<FzProcedureDO>>();

        List<FzProcedureDO> fzproceInfo = fzProcedureDao.list();
        for (FzProcedureDO fzproce : fzproceInfo) {
            Tree<FzProcedureDO> tree = new Tree<FzProcedureDO>();
            fzproce.setCContent("");//因为有html代码，所以导致数据展示异常，重置该字段为空字符串
            tree.setId("p_" + fzproce.getId().toString()); //父节点信息
            Map<String, Object> state = new HashMap<>();
            state.put("opened", false);
            tree.setState(state);


            //获取工序中對應的对应的机器
            List<FzMachineDO> machineLs = fzmachineservice.list(fzproce.getCGzzxIDStr());
            int num = (machineLs != null) ? machineLs.size() : 0;
            for (int i = 0; machineLs != null && i < machineLs.size(); i++) {
                FzMachineDO msinf = machineLs.get(i);
                Tree<FzProcedureDO> treeU = new Tree<FzProcedureDO>();
                treeU.setId("m_" + msinf.getId().toString()+"p_" + fzproce.getId().toString());
                treeU.setParentId("p_" + fzproce.getId().toString());

                treeU.setText("<span style='color:#000000'>" + msinf.getCJzmc() + "(" + msinf.getCbmmc() +"_" +msinf.getId()+")");
                //treeU.setText(msinf.getCJzmc());
                Map<String, Object> state1 = new HashMap<>();
                state1.put("opened", false);
                treeU.setState(state1);
                treeU.setChecked(true);
                if (isExist(trees, treeU)) {
                    num--;
                } else {
                    trees.add(treeU);
                }
            }
            tree.setText(fzproce.getCName() + "(" + num + ")");
            trees.add(tree);

        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<FzProcedureDO> t = BuildTree.build(trees);
        return t;
    }

    private boolean isExist(List<Tree<FzProcedureDO>> trees, Tree<FzProcedureDO> tree) {
        if (trees != null) {
            List<Tree<FzProcedureDO>> treesnew = new ArrayList<>();
            for (Tree<FzProcedureDO> tsinfo : trees) {
                //判断树的节点是否已经增加过，如果增加则不再重复增加。
                if (tsinfo.getId().equalsIgnoreCase(tree.getId())) {

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public FzProcedureDO getId(Integer id) {
        return fzProcedureDao.getId(id);
    }
}
