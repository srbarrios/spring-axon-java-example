package com.casumo.test.videostore.aggregate;

import com.casumo.test.videostore.constants.FilmFormat;
import com.casumo.test.videostore.constants.FilmType;
import com.casumo.test.videostore.coreapi.CreateFilmCommand;
import com.casumo.test.videostore.coreapi.FilmCreatedEvent;
import com.casumo.test.videostore.coreapi.FilmRemovedEvent;
import com.casumo.test.videostore.coreapi.RemoveFilmCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;
import java.util.List;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class FilmAggregate {

    @AggregateIdentifier
    private String filmId;
    private String title;
    private FilmType type;
    private FilmFormat format;
    private String genre;
    private List<String> languages;
    private Integer minimumAge;
    private Date releaseDate;
    private String description;
    private CustomerAggregate renter;

    @SuppressWarnings("unused")
    public FilmAggregate() {
    }

    @CommandHandler
    public FilmAggregate(CreateFilmCommand cmd) {
        apply(new FilmCreatedEvent(
                cmd.getFilmId(),
                cmd.getTitle(),
                cmd.getType(),
                cmd.getFormat(),
                cmd.getGenre(),
                cmd.getLanguages(),
                cmd.getMinimumAge(),
                cmd.getReleaseDate(),
                cmd.getDescription()
        ));
    }

    @CommandHandler
    public void handle(RemoveFilmCommand cmd) {
        apply(new FilmRemovedEvent(
                cmd.getFilmId()
        ));
    }

    @EventSourcingHandler
    protected void on(FilmCreatedEvent event) {
        this.filmId = event.getFilmId();
        this.type = event.getType();
        this.title = event.getTitle();
        this.format = event.getFormat();
        this.genre = event.getGenre();
        this.languages = event.getLanguages();
        this.minimumAge = event.getMinimumAge();
        this.description = event.getDescription();
        this.releaseDate = event.getReleaseDate();
    }

}
