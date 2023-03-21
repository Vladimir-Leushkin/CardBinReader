package com.myproject;

import lombok.Data;

@Data
public class Bank {
    private String name;
    private String url;
    private String phone;
    private String city;

    public Bank(String name, String url, String phone, String city) {
        this.name = name;
        this.url = url;
        this.phone = phone;
        this.city = city;
    }
}
