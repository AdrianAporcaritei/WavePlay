package com.GarajM.WavePlay.serviceImpl;

import com.GarajM.WavePlay.Application;
import com.GarajM.WavePlay.Exception.*;
import com.GarajM.WavePlay.Repository.AppUserRepository;
import com.GarajM.WavePlay.dto.request.ForgotPasswordRequest;
import com.GarajM.WavePlay.dto.request.LoginUserRequest;
import com.GarajM.WavePlay.dto.request.RefreshTokenRequest;
import com.GarajM.WavePlay.dto.request.RegisterUserRequest;
import com.GarajM.WavePlay.dto.response.AppUserResponse;
import com.GarajM.WavePlay.dto.response.MessageResponse;
import com.GarajM.WavePlay.entity.AppUser;
import com.GarajM.WavePlay.service.AuthService;
import com.GarajM.WavePlay.service.EmailService;
import com.GarajM.WavePlay.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {


    private final AppUserRepository appUserRepository;


    private final JwtUtil jwtUtil;


    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public AuthServiceImpl(AppUserRepository appUserRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, EmailService emailService){
        this.appUserRepository = appUserRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;

    }


    @Override
    public MessageResponse registerUser(RegisterUserRequest request) {
        if(appUserRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        String tempPassword = generateTemporaryPassword();
        AppUser appUser  = new AppUser();
        appUser.setName(request.getName());
        appUser.setEmail(request.getEmail());
        appUser.setPassword(passwordEncoder.encode(tempPassword));
        appUser.setRole(request.getRole() != null? request.getRole(): "USER");

        appUserRepository.save(appUser);
        emailService.sendWelcomeEmail(appUser.getEmail(), appUser.getName(), tempPassword);
        return new MessageResponse("Account created successfully. A temporary password has been sent to your email");
    }

    @Override
    public AppUserResponse loginUser(LoginUserRequest request) {
        AppUser appUser = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new InvalidCredentialsException("Invalid email or password"));

        if(!passwordEncoder.matches(request.getPassword(), appUser.getPassword())){
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateAccessToken(appUser.getId(), appUser.getName(), appUser.getEmail(), appUser.getRole());

        String refreshToken = jwtUtil.generateRefreshToken(appUser.getId(), appUser.getEmail());

        appUser.setRefreshToken(refreshToken);
        appUserRepository.save(appUser);

        return AppUserResponse.fromEntity(appUser, accessToken, refreshToken);
    }

    @Override
    public AppUserResponse refreshAccessToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        String email = jwtUtil.extractEmail(refreshToken);

        if(!jwtUtil.isAccessToken(refreshToken)){
            throw new InvalidTokenException("Invalid token exception");
        }

        AppUser appUser = appUserRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new InvalidTokenException("Invalid refresh token"));

        if(!jwtUtil.validateToken(refreshToken, email)){
            throw new TokenExpiredException("Refresh token expired or invalid");
        }
        String newAccessToken = jwtUtil.generateAccessToken(appUser.getId(), appUser.getName(), appUser.getEmail(), appUser.getRole());

        return AppUserResponse.fromEntity(appUser, newAccessToken, refreshToken);
    }

    @Override
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
         AppUser appUser = appUserRepository.findByEmail(request.getEmail())
                 .orElseThrow(()->new ResourceNotFoundException("User not found with email: "  + request.getEmail()));
         String tempPassword = generateTemporaryPassword();

         appUser.setPassword(passwordEncoder.encode(tempPassword));
         appUserRepository.save(appUser);

         emailService.sendCredentialsEmail(appUser.getEmail(), appUser.getName(), tempPassword);

         return new MessageResponse("Temporary password has been sent to your email");
    }

    private String generateTemporaryPassword(){
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(10);

        for(int i=0; i<10; i++){
            password.append(chars.charAt(random.nextInt(chars.length())));

        }
        return password.toString();
    }
}