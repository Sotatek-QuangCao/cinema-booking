package com.quangcd.cinemaproject.service.genre;

import com.quangcd.cinemaproject.entity.Genre;

import java.util.List;

public interface GenreService {
    Genre findGenreById(Long genreId);
    Genre findGenreByCode(String code);

    void addAll(List<String> genres);
}
