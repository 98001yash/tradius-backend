package com.company.tradius_backend.security;


import com.company.tradius_backend.entities.User;
import com.company.tradius_backend.enums.AuthProvider;
import com.company.tradius_backend.enums.Role;
import com.company.tradius_backend.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jjwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // extract user info from google
        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            log.error("OAuth login failed: email not provided by google");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found from OAuth provider");
            return;
        }
        log.error("OAuth login success for  email: {}", email);

        // find or create local user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("Creating new user via google OAuth: {}", email);

                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setRole(Role.USER);
                    newUser.setAuthProvider(AuthProvider.GOOGLE);
                    newUser.setEnabled(true);

                    newUser.setPassword("OAUTH_USER");
                    return userRepository.save(newUser);
                });

        // prevent google login for LOCALE_USER
        if (user.getAuthProvider() == AuthProvider.LOCAL) {
            log.warn("OAuth login attempted for LOCAL user: {}", email);
            response.sendError(
                    HttpServletResponse.SC_CONFLICT,
                    "This email is registered with password logic"
            );
            return;
        }
        // generate JWT token
        String token = jjwtService.generateToken(
                user.getId().toString(),
                user.getEmail(),
                user.getRole().name()
        );

        // send token to frontend
        response.sendRedirect(
                "http://localhost:3000/oauth-success?token=" + token
        );
    }
}
