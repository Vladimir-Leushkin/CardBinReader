package com.myproject;

import lombok.Data;

@Data
public class Country {
    private String numeric;
    private String alpha2;
    private String name;
    private String emoji;
    private String currency;
    private Byte latitude;
    private Byte longitude;

}
