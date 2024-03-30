package com.yang.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRquest implements Serializable {

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
