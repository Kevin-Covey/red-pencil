package com.quaildev.pillar

import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

import static java.time.temporal.ChronoUnit.DAYS

class RedPencilTest extends Specification {

    static final ZoneId TIME_ZONE = ZoneId.of('UTC')

    Clock mockClock = Mock()

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
        mockClock.instant() >>> [Instant.now(), Instant.now().plus(30, DAYS)]
        def redPencil = new RedPencil(6.29, mockClock)

        when:
        redPencil.price = 5.49

        then:
        redPencil.price == 6.29
        redPencil.promotionalPrice == 5.49
        redPencil.dateOfLastPriceChange == LocalDate.now(TIME_ZONE).plusDays(30)
    }

}
