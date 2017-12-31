package com.casumo.test.videostore.listener;

import com.casumo.test.videostore.coreapi.FilmCreatedEvent;
import com.casumo.test.videostore.coreapi.FilmRemovedEvent;
import com.casumo.test.videostore.entity.Film;
import com.casumo.test.videostore.repository.FilmRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmEventHandler {

    private final FilmRepository repository;

    public FilmEventHandler(FilmRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    protected void on(FilmCreatedEvent event) {
        repository.save(new Film(event.getFilmId(),
                event.getTitle(),
                event.getType(),
                event.getFormat(),
                event.getGenre(),
                event.getLanguages(),
                event.getMinimumAge(),
                event.getReleaseDate(),
                event.getDescription()));
    }

    @EventHandler
    protected void on(FilmRemovedEvent event) {
        repository.delete(event.getFilmId());
    }
}
