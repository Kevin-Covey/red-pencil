package com.quaildev.pillar

import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class RedPencilTest extends Specification {

    static final ZoneId TIME_ZONE = ZoneId.of('UTC')

    def 'constructing a red pencil sets the price and the date of last price change to now()'() {
        given:
        Clock mockClock = Mock()
        mockClock.instant() >> Instant.now()
        mockClock.zone >> TIME_ZONE

        when:
        def redPencil = new RedPencil(6.29, mockClock)

        then:
        redPencil.price == 6.29
        redPencil.dateOfLastPriceChange == LocalDate.now(TIME_ZONE)
    }

}
