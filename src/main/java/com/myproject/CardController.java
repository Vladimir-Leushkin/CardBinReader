package com.myproject;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/card")
public class CardController {

    private final CardService cardService;

    @GetMapping("/{cardNumber}")
    public CardFull getCardInformation(@PathVariable String cardNumber){
        return cardService.getCardInformation(cardNumber);
    }

    @GetMapping("/bank")
    public List<Bank> getAllBanks(
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ){
        return cardService.getAllBanks(from, size);
    }

}
