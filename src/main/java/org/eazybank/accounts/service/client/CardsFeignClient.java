package org.eazybank.accounts.service.client;

import org.eazybank.accounts.dto.CardDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cards", fallback = CardsFallback.class)
@Qualifier("cardsServiceClient")
public interface CardsFeignClient {

    @GetMapping(value = "/api/fetch", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CardDto> fetchCardDetails(@RequestParam String mobileNumber);
}
