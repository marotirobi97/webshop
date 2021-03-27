package com.example.webshop.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Gender {
    MAN("Man"),
    WOMEN("Women");

    private final String gender;

    public String getGender(){
        return gender;
    }

}
