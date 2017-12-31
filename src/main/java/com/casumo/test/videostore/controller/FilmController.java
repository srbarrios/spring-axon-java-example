package com.casumo.test.videostore.controller;

import com.casumo.test.videostore.callback.LoggingCallback;
import com.casumo.test.videostore.controller.dto.FilmDto;
import com.casumo.test.videostore.coreapi.CreateFilmCommand;
import com.casumo.test.videostore.coreapi.RemoveFilmCommand;
import com.casumo.test.videostore.entity.Film;
import com.casumo.test.videostore.repository.FilmRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
public class FilmController {

    private final FilmRepository repository;

    @Autowired
    private CommandGateway commandGateway;

    public FilmController(FilmRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/films")
    public @ResponseBody
    ResponseEntity<List<Film>> getFilms() {
        List<Film> films = repository.findAll();
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @GetMapping("/film/{filmId}")
    public @ResponseBody
    ResponseEntity<Film> getFilm(@PathVariable String filmId) {
        Film film = repository.findOne(filmId);
        if (film != null) {
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/film")
    public @ResponseBody
    ResponseEntity<String> createFilm(@RequestBody FilmDto film) {
        String filmId = UUID.randomUUID().toString();
        commandGateway.send(
                new CreateFilmCommand(
                        filmId,
                        film.getTitle(),
                        film.getType(),
                        film.getFormat(),
                        film.getGenre(),
                        film.getLanguages(),
                        film.getMinimumAge(),
                        film.getReleaseDate(),
                        film.getDescription()
                ),
                LoggingCallback.INSTANCE);
        return new ResponseEntity<>(filmId, HttpStatus.CREATED); //TODO: Check when creation fails
    }

    @DeleteMapping("/film/{filmId}")
    public ResponseEntity<String> deleteFilm(@PathVariable String filmId) {
        commandGateway.send(new RemoveFilmCommand(filmId), LoggingCallback.INSTANCE);
        return new ResponseEntity<>(filmId, HttpStatus.NO_CONTENT);
    }


}
