package com.GarajM.WavePlay.ServiceTest;

import com.GarajM.WavePlay.Repository.AppUserRepository;
import com.GarajM.WavePlay.dto.request.RegisterUserRequest;
import com.GarajM.WavePlay.dto.response.MessageResponse;
import com.GarajM.WavePlay.entity.AppUser;
import com.GarajM.WavePlay.service.EmailService;
import com.GarajM.WavePlay.serviceImpl.AuthServiceImpl;
import com.GarajM.WavePlay.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private  AppUserRepository appUserRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImpl authServiceIml;

    @Captor
    private ArgumentCaptor <AppUser> userCaptor;


    private RegisterUserRequest buildRequest (String email, String name, String role){
        RegisterUserRequest req = new RegisterUserRequest();
        req.setEmail(email);
        req.setName(name);
        req.setRole(role);

        return req;
    }

    @Test
    void registerUser_shouldCreateUserAndSendEmail_whenEmailIsNew(){
        //Arrange
        RegisterUserRequest request = buildRequest("ana@test.com", "Ana", "ADMIN");


        when(appUserRepository.existsByEmail("ana@test.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-password");

        // Act
        MessageResponse response = authServiceIml.registerUser(request);

        // Assert — mesajul returnat

        assertNotNull(response);
        assertEquals("Account created successfully. A temporary password has been sent to your email", response.getMessage());

        //Assert - userul are datele corecte salvate

        verify(appUserRepository).save(userCaptor.capture());
        AppUser saved= userCaptor.getValue();

        assertEquals("Ana", saved.getName());
        assertEquals("ana@test.com", saved.getEmail());
        assertEquals("hashed-password", saved.getPassword());
        assertEquals("ADMIN", saved.getRole());

        // Assert — emailul a fost trimis cu parametrii corecți
        verify(emailService).sendWelcomeEmail(
                eq("ana@test.com"),
                eq("Ana"),
                anyString()
        );


    }

    @Test
    void registerUser_shouldAssignDefaultRole_whenRoleIsNull() {
        // Arrange
        RegisterUserRequest request = buildRequest("ion@test.com", "Ion", null);

        when(appUserRepository.existsByEmail("ion@test.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");

        // Act
        authServiceIml.registerUser(request);

        // Assert — rolul salvat trebuie să fie "USER"
        verify(appUserRepository).save(userCaptor.capture());
        assertEquals("USER", userCaptor.getValue().getRole());
    }

}
