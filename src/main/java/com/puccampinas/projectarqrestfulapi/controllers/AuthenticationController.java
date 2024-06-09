package com.puccampinas.projectarqrestfulapi.controllers;

import com.puccampinas.projectarqrestfulapi.dtos.api.ApiResponse;
import com.puccampinas.projectarqrestfulapi.dtos.auth.LoginDTO;
import com.puccampinas.projectarqrestfulapi.dtos.auth.SignUpDTO;
import com.puccampinas.projectarqrestfulapi.dtos.auth.TokenDTO;
import com.puccampinas.projectarqrestfulapi.repositories.RefreshTokenRepository;
import com.puccampinas.projectarqrestfulapi.repositories.UserRepository;
import com.puccampinas.projectarqrestfulapi.services.UserService;
import com.puccampinas.projectarqrestfulapi.services.auth.TokenService;
import com.puccampinas.projectarqrestfulapi.domain.user.RefreshToken;
import com.puccampinas.projectarqrestfulapi.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenService tokenService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDTO>> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {
        Authentication authentication = authenticate(loginRequest);
        User user = getUserFromAuthentication(authentication);

        if(user == null) return generateNotFoundResponse();

        RefreshToken refreshToken = createAndSaveRefreshToken(user);

        String accessToken = tokenService.generateAccessToken(user);
        String refreshTokenString = tokenService.generateRefreshToken(user, refreshToken);

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshTokenString);

        return generateOkResponse("Login successful", tokenDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<TokenDTO>> registerUser(@Valid @RequestBody SignUpDTO signUpRequest) {
        User user = createUser(signUpRequest);
        userRepository.save(user);

        RefreshToken refreshToken = createAndSaveRefreshToken(user);

        String accessToken = tokenService.generateAccessToken(user);
        String refreshTokenString = tokenService.generateRefreshToken(user, refreshToken);

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshTokenString);
        return generateCreatedResponse("User registered successfully", tokenDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody TokenDTO tokenRequest) {
        String refreshTokenString = tokenRequest.getRefreshToken();
        if (isRefreshTokenValidAndExists(refreshTokenString)) {
            refreshTokenRepository.deleteById(tokenService.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutUserFromAllDevices(@RequestBody TokenDTO tokenRequest) {
        String refreshTokenString = tokenRequest.getRefreshToken();
        if (isRefreshTokenValidAndExists(refreshTokenString)) {
            refreshTokenRepository.deleteByOwner_Id(tokenService.getUserIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("/access-token")
    public ResponseEntity<?> generateAccessToken(@RequestBody TokenDTO tokenRequest) {
        String refreshTokenString = tokenRequest.getRefreshToken();
        if (isRefreshTokenValidAndExists(refreshTokenString)) {
            User user = userService.findUserById(tokenService.getUserIdFromRefreshToken(refreshTokenString));
            String accessToken = tokenService.generateAccessToken(user);

            return ResponseEntity.ok(new TokenDTO(accessToken, refreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshUserToken(@RequestBody TokenDTO tokenRequest) {
        String refreshTokenString = tokenRequest.getRefreshToken();
        if (isRefreshTokenValidAndExists(refreshTokenString)) {
            refreshTokenRepository.deleteById(tokenService.getTokenIdFromRefreshToken(refreshTokenString));

            User user = userService.findUserById(tokenService.getUserIdFromRefreshToken(refreshTokenString));

            RefreshToken refreshToken = createAndSaveRefreshToken(user);

            String accessToken = tokenService.generateAccessToken(user);
            String newRefreshTokenString = tokenService.generateRefreshToken(user, refreshToken);

            return ResponseEntity.ok(new TokenDTO(accessToken, newRefreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }

    private Authentication authenticate(LoginDTO loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );
    }

    private User getUserFromAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (User) authentication.getPrincipal();
    }

    private ResponseEntity<ApiResponse<TokenDTO>> generateNotFoundResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(HttpStatus.NOT_FOUND, "User not found", null));
    }

    private RefreshToken createAndSaveRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(user);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private ResponseEntity<ApiResponse<TokenDTO>> generateOkResponse(String message, TokenDTO tokenDTO) {
        return ResponseEntity
                .ok(new ApiResponse<>(HttpStatus.OK, message, tokenDTO));
    }

    private User createUser(SignUpDTO signUpRequest) {
        return new User(signUpRequest, passwordEncoder.encode(signUpRequest.getPassword()));
    }

    private ResponseEntity<ApiResponse<TokenDTO>> generateCreatedResponse(String message, TokenDTO tokenDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED, message, tokenDTO));
    }

    private boolean isRefreshTokenValidAndExists(String refreshTokenString) {
        return tokenService.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(tokenService.getTokenIdFromRefreshToken(refreshTokenString));
    }
}
