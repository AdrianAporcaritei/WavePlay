package com.GarajM.WavePlay.service;

import com.GarajM.WavePlay.dto.request.ForgotPasswordRequest;
import com.GarajM.WavePlay.dto.request.LoginUserRequest;
import com.GarajM.WavePlay.dto.request.RefreshTokenRequest;
import com.GarajM.WavePlay.dto.request.RegisterUserRequest;
import com.GarajM.WavePlay.dto.response.AppUserResponse;
import com.GarajM.WavePlay.dto.response.MessageResponse;
import jakarta.validation.Valid;


public interface AuthService {

    MessageResponse registerUser(RegisterUserRequest request);

    AppUserResponse loginUser(LoginUserRequest request);

    AppUserResponse refreshAccessToken(RefreshTokenRequest request);


    MessageResponse forgotPassword( ForgotPasswordRequest request);
}
