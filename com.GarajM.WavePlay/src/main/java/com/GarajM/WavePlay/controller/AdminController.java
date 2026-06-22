package com.GarajM.WavePlay.controller;

import com.GarajM.WavePlay.dto.response.SongResponse;
import com.GarajM.WavePlay.service.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    @Autowired
    private SongService songService;

    @PostMapping("/addSong")
    public ResponseEntity<SongResponse>

}
