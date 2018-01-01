package com.casumo.test.videostore.entity;

import com.casumo.test.videostore.constants.FilmFormat;
import com.casumo.test.videostore.constants.FilmType;
import com.casumo.test.videostore.controller.dto.FilmDto;
import com.casumo.test.videostore.coreapi.CreateFilmCommand;
import com.casumo.test.videostore.coreapi.FilmCreatedEvent;
import com.casumo.test.videostore.coreapi.FilmRemovedEvent;
import com.casumo.test.videostore.coreapi.RemoveFilmCommand;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Entity
@Data
public class Film extends FilmDto {

    @Id
    private String filmId;

    @ManyToOne(cascade = CascadeType.DETACH, targetEntity = Customer.class)
    private Customer renter;

    public Film() {
    }

    public Film(String filmId, String title, FilmType type, FilmFormat format, String genre, List<String> languages, Integer minimumAge,
                Date releaseDate, String description) {
        this.filmId = filmId;
        this.title = title;
        this.type = type;
        this.format = format;
        this.genre = genre;
        this.languages = languages;
        this.minimumAge = minimumAge;
        this.releaseDate = releaseDate;
        this.description = description;
        this.renter = null;
    }


    @CommandHandler
    public Film(CreateFilmCommand cmd) {
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
    public Film(RemoveFilmCommand cmd) {
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
