package com.example.webshop.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Gender {
    MAN("Man"),
    WOMEN("Women");

    private final String Gender;

    public String getGender(){
        return Gender;
    }

}
