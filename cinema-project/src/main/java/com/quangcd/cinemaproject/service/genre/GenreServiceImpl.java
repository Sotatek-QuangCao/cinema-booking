package com.quangcd.cinemaproject.service.genre;

import com.quangcd.cinemaproject.configuration.Translator;
import com.quangcd.cinemaproject.entity.Genre;
import com.quangcd.cinemaproject.exception.ResourceNotFoundException;
import com.quangcd.cinemaproject.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Genre findGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + genreId));
    }

    @Override
    public Genre findGenreByCode(String code) {
        return genreRepository.findByCode(code).orElseThrow(()
                -> new ResourceNotFoundException(
                        Translator.toLocale("resource.not.found",code)));
    }

    @Override
    public void addAll(List<String> genres) {
        for (String genre : genres) {
            if(genreRepository.existsByName(genre.toLowerCase())){
                log.error("Genre with name: " + genre + " already exists");
                continue;
            }
            genreRepository.save(Genre.builder().name(genre.toLowerCase()).build());
        }
    }

}
