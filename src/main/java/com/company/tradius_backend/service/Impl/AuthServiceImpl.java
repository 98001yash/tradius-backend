package com.company.tradius_backend.service.Impl;


import com.company.tradius_backend.dtos.AuthResponse;
import com.company.tradius_backend.dtos.LoginRequest;
import com.company.tradius_backend.dtos.SignupRequest;
import com.company.tradius_backend.entities.User;
import com.company.tradius_backend.enums.Role;
import com.company.tradius_backend.exceptions.ResourceNotFoundException;
import com.company.tradius_backend.exceptions.RuntimeConflictException;
import com.company.tradius_backend.repository.UserRepository;
import com.company.tradius_backend.security.JwtService;
import com.company.tradius_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public AuthResponse signup(SignupRequest request) {
        log.info("Signup attempt for email={}",request.getEmail());

        // check if email is already exists
        if(userRepository.existsByEmail(request.getEmail())){
            log.warn("Signup failed: email already reqistered -> {}", request.getEmail());
            throw new RuntimeConflictException("Email already registered.");
        }
        User user = modelMapper.map(request, User.class);

        // set fields
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        // save user
        User savedUser = userRepository.save(user);

        log.info("User registered successfully with id: {}",savedUser.getId());

        // generate JWT
        String token = jwtService.generateToken(
                savedUser.getId().toString(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
        // return response
        return new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole(),
                token
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email={}",request.getEmail());

        // find User
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->{
                    log.warn("Login failed user not found -> {}",request.getEmail());
                    return new ResourceNotFoundException("Invalid email or password");
                });

        //check if account is enabled
        if(!user.isEnabled()){
            log.warn("Login blocked: disabled account-> {}",request.getEmail());
            throw new RuntimeConflictException("Account is disabled");
        }

        // verify password
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            log.warn("Login failed: onvalid password -> {}",request.getEmail());
            throw new ResourceNotFoundException("Invalid email or password");
        }
        log.info("Login successful for userId={}",user.getId());

        // generate JWT
        String token = jwtService.generateToken(
                user.getId().toString(),
                user.getEmail(),
                user.getRole().name()
        );

        // return Response
        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}
