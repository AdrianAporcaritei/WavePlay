package com.GarajM.WavePlay.serviceImpl;

import com.GarajM.WavePlay.Repository.AppUserRepository;
import com.GarajM.WavePlay.Repository.PlaylistSongRepository;
import com.GarajM.WavePlay.Repository.SongRepository;
import com.GarajM.WavePlay.dto.request.AppUserRequest;
import com.GarajM.WavePlay.dto.request.SongRequest;
import com.GarajM.WavePlay.dto.response.SongResponse;
import com.GarajM.WavePlay.entity.AppUser;
import com.GarajM.WavePlay.entity.Song;
import com.GarajM.WavePlay.service.SongService;
import com.GarajM.WavePlay.util.FileHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class SongServiceImp implements SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Autowired
    private FileHandlerUtil fileHandlerUtil;

    //GenericGeminiService

    @Value("${app.base.url}")
    private String baseurl;


    @Override
    public SongResponse addSong(SongRequest request, MultipartFile songFile, MultipartFile imageFile, String email) {
        AppUser appUser = getUserByEmail(email);
        String uniqueId = UUID.randomUUID().toString();

        Song song = new Song();
        song.setAppUser(appUser);
        updateSongMetadata(song, request);

        String songUrl = processSongFile(songFile, uniqueId);
        song.setSongUrl(songUrl);

        String imageUrl = processImageFile(imageFile, uniqueId);
        song.setImageUrl(imageUrl);

        Song savedSong = songRepository.save(song);

        return  SongResponse.fromEntity(savedSong, baseurl);

    }



    private void updateSongMetadata(Song song, SongRequest request) {
        song.setTitle(request.getTitle());
        song.setArtist(request.getArtist());
    }

    private AppUser  getUserByEmail(String email){
        return appUserRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
    }

    private String processSongFile(MultipartFile songFile, String uniqueId) {
        String songExtention = fileHandlerUtil.getFileExtension(songFile.getOriginalFilename());
        String songFilename = uniqueId + songExtention;
        fileHandlerUtil.saveSongFileWithName(songFile, songFilename);
        return "/api/file/song" + songFilename;
    }

    private String processImageFile(MultipartFile imageFile, String uniqueId) {
        if(imageFile == null || imageFile.isEmpty()){
            return null;
        }
        String imageExtension = fileHandlerUtil.getFileExtension(imageFile.getOriginalFilename());

        String  imageFileName = uniqueId + imageExtension;
        fileHandlerUtil.saveImageFileWithName(imageFile, imageFileName);
        return "api/file/image" + imageFileName;

    }

}
