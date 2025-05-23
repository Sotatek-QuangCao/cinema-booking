package com.quangcd.cinemaproject.dto.request.movie;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UpdateMovieRequest implements Serializable {

    @NotNull(message = "field.required")
    private long movieId;

    @NotBlank(message = "field.required")
    private String title;

    @NotBlank(message = "field.required")
    private String description;

    @NotBlank( message = "field.required")
    private String mainGenre;

    @NotNull(message = "field.required")
    private Date releaseDate;

    @NotNull(message = "field.required")
    private String posterUrl;

    @NotNull(message = "field.required")
    @Min(value = 1, message = "greater.than.zero")
    private Integer duration;

    @NotBlank(message = "field.required")
    private String director;

    @NotBlank(message = "field.required")
    private String rating;

    @NotBlank(message = "field.required")
    private String rateVote;

    @NotBlank(message = "field.required")
    private String trailerUrl;

    private List<String> genres;
}
