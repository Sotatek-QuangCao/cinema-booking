package com.quangcd.cinemaproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class GenreDto implements Serializable {
    @NotNull(message = "request.invalid")
    @NotBlank(message = "request.invalid")
    private String name;
    @NotNull(message = "request.invalid")
    @NotBlank(message = "request.invalid")
    private String code;
}
