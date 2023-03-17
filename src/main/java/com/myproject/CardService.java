package com.myproject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myproject.exception.NotFoundException;
import com.myproject.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final CardRepository cardRepository;

    private final static Integer MINCARDMUMBER = 8;
    private final static Integer MAXCARDMUMBER = 16;

    public Card getCardInformation(String cardNumber){
        URI uri = URI.create("https://lookup.binlist.net/" + getBin(cardNumber));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        Card card = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                if(!jsonElement.isJsonObject()) {
                    throw new NotFoundException("Card number not found");
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                card = new Gson().fromJson(jsonObject, Card.class);
                saveCard(createCardDto(card, getCardNumber(cardNumber)));
            } else {
                throw new ValidationException("Error card number");
            }
        } catch (IOException | InterruptedException e) {
            throw new ValidationException("Error card number");
        }catch (NullPointerException e){
            throw new NotFoundException("Card number not found");
        }
        return card;
    }

    public CardDto createCardDto(Card card, Long cardNumber){
        return new CardDto(
                cardNumber,
                card.getCountry().getName(),
                card.getBank().getCity(),
                card.getBank().getUrl(),
                card.getBank().getPhone()
        );
    }

    public void saveCard(CardDto cardDto){
        cardRepository.save(cardDto);
    }

    public Integer getBin(String cardNumber){
        if (cardNumber.length() >= MINCARDMUMBER){
            return Integer.parseInt(cardNumber.substring(0, MINCARDMUMBER));
        }
        throw new ValidationException("Error card number length");
    }

    public Long getCardNumber(String cardNumber){
        if (cardNumber.length() >= MINCARDMUMBER && cardNumber.length() <= MAXCARDMUMBER){
            return Long.parseLong(cardNumber);
        }
        throw new ValidationException("Error card number length");
    }
}
