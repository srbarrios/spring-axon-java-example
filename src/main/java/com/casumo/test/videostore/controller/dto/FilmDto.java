package com.casumo.test.videostore.controller.dto;

import com.casumo.test.videostore.constants.FilmFormat;
import com.casumo.test.videostore.constants.FilmType;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.List;

@MappedSuperclass
@Data
public class FilmDto {

    protected String title;

    protected FilmType type;

    protected FilmFormat format;

    protected String genre;

    @ElementCollection(targetClass = String.class)
    protected
    List<String> languages;

    protected Integer minimumAge;

    protected Date releaseDate;

    protected String description;


    public FilmDto() {
    }

    public FilmDto(String title, FilmType type, FilmFormat format, String genre, List<String> languages, Integer minimumAge, Date releaseDate, String description) {
        this.title = title;
        this.type = type;
        this.format = format;
        this.genre = genre;
        this.languages = languages;
        this.minimumAge = minimumAge;
        this.releaseDate = releaseDate;
        this.description = description;
    }
}
