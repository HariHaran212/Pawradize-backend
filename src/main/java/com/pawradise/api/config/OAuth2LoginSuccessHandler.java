package com.pawradise.api.config;

import com.pawradise.api.models.User;
import com.pawradise.api.service.JwtService;
import com.pawradise.api.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public OAuth2LoginSuccessHandler(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Get user details from the OAuth2 authentication object
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String firstName = oauthUser.getAttribute("given_name");
        String lastName = oauthUser.getAttribute("family_name");
        String avatarUrl = oauthUser.getAttribute("picture");

        // 2. Pass the detailed info to your service to find or create the user
        User user = userService.processOAuthPostLogin(email, firstName, lastName, avatarUrl);

        // Generate a JWT for this user
        String jwtToken = jwtService.generateToken(user.getUsername());

        // Redirect the user to your frontend, passing the token as a parameter
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", jwtToken)
                .build().toUriString();

        response.sendRedirect(targetUrl);
    }
}