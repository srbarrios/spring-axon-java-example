package com.casumo.test.videostore.repository;

import com.casumo.test.videostore.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Film, String> {
}
