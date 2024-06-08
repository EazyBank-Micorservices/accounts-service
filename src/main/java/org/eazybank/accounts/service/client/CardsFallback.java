package org.eazybank.accounts.service.client;

import org.eazybank.accounts.dto.CardDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Qualifier("cardsServiceFallBack")
public class CardsFallback implements CardsFeignClient{

    @Override
    public ResponseEntity<CardDto> fetchCardDetails(String mobileNumber) {
        return null;
    }

}
