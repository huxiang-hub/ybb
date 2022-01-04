package com.anaysis.service;

import com.anaysis.entity.BoxinfoViewEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoxinfoViewService {
    List<BoxinfoViewEntity> getList();
}
