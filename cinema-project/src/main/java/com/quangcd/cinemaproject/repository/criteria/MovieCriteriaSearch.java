package com.quangcd.cinemaproject.repository.criteria;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCriteriaSearch {
    private String key; //title , id, mainGenre,...
    private String operation; // =, < , >
    private Object value;

}
