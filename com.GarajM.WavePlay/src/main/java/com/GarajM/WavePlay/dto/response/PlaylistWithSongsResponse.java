package com.GarajM.WavePlay.dto.response;


import com.GarajM.WavePlay.entity.Playlist;
import com.GarajM.WavePlay.entity.PlaylistSong;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistWithSongsResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean isPublic;
    private String imageURL;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long appUserId;
    private String appUserName;
    private Integer songCount;
    private List<SongInPlaylistResponse> songs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SongInPlaylistResponse{
        private Long songId;
        private String title;
        private String artist;
        private String songUrl;
        private String imageUrl;
        private Integer position;
        private LocalDateTime addedAt;
        //inner class song created because it will be used only at this class
    }

    public static PlaylistWithSongsResponse fromEntity(Playlist playlist, List<PlaylistSong> playlistSongs, String baseUrl){
        PlaylistWithSongsResponse response = new PlaylistWithSongsResponse();
        response.setId(playlist.getId());
        response.setName(playlist.getName());
        response.setDescription(playlist.getDescription());
        response.setIsPublic(playlist.getIsPublic());
        response.setImageURL(playlist.getImageUrl() != null ? baseUrl + playlist.getImageUrl() : null);
        response.setCreatedAt(playlist.getCreatedAt());
        response.setUpdatedAt(playlist.getUpdatedAt());
        response.setAppUserId(playlist.getAppUser().getId());
        response.setAppUserName(playlist.getAppUser().getName());
        response.setSongCount(playlistSongs.size());

        //Lista de piese unde fiecare piese o setam cu proprietatile din entitatea playlistsong
        List<SongInPlaylistResponse> songs = playlistSongs.stream().map(ps ->{

            SongInPlaylistResponse songResponse = new SongInPlaylistResponse();
            songResponse.setSongId(ps.getSong().getId());
            songResponse.setTitle(ps.getSong().getTitle());
            songResponse.setArtist(ps.getSong().getArtist());
            songResponse.setSongUrl(ps.getSong().getSongUrl()!= null? baseUrl + ps.getSong().getSongUrl() : null );
            songResponse.setImageUrl(ps.getSong().getImageUrl()!=null? baseUrl + ps.getSong().getImageUrl() : null);
            songResponse.setPosition(ps.getPosition());
            songResponse.setAddedAt(ps.getAddedAt());
            return songResponse;

        }).collect(Collectors.toList());

        response.setSongs(songs);
        return response;
    }
}
