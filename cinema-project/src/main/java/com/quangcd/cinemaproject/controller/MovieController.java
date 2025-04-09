package com.quangcd.cinemaproject.controller;


import com.quangcd.cinemaproject.configuration.Translator;
import com.quangcd.cinemaproject.dto.MovieDto;
import com.quangcd.cinemaproject.dto.request.movie.CreateMovieRequest;
import com.quangcd.cinemaproject.dto.request.movie.SearchMovieRequest;
import com.quangcd.cinemaproject.dto.request.movie.UpdateMovieRequest;
import com.quangcd.cinemaproject.dto.response.BaseResponse;
import com.quangcd.cinemaproject.service.movie.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/movie")
@RequiredArgsConstructor
@Validated

public class MovieController {

    private final MovieService movieService;

    @PostMapping("/search")
    @Operation(summary = " Search movie", description = "")
    public ResponseEntity<?> search(@Valid @RequestBody SearchMovieRequest searchMovieRequest) {
        try {

            Page<MovieDto> list = movieService.getAllMovies(searchMovieRequest);
            log.info("Search movies successful");
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(200)
                    .message(Translator.toLocale("success.notification"))
                    .data(list)
                    .build());

        } catch (Exception e) {
            log.error("errMessage={}", e.getMessage(), e.getCause());
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }
    @GetMapping("/advanced-search")
    @Operation(summary = " Search movie", description = "")
    public ResponseEntity<?> advancedSearchByCriteria(
            @RequestParam(defaultValue = "0",required = false) int pageNo,
            @RequestParam(defaultValue = "5",required = false) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String... search) {
        try {

            Page<MovieDto> list = movieService.searchByCriteria(pageNo,pageSize,sortBy,search);
            log.info("Search movies successful");
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(200)
                    .message(Translator.toLocale("success.notification"))
                    .data(list)
                    .build());

        } catch (Exception e) {
            log.error("errMessage={}", e.getMessage(), e.getCause());
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/add")
    @Operation(summary = " Add movie", description = "")
    public ResponseEntity<?> addMovie(@Valid @RequestBody CreateMovieRequest movie) {

        try {
            movieService.saveMovie(movie);
            log.info(" Add successful");
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(200)
                    .message(Translator.toLocale("success.notification"))
                    .build());
        } catch (Exception e) {
            log.error("errMessage={}", e.getMessage(), e.getCause());
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/update")
    @Operation(summary = " Update movie", description = "")
    public ResponseEntity<?> updateMovie(@Valid @RequestBody UpdateMovieRequest movie) {

        try {
            movieService.updateMovie(movie);
            log.info("Update movies successful");
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(200)
                    .message(Translator.toLocale("success.notification"))
                    .build());
        } catch (Exception e) {
            log.error("errMessage={}", e.getMessage(), e.getCause());
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = " Delete movie", description = "")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") long id) {

        try {
            movieService.deleteMovie(id);
            log.info("Delete movies successful");
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(200)
                    .message(Translator.toLocale("success.notification"))
                    .build());
        } catch (Exception e) {
            log.error("errMessage={}", e.getMessage(), e.getCause());
            return ResponseEntity.ok(BaseResponse.builder()
                    .code(400)
                    .message(e.getMessage())
                    .build());
        }
    }

}
