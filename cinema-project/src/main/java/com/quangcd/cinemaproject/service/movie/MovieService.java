package com.quangcd.cinemaproject.service.movie;

import com.quangcd.cinemaproject.dto.MovieDto;
import com.quangcd.cinemaproject.dto.request.movie.CreateMovieRequest;
import com.quangcd.cinemaproject.dto.request.movie.SearchMovieRequest;
import com.quangcd.cinemaproject.dto.request.movie.UpdateMovieRequest;
import com.quangcd.cinemaproject.entity.Movie;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieService {
    Page<MovieDto> getAllMovies(SearchMovieRequest searchMovieRequest);

    Page<MovieDto> searchByCriteria(int pageNo, int pageSize, String sortBy, String... search);

    MovieDto findMovieById(Long id);

    void saveMovie(CreateMovieRequest movie);

    void updateMovie(UpdateMovieRequest movie);

    void deleteMovie(long id);

}
