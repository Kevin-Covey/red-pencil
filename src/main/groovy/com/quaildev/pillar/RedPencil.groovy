package com.quaildev.pillar

import java.time.Clock
import java.time.LocalDate

class RedPencil {

    private final Clock clock

    Double price
    Double promotionalPrice
    LocalDate dateOfLastPriceChange

    RedPencil(Double price, Clock clock) {
        this.price = price
        this.clock = clock
        this.dateOfLastPriceChange = LocalDate.now(clock)
    }

    void setPrice(Double newPrice) {
        promotionalPrice = newPrice
        dateOfLastPriceChange = LocalDate.now(clock)
    }
}
