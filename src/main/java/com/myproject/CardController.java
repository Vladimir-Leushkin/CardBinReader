package com.myproject;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/card")
public class CardController {

    private final CardService cardService;

    @GetMapping("/{cardNumber}")
    public Card getCardInformation(@PathVariable String cardNumber){
        return cardService.getCardInformation(cardNumber);
    }

}
