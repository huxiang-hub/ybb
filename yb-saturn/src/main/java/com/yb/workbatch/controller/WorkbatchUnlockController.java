package com.yb.workbatch.controller;

import com.yb.workbatch.service.WorkbatchUnlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/WorkbatchUnlock")
public class WorkbatchUnlockController {

    @Autowired
    private WorkbatchUnlockService workbatchUnlockService;
}
