package com.quangcd.cinemaproject.dto;

import com.quangcd.cinemaproject.entity.Genre;
import com.quangcd.cinemaproject.entity.Movie;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieGenreDto {

    private Long id;
    private MovieDto movie;
    private List<String> genre;
}
