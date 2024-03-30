package com.yang.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRquest implements Serializable {

    private String userAccount;
    private String userPassword;
}
