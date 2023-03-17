package com.myproject;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CARDS")
public class CardDto {

    @Id
    @Column(name = "number", nullable = false)
    private Long number;
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

    public CardDto(Long cardNumber, String country, String city, String url, String phone) {
    this.number = cardNumber;
    this.country = country;
    this.city = city;
    this.url = url;
    this.phone = phone;
    this.lastRequest = LocalDateTime.now();
    }

    public CardDto() {

    }
}
