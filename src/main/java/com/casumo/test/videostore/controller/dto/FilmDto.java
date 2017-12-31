package com.casumo.test.videostore.controller.dto;

import com.casumo.test.videostore.constants.FilmFormat;
import com.casumo.test.videostore.constants.FilmType;

import javax.persistence.ElementCollection;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.List;

@MappedSuperclass
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FilmType getType() {
        return type;
    }

    public void setType(FilmType type) {
        this.type = type;
    }

    public FilmFormat getFormat() {
        return format;
    }

    public void setFormat(FilmFormat format) {
        this.format = format;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Integer getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(Integer minimumAge) {
        this.minimumAge = minimumAge;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
