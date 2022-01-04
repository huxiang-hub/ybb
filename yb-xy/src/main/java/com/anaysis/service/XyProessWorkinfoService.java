package com.anaysis.service;

import com.anaysis.entity.ProcessWorkinfo;
import com.anaysis.entity.ProcessWorkinfoVO;
import com.anaysis.sqlservermapper.domain.FzProcedureDO;
import com.anaysis.sqlservermapper.domain.Tree;

import java.util.List;

public interface XyProessWorkinfoService {

    List<ProcessWorkinfoVO> list();
}
