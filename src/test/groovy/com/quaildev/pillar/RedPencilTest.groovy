package com.quaildev.pillar

import spock.lang.Specification

import java.time.*
import java.time.temporal.TemporalUnit

import static java.time.temporal.ChronoUnit.DAYS

class RedPencilTest extends Specification {

    static final ZoneId TIME_ZONE = ZoneId.of('UTC')

    LocalDate expectedDateOfLastPriceChange
    Clock mockClock = Mock()

    def setupSpec() {
        LocalDate.metaClass.asType = { Class<Instant> type ->
            delegate.atStartOfDay().toInstant(ZoneOffset.UTC)
        }
    }

    def setup() {
        mockClock.zone >> TIME_ZONE
    }

    def 'constructing a red pencil sets the price and the date of last price change to now()'() {
        given:
        mockClock.instant() >> Instant.now()

        when:
        def redPencil = new RedPencil(6.29, mockClock)

        then:
        redPencil.price == 6.29
        redPencil.dateOfLastPriceChange == LocalDate.now(TIME_ZONE)
    }

    def 'price reduction after 30 days starts a promotion'() {
        given:
        priceChangeOccursAfter 30, DAYS
        def redPencil = new RedPencil(6.29, mockClock)

        when:
        redPencil.price = 5.49

        then:
        redPencil.price == 6.29
        redPencil.promotionalPrice == 5.49
        redPencil.dateOfLastPriceChange == expectedDateOfLastPriceChange
    }

    def 'price reduction within 30 days does not start a promotion'() {
        given:
        priceChangeOccursAfter 29, DAYS
        def redPencil = new RedPencil(6.29, mockClock)

        when:
        redPencil.price = 5.49

        then:
        redPencil.price == 5.49
        redPencil.promotionalPrice == null
        redPencil.dateOfLastPriceChange == expectedDateOfLastPriceChange
    }

    def 'price reduction < 5% does not start a promotion'() {
        given:
        priceChangeOccursAfter 30, DAYS
        def redPencil = new RedPencil(10.00, mockClock)

        when:
        redPencil.price = 9.51

        then:
        redPencil.price == 9.51
        redPencil.promotionalPrice == null
        redPencil.dateOfLastPriceChange == expectedDateOfLastPriceChange
    }

    def 'price reduction > 30% does not start a promotion'() {
        given:
        priceChangeOccursAfter 30, DAYS
        def redPencil = new RedPencil(10.00, mockClock)

        when:
        redPencil.price = 6.99

        then:
        redPencil.price == 6.99
        redPencil.promotionalPrice == null
        redPencil.dateOfLastPriceChange == expectedDateOfLastPriceChange
    }

    def 'price changes of exactly 5% and 30% start a promotion'() {
        given:
        priceChangeOccursAfter 30, DAYS
        def redPencil = new RedPencil(10.00, mockClock)

        when:
        redPencil.price = newPrice

        then:
        redPencil.price == 10.00
        redPencil.promotionalPrice == newPrice
        redPencil.dateOfLastPriceChange == expectedDateOfLastPriceChange

        where:
        newPrice << [9.50, 7.00]
    }

    def priceChangeOccursAfter(int quantity, TemporalUnit increment) {
        def initialPriceDate = LocalDate.of(2017, 3, 31)
        expectedDateOfLastPriceChange = initialPriceDate.plus(quantity, increment)
        mockClock.instant() >>> [initialPriceDate, expectedDateOfLastPriceChange].collect { it as Instant }
    }

}
