package com.myproject;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CARDS")
public class Card {

    @Id
    @Column(name = "number", nullable = false)
    private Long number;
    @Column(name = "name")
    private String name;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "url")
    private String url;
    @Column(name = "phone")
    private String phone;
    @Column(name = "last_request", nullable = false)
    private LocalDateTime lastRequest;

    public Card(Long cardNumber, String name, String country, String city, String url, String phone) {
    this.number = cardNumber;
    this.name = name;
    this.country = country;
    this.city = city;
    this.url = url;
    this.phone = phone;
    this.lastRequest = LocalDateTime.now();
    }

    public Card() {

    }
}
