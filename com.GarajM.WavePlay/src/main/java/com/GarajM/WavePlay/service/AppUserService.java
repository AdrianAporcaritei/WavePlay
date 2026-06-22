package com.GarajM.WavePlay.service;

import com.GarajM.WavePlay.dto.request.AppUserRequest;
import com.GarajM.WavePlay.dto.response.AppUserResponse;
import com.GarajM.WavePlay.dto.response.PaginatedResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.jspecify.annotations.Nullable;

public interface AppUserService {
    AppUserResponse getUserProfile(String email);

    AppUserResponse updateUserProfile(AppUserRequest request, String email);

    PaginatedResponse<AppUserResponse> getAllUsers(int page, int size);

    AppUserResponse updateUserRole(Long userId, String role, String email);
}
