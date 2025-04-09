package com.quangcd.cinemaproject.dto.request.movie;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SearchMovieRequest implements Serializable {
    String title,mainGenre;

    @NotNull(message = "request.invalid")
    @Min(value = 0, message = "greater.than.zero")
    int page;
    @NotNull(message = "request.invalid")
    @Min(value = 0, message = "greater.than.zero")
    int size;

    String sortBy;
    String sortOrder;

}
