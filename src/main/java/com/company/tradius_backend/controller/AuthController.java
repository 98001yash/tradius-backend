package com.company.tradius_backend.controller;


import com.company.tradius_backend.dtos.AuthResponse;
import com.company.tradius_backend.dtos.LoginRequest;
import com.company.tradius_backend.dtos.SignupRequest;
import com.company.tradius_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request
            ){
        log.info("Received request for email: {}",request.getEmail());

        AuthResponse response = authService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        log.info("Received login request for email: {}", request.getEmail());

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
