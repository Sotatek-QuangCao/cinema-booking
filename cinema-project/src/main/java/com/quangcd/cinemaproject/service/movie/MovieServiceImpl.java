package com.quangcd.cinemaproject.service.movie;

import com.quangcd.cinemaproject.configuration.Translator;
import com.quangcd.cinemaproject.dto.GenreDto;
import com.quangcd.cinemaproject.dto.MovieDto;
import com.quangcd.cinemaproject.dto.request.movie.CreateMovieRequest;
import com.quangcd.cinemaproject.dto.request.movie.SearchMovieRequest;
import com.quangcd.cinemaproject.dto.request.movie.UpdateMovieRequest;
import com.quangcd.cinemaproject.entity.Genre;
import com.quangcd.cinemaproject.entity.Movie;
import com.quangcd.cinemaproject.entity.MovieGenre;
import com.quangcd.cinemaproject.exception.ResourceNotFoundException;
import com.quangcd.cinemaproject.repository.MovieRepository;
import com.quangcd.cinemaproject.repository.criteria.MovieCriteriaQueryConsumer;
import com.quangcd.cinemaproject.repository.criteria.MovieCriteriaSearch;
import com.quangcd.cinemaproject.service.genre.GenreService;
import com.quangcd.cinemaproject.service.moviegenre.MovieGenreService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;
    private final MovieGenreService movieGenreService;

    private final EntityManager entityManager;

    @Override
    public Page<MovieDto> getAllMovies(SearchMovieRequest searchMovieRequest) {

        Pageable pageable = PageRequest.of(searchMovieRequest.getPage(),
                searchMovieRequest.getSize());
        if (StringUtils.hasLength(searchMovieRequest.getSortOrder())) {
            Sort sort = searchMovieRequest.getSortOrder().toUpperCase().equals("DESC")
                    ? Sort.by(Sort.Direction.DESC, searchMovieRequest.getSortBy())
                    : Sort.by(Sort.Direction.ASC, searchMovieRequest.getSortBy());
            pageable = PageRequest.of(searchMovieRequest.getPage(),
                    searchMovieRequest.getSize(),
                    sort);
        }

        Page<Movie> movies = movieRepository.searchByTitleAndGenreByIsDeleted(searchMovieRequest.getTitle(),
                searchMovieRequest.getMainGenre(), false, pageable);
        return movies.map(movie ->
                new MovieDto(movie.getId(), movie.getTitle(), movie.getDescription()
                        , movie.getMainGenre(), movie.getReleaseDate(), movie.getPosterUrl()
                        , movie.getDuration(), movie.getDirector(), movie.getRating(),
                        movie.getRateVote(), movie.getTrailerUrl(), new ArrayList<>()));

    }

    @Override
    public Page<MovieDto> searchByCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        Pageable pageable = PageRequest.of(pageNo,
                pageSize);
        if (StringUtils.hasLength(sortBy)) {
            Sort sort = sortBy.toUpperCase().equals("DESC")
                    ? Sort.by(Sort.Direction.DESC, sortBy)
                    : Sort.by(Sort.Direction.ASC, sortBy);
            pageable = PageRequest.of(pageNo,
                    pageSize,
                    sort);
        }

        List<MovieCriteriaSearch> criteriaSearches = new ArrayList<>();
        if (search != null) {
            for (String s : search) {
                Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(.*)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaSearches.add(new MovieCriteriaSearch(matcher.group(1),
                            matcher.group(2), matcher.group(3)));
                }
            }
        }
        List<Movie> movies = getMovies(pageNo,pageSize,criteriaSearches,sortBy);

        return null;
    }

    private List<Movie> getMovies(int pageNo, int pageSize, List<MovieCriteriaSearch> criteriaSearches, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);
        //xu ly cac dk tim kiem

        Predicate predicate = criteriaBuilder.conjunction();
        MovieCriteriaQueryConsumer queryConsumer = new MovieCriteriaQueryConsumer(
                criteriaBuilder,root,predicate);

        criteriaSearches.forEach(queryConsumer);
        predicate =queryConsumer.getPredicate();

        query.where(predicate);

        return entityManager.createQuery(query)
                .setFirstResult(pageNo)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public MovieDto findMovieById(Long id) {
        // Tìm movie theo ID
        Optional<Movie> movieOptional = movieRepository.findById(id);

        // Kiểm tra xem movie có tồn tại không
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            // Chuyển đổi Movie sang MovieDto
            return MovieDto.builder()
                    .id(movie.getId())
                    .title(movie.getTitle())
                    .description(movie.getDescription())
                    .duration(movie.getDuration())
                    .director(movie.getDirector())
                    .mainGenre(movie.getMainGenre())
                    .posterUrl(movie.getPosterUrl())
                    .rateVote(movie.getRateVote())
                    .rating(movie.getRating())
                    .releaseDate(movie.getReleaseDate())
                    .trailerUrl(movie.getTrailerUrl())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public void saveMovie(CreateMovieRequest movieRequest) {
        Movie movie = Movie.builder()
                .title(movieRequest.getTitle())
                .description(movieRequest.getDescription())
                .duration(movieRequest.getDuration())
                .director(movieRequest.getDirector())
                .mainGenre(movieRequest.getMainGenre())
                .posterUrl(movieRequest.getPosterUrl())
                .rateVote(movieRequest.getRateVote())
                .rating(movieRequest.getRating())
                .trailerUrl(movieRequest.getTrailerUrl())
                .releaseDate((movieRequest.getReleaseDate()))
                .build();
        movieRepository.save(movie);
        log.info("Add movie success");
        List<MovieGenre> movieGenres = movieRequest.getGenres().stream()
                .map(genreCode -> {
                    Genre genre = genreService.findGenreByCode(genreCode);
                    log.info("Found genre: {}", genre);
                    return MovieGenre.builder()
                            .movieId(movie.getId())
                            .genreId(genre.getId())
                            .build();
                }).toList();

        movieGenreService.saveAll(movieGenres);
    }

    @Override
    public void updateMovie(UpdateMovieRequest movieRequest) {
        Movie movie = movieRepository.findById(movieRequest.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("resource.not.found", movieRequest.getTitle())));
        movie.setTitle(movieRequest.getTitle());
        movie.setDescription(movieRequest.getDescription());
        movie.setDuration(movieRequest.getDuration());
        movie.setDirector(movieRequest.getDirector());
        movie.setMainGenre(movieRequest.getMainGenre());
        movie.setPosterUrl(movieRequest.getPosterUrl());
        movie.setRateVote(movieRequest.getRateVote());
        movie.setRating(movieRequest.getRating());
        movie.setTrailerUrl(movieRequest.getTrailerUrl());
        movie.setReleaseDate(movieRequest.getReleaseDate());
        movieRepository.save(movie);

        log.info("Add movie success");
        List<MovieGenre> movieGenres = movieRequest.getGenres().stream()
                .map(genreCode -> {
                    Genre genre = genreService.findGenreByCode(genreCode);
                    log.info("Found genre: {}", genre);
                    return MovieGenre.builder()
                            .movieId(movie.getId())
                            .genreId(genre.getId())
                            .build();
                }).toList();

        movieGenreService.updateAll(movieGenres);
    }

    @Override
    public void deleteMovie(long id) {
        Movie movie = movieRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Translator.toLocale("resource.not.found", id)
                ));
        movie.setDeleted(true);
        movieRepository.save(movie);
    }


}
