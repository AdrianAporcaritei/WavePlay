package com.GarajM.WavePlay.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SongRequest {

    @NotBlank(message = "Title is required")
    @Size(max=100, message = "Title must not exceed 100 characters")
    private String title;


    @NotBlank(message = "Artist is required")
    @Size(max=100, message = "Artist must not exceed 100 characters")
    private String artist;

}
