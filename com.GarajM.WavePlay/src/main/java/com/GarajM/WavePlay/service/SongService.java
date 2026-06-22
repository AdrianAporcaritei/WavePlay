package com.GarajM.WavePlay.service;

import com.GarajM.WavePlay.dto.request.SongRequest;
import com.GarajM.WavePlay.dto.response.SongResponse;
import org.springframework.web.multipart.MultipartFile;

public interface SongService {
    SongResponse addSong(SongRequest request, MultipartFile songFile, MultipartFile imageFile, String email);
}
