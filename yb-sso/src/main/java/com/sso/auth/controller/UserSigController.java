package com.sso.auth.controller;


import com.sso.auth.service.IuserSigService;
import com.sso.auth.service.imp.IuserSigServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("vimSig")
@AllArgsConstructor
public class UserSigController {

    @Autowired
    private IuserSigService sigService;

    @GetMapping("/getSin")
    public String getSin(String userId) {

        return sigService.generateUserSig(userId);
    }


    public static void main(String[] args) {
        IuserSigService sigService = new IuserSigServiceImp();
        String result = sigService.generateUserSig("administrator");
        System.out.println(result);
    }
}
