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
    private LocalDate datePromotionStarted

    RedPencil(BigDecimal price, Clock clock) {
        this.price = price
        this.clock = clock
        this.dateOfLastPriceChange = now(clock)
    }

    BigDecimal getPrice() {
        if (promotionalPrice != null && datePromotionStarted.isBefore(now(clock).minus(30, DAYS))) {
            endPromotion(promotionalPrice)
        }
        return price
    }

    void setPrice(BigDecimal newPrice) {
        def today = now(clock)
        getPrice()
        if (priceChangeIsWithinPromotionBoundaries(newPrice)) {
            if (promotionIsInProgress()) {
                promotionalPrice = newPrice
            } else if (priceHasBeenStable(today)) {
                datePromotionStarted = today
                promotionalPrice = newPrice
            } else {
                price = newPrice
            }
        } else {
            endPromotion(newPrice)
        }
        dateOfLastPriceChange = today
    }

    private boolean priceChangeIsWithinPromotionBoundaries(BigDecimal newPrice) {
        def changePercentage = 1 - newPrice / price
        changePercentage >= 0.05 && changePercentage <= 0.30
    }

    private boolean promotionIsInProgress() {
        promotionalPrice != null
    }

    private boolean priceHasBeenStable(LocalDate today) {
        today.isAfter(dateOfLastPriceChange.plusDays(29))
    }

    private void endPromotion(newPrice) {
        price = newPrice
        promotionalPrice = null
    }

}
