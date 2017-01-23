package com.quaildev.pillar

import spock.lang.Specification

import java.time.*

class RedPencilTest extends Specification {

    static final ZoneId TIME_ZONE = ZoneId.of('UTC')

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
        def dateOfPriceChange = LocalDate.of(2017, 3, 31)
        mockClock.instant() >>> [LocalDate.of(2017, 3, 1) as Instant, dateOfPriceChange as Instant]
        def redPencil = new RedPencil(6.29, mockClock)

        when:
        redPencil.price = 5.49

        then:
        redPencil.price == 6.29
        redPencil.promotionalPrice == 5.49
        redPencil.dateOfLastPriceChange == dateOfPriceChange
    }

    def 'price reduction within 30 days does not start a promotion'() {
        given:
        def dateOfPriceChange = LocalDate.of(2017, 3, 30)
        mockClock.instant() >>> [LocalDate.of(2017, 3, 1) as Instant, dateOfPriceChange as Instant]
        def redPencil = new RedPencil(6.29, mockClock)

        when:
        redPencil.price = 5.49

        then:
        redPencil.price == 5.49
        redPencil.promotionalPrice == null
        redPencil.dateOfLastPriceChange == dateOfPriceChange
    }

    def 'price reduction < 5% does not start a promotion'() {
        given:
        def dateOfPriceChange = LocalDate.of(2017, 3, 31)
        mockClock.instant() >>> [LocalDate.of(2017, 3, 1) as Instant, dateOfPriceChange as Instant]
        def redPencil = new RedPencil(10.00, mockClock)

        when:
        redPencil.price = 9.51

        then:
        redPencil.price == 9.51
        redPencil.promotionalPrice == null
        redPencil.dateOfLastPriceChange == dateOfPriceChange
    }

}
