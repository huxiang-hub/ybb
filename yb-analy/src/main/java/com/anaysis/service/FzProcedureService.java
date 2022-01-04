package com.anaysis.service;

import com.anaysis.entity.FzProcedureDO;
import com.anaysis.entity.Tree;

public interface FzProcedureService {
    public Tree<FzProcedureDO> getTree();

    public FzProcedureDO getId(Integer id);

}
