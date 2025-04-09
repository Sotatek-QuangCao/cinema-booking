package com.quangcd.cinemaproject.service.moviegenre;

import com.quangcd.cinemaproject.dto.MovieGenreDto;
import com.quangcd.cinemaproject.entity.Genre;
import com.quangcd.cinemaproject.entity.MovieGenre;
import com.quangcd.cinemaproject.repository.GenreRepository;
import com.quangcd.cinemaproject.repository.MovieGenreRepository;
import com.quangcd.cinemaproject.service.genre.GenreService;
import com.quangcd.cinemaproject.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
//@RequiredArgsConstructor
public class MovieGenreServiceImpl implements MovieGenreService {

    private final MovieGenreRepository movieGenreRepository;
    private final GenreService genreService;
    private final MovieService movieService;

    public MovieGenreServiceImpl(MovieGenreRepository movieGenreRepository,
                                 @Lazy GenreService genreService,
                                 @Lazy MovieService movieService) {
        this.movieGenreRepository = movieGenreRepository;
        this.genreService = genreService;
        this.movieService = movieService;
    }

    @Override
    public List<MovieGenreDto> findAllMovieGenre() {
        List<MovieGenre> list = movieGenreRepository.findAll();
        List<MovieGenreDto> result = new ArrayList<>();

        result = list.stream()
                .map(movieGenre -> new MovieGenreDto(movieGenre.getId(),
                        movieService.findMovieById(movieGenre.getMovieId())
                        , new ArrayList<>()))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public MovieGenreDto getDetailMovieById(Long movieId) {
        MovieGenreDto result = MovieGenreDto.builder()
                .movie(movieService.findMovieById(movieId))
                .genre(movieGenreRepository.findByMovieId(movieId).stream()
                        .map(movieGenre -> genreService.findGenreById(movieGenre.getGenreId()))
                        .map(Genre::getName)
                        .collect(Collectors.toList()))
                .build();

        return result;
    }

    @Override
    public void addDataFromScapt(List<String> genres, Long id) {
        genreService.addAll(genres);
        for (String genre : genres) {
            movieGenreRepository.save(MovieGenre.builder()
                    .movieId(id)
                    .genreId(genreService.findGenreByCode(genre).getId())
                    .build());
        }

    }

    @Override
    public void saveAll(List<MovieGenre> movieGenres) {
        movieGenreRepository.saveAll(movieGenres);
    }

    @Override
    public void updateAll(List<MovieGenre> movieGenres) {
        Long movieId = movieGenres.get(0).getMovieId();
        List<MovieGenre> existingList = movieGenreRepository.findByMovieId(movieId);

        Set<Long> newGenreId = movieGenres.stream()
                .map(MovieGenre::getGenreId)
                .collect(Collectors.toSet());
        List<MovieGenre> toUpdate = new ArrayList<>();

        existingList.forEach(movieGenre -> {
            if (!newGenreId.contains(movieGenre.getGenreId())) {
                movieGenre.setDeleted(true);
                movieGenreRepository.save(movieGenre);
                toUpdate.add(movieGenre);
            } else if(newGenreId.contains(movieGenre.getGenreId())){
                movieGenre.setDeleted(false);
                movieGenreRepository.save(movieGenre);
                newGenreId.remove(movieGenre.getGenreId());
                toUpdate.add(movieGenre);
            }
        });

        if (!toUpdate.isEmpty()) {
            movieGenreRepository.saveAll(toUpdate);
        }

        List<MovieGenre> addNewInDb = newGenreId.stream()
                .map(genreId -> MovieGenre.builder()
                        .genreId(genreId)
                        .movieId(movieId)
                        .build())
                .toList();
        movieGenreRepository.saveAll(addNewInDb);
    }
}
