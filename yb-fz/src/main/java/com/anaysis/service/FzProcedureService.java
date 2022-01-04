package com.anaysis.service;

import com.anaysis.sqlservermapper.domain.Tree;
import com.anaysis.sqlservermapper.domain.FzProcedureDO;
import org.springframework.stereotype.Service;

public interface FzProcedureService {
    public Tree<FzProcedureDO> getTree();

    public FzProcedureDO getId(Integer id);

}
