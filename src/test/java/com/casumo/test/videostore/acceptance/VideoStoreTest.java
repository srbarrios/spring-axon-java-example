package com.casumo.test.videostore.acceptance;

import com.casumo.test.videostore.aggregate.CustomerAggregate;
import com.casumo.test.videostore.constants.FilmFormat;
import com.casumo.test.videostore.constants.FilmType;
import com.casumo.test.videostore.coreapi.*;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static java.util.Collections.singletonList;

public class VideoStoreTest {

    private FixtureConfiguration<CustomerAggregate> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(CustomerAggregate.class);
    }

    @Test
    public void testRentAFilm() throws Exception {
        fixture.given(new CustomerCreatedEvent("customer1", "name1", 0),
                new FilmCreatedEvent("film1", "GhostBusters",
                        FilmType.old,
                        FilmFormat.vhs,
                        "SciFy",
                        singletonList("english"),
                        12,
                        new Date(1995, 1, 1),
                        ""))
                .when(new RentFilmsCommand("customer1", singletonList("film1"), 3))
                .expectEvents(new FilmsRentedEvent("customer1", singletonList("film1"), 3));
    }

    @Test
    public void testReturnAFilm() {
        fixture.given(new CustomerCreatedEvent("customer1", "name1", 0),
                new FilmCreatedEvent("film1", "GhostBusters",
                        FilmType.old,
                        FilmFormat.vhs,
                        "SciFy",
                        singletonList("english"),
                        12,
                        new Date(1995, 1, 1),
                        ""),
                new FilmsRentedEvent("customer1", singletonList("film1"), 3))
                .when(new ReturnFilmsCommand("customer1", singletonList("film1")))
                .expectEvents(new FilmsReturnedEvent("customer1", singletonList("film1")));
    }
}
