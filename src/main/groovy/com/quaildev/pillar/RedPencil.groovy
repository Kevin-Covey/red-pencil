package com.quaildev.pillar

import java.time.Clock
import java.time.LocalDate

import static java.time.LocalDate.now
import static java.time.temporal.ChronoUnit.DAYS

class RedPencil {

    private final Clock clock

    BigDecimal price
    BigDecimal promotionalPrice
    LocalDate dateOfLastPriceChange

    RedPencil(BigDecimal price, Clock clock) {
        this.price = price
        this.clock = clock
        this.dateOfLastPriceChange = now(clock)
    }

    BigDecimal getPrice() {
        if (promotionalPrice != null && dateOfLastPriceChange.isBefore(now(clock).minus(30, DAYS))) {
            price = promotionalPrice
            promotionalPrice = null
        }
        return price
    }

    void setPrice(BigDecimal newPrice) {
        def today = now(clock)
        def changePercentage = 1 - newPrice / price
        if (changePercentage >= 0.05 && changePercentage <= 0.30) {
            if (promotionalPrice != null || today.isAfter(dateOfLastPriceChange.plusDays(29))) {
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
