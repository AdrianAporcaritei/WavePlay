package com.GarajM.WavePlay.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistRequest {

    @NotBlank(message = "Playlist name is required")
    @Size(max=100, message = "Playlist name must not exceed 100 characters")
    private String name;

    @Size(max=500, message = "Description most not exceed 500 characters")
    private String description;

    private Boolean isPublic;
}
