package com.quaildev.pillar

import java.time.Clock
import java.time.LocalDate

class RedPencil {

    private final Clock clock

    BigDecimal price
    BigDecimal promotionalPrice
    LocalDate dateOfLastPriceChange

    RedPencil(BigDecimal price, Clock clock) {
        this.price = price
        this.clock = clock
        this.dateOfLastPriceChange = LocalDate.now(clock)
    }

    void setPrice(BigDecimal newPrice) {
        def today = LocalDate.now(clock)
        def changePercentage = 1 - newPrice / price
        if (changePercentage >= 0.05 && changePercentage <= 0.30) {
            if (today.isAfter(dateOfLastPriceChange.plusDays(29))) {
                promotionalPrice = newPrice
            } else {
                price = newPrice
            }
        } else {
            price = newPrice
        }
        dateOfLastPriceChange = today
    }

}
