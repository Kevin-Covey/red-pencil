package com.quaildev.pillar

import java.time.Clock
import java.time.LocalDate

class RedPencil {

    private final Clock clock

    double price
    double promotionalPrice
    LocalDate dateOfLastPriceChange

    RedPencil(double price, Clock clock) {
        this.price = price
        this.clock = clock
        this.dateOfLastPriceChange = LocalDate.now(clock)
    }

    void setPrice(double newPrice) {
        promotionalPrice = newPrice
        dateOfLastPriceChange = LocalDate.now(clock)
    }
}
