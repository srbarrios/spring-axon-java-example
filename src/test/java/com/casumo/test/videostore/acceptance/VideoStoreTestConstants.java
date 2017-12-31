package com.casumo.test.videostore.acceptance;

import com.casumo.test.videostore.constants.FilmFormat;
import com.casumo.test.videostore.constants.FilmType;
import com.casumo.test.videostore.entity.Film;

import java.util.Date;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class VideoStoreTestConstants {
    public static final Film matrixFilm = new Film("1",
            "Matrix 11",
            FilmType.newFilm,
            FilmFormat.blueray,
            "ScyFi",
            asList("english", "swedish", "spanish"),
            16,
            new Date(2017, 9, 6),
            "Matrix description");
    public static final Film spiderManFilm = new Film("2",
            "Spider Man",
            FilmType.regular,
            FilmFormat.dvd,
            "Action",
            asList("english", "swedish", "spanish"),
            12,
            new Date(2009, 6, 7),
            "Spider Man description");
    public static final Film spiderMan2Film = new Film("3",
            "Spider Man 2",
            FilmType.regular,
            FilmFormat.dvd,
            "Action",
            asList("english", "swedish", "spanish"),
            12,
            new Date(2012, 12, 3),
            "Spider Man 2 description");
    public static final Film outOfAfricaFilm = new Film("4",
            "Out of Africa",
            FilmType.old,
            FilmFormat.vhs,
            "Drama",
            singletonList("english"),
            18,
            new Date(1990, 10, 23),
            "Out of Africa description");
}
