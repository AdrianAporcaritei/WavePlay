package com.GarajM.WavePlay.dto.response;

import com.GarajM.WavePlay.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {

    private Long id;
    private String title;
    private String artist;
    private String songURL;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Long appUserId;
    private String appUserName;


    public static SongResponse fromEntity(Song song, String baseUrl){
        SongResponse response = new SongResponse();

        response.setId(song.getId());
        response.setTitle(song.getTitle());
        response.setArtist(song.getArtist());
        response.setSongURL(song.getSongUrl() != null ? song.getSongUrl() + baseUrl : null);
        response.setImageUrl(song.getImageUrl() != null ? song.getImageUrl() + baseUrl : null);
        response.setCreatedAt(song.getCreatedAt());
        response.setAppUserId(song.getAppUser().getId());
        response.setAppUserName(song.getAppUser().getName());
        return response;

    }

}
