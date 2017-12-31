package com.casumo.test.videostore.acceptance;

import com.casumo.test.videostore.aggregate.FilmAggregate;
import com.casumo.test.videostore.coreapi.CreateFilmCommand;
import com.casumo.test.videostore.coreapi.FilmCreatedEvent;
import com.casumo.test.videostore.coreapi.FilmRemovedEvent;
import com.casumo.test.videostore.coreapi.RemoveFilmCommand;
import org.axonframework.eventsourcing.eventstore.EventStoreException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import static com.casumo.test.videostore.acceptance.VideoStoreTestConstants.matrixFilm;

public class FilmTest {

    private FixtureConfiguration<FilmAggregate> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(FilmAggregate.class);
    }

    @Test
    public void testFilmCreated() throws Exception {
        fixture.givenNoPriorActivity()
                .when(new CreateFilmCommand(
                        matrixFilm.getFilmId(),
                        matrixFilm.getTitle(),
                        matrixFilm.getType(),
                        matrixFilm.getFormat(),
                        matrixFilm.getGenre(),
                        matrixFilm.getLanguages(),
                        matrixFilm.getMinimumAge(),
                        matrixFilm.getReleaseDate(),
                        matrixFilm.getDescription()))
                .expectEvents(new FilmCreatedEvent(
                        matrixFilm.getFilmId(),
                        matrixFilm.getTitle(),
                        matrixFilm.getType(),
                        matrixFilm.getFormat(),
                        matrixFilm.getGenre(),
                        matrixFilm.getLanguages(),
                        matrixFilm.getMinimumAge(),
                        matrixFilm.getReleaseDate(),
                        matrixFilm.getDescription()));
    }

    @Test
    public void testUserCantCreateSameFilmTwice() throws Exception {
        fixture.given(new FilmCreatedEvent(
                matrixFilm.getFilmId(),
                matrixFilm.getTitle(),
                matrixFilm.getType(),
                matrixFilm.getFormat(),
                matrixFilm.getGenre(),
                matrixFilm.getLanguages(),
                matrixFilm.getMinimumAge(),
                matrixFilm.getReleaseDate(),
                matrixFilm.getDescription()))
                .when(new CreateFilmCommand(
                        matrixFilm.getFilmId(),
                        matrixFilm.getTitle(),
                        matrixFilm.getType(),
                        matrixFilm.getFormat(),
                        matrixFilm.getGenre(),
                        matrixFilm.getLanguages(),
                        matrixFilm.getMinimumAge(),
                        matrixFilm.getReleaseDate(),
                        matrixFilm.getDescription()))
                .expectEvents(new FilmCreatedEvent(
                        matrixFilm.getFilmId(),
                        matrixFilm.getTitle(),
                        matrixFilm.getType(),
                        matrixFilm.getFormat(),
                        matrixFilm.getGenre(),
                        matrixFilm.getLanguages(),
                        matrixFilm.getMinimumAge(),
                        matrixFilm.getReleaseDate(),
                        matrixFilm.getDescription()))
                .expectException(EventStoreException.class);
    }

    @Test
    public void testFilmRemoved() throws Exception {
        fixture.givenCommands(new CreateFilmCommand(
                matrixFilm.getFilmId(),
                matrixFilm.getTitle(),
                matrixFilm.getType(),
                matrixFilm.getFormat(),
                matrixFilm.getGenre(),
                matrixFilm.getLanguages(),
                matrixFilm.getMinimumAge(),
                matrixFilm.getReleaseDate(),
                matrixFilm.getDescription()))
                .when(new RemoveFilmCommand(
                        matrixFilm.getFilmId()))
                .expectEvents(new FilmRemovedEvent(
                        matrixFilm.getFilmId()));
    }

}
