package com.myproject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myproject.exception.NotFoundException;
import com.myproject.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final CardRepository cardRepository;

    private final static Integer MINCARDMUMBER = 8;
    private final static Integer MAXCARDMUMBER = 16;

    public CardFull getCardInformation(String cardNumber){
        URI uri = URI.create("https://lookup.binlist.net/" + getBin(cardNumber));
        log.info("Отправлен запрос : {}", uri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        CardFull cardFull;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                if(!jsonElement.isJsonObject()) {
                    throw new NotFoundException("Card number not found");
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                cardFull = new Gson().fromJson(jsonObject, CardFull.class);
                saveCard(createCard(cardFull, getCardNumber(cardNumber)));
            } else {
                throw new ValidationException("Error card number");
            }
        } catch (IOException | InterruptedException e) {
            throw new ValidationException("Error card number");
        }catch (NullPointerException e){
            throw new NotFoundException("Card number not found");
        }
        log.info("Получены данные карты : {}", cardFull);
        return cardFull;
    }

    public Card createCard(CardFull cardFull, Long cardNumber){
        return new Card(
                cardNumber,
                cardFull.getBank().getName(),
                cardFull.getCountry().getName(),
                cardFull.getBank().getCity(),
                cardFull.getBank().getUrl(),
                cardFull.getBank().getPhone()
        );
    }

    public void saveCard(Card card){
        log.info("Сохранены данные карты : {}", card);
        cardRepository.save(card);
    }

    public Integer getBin(String cardNumber){
        if (cardNumber.length() >= MINCARDMUMBER){
            log.info("Получен bin из карты : {}", cardNumber);
            return Integer.parseInt(cardNumber.substring(0, MINCARDMUMBER));
        }
        throw new ValidationException("Error card number length");
    }

    public Long getCardNumber(String cardNumber){
        if (cardNumber.length() >= MINCARDMUMBER && cardNumber.length() <= MAXCARDMUMBER){
            log.info("Получен номер карты : {}", cardNumber);
            return Long.parseLong(cardNumber);
        }
        throw new ValidationException("Error card number length");
    }

    public List<Bank> getAllBanks(Integer from, Integer size) {
        PageRequest page = pagination(from, size);
        List<Card> cards;
        cards = cardRepository.findAll(page).toList();
        List<Bank> bankDto;
        bankDto = cards.stream().map(this::mappingBankDto).collect(Collectors.toList());
        log.info("Получен список банков : {}", bankDto);
        return bankDto;
    }

    private PageRequest pagination(int from, int size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size, Sort.unsorted());
    }

    public Bank mappingBankDto(Card card){
        return new Bank(
                card.getName(),
                card.getUrl(),
                card.getPhone(),
                card.getCity()
        );
    }
}
