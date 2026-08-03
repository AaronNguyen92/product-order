package com.dat.productorder.service.impl;

import com.dat.productorder.entity.RefreshToken;
import com.dat.productorder.entity.User;
import com.dat.productorder.exception.RefreshTokenException;
import com.dat.productorder.repository.RefreshTokenRepository;
import com.dat.productorder.security.CustomUserDetails;
import com.dat.productorder.security.jwt.JwtService;
import com.dat.productorder.service.RefreshTokenService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(CustomUserDetails userDetails) {

        User user = userDetails.getUser();

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        existingToken.ifPresent(refreshTokenRepository::delete);

        String token = jwtService.generateRefreshToken(userDetails);

        RefreshToken newRefreshToken  = RefreshToken.builder()
                .token(token).
                expiryDate(
                        LocalDateTime.now().plus(Duration.ofMillis(refreshExpiration))
                )
                .user(user)
                .build();

        return refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    @Transactional(noRollbackFor = RefreshTokenException.class)
    public RefreshToken verifyRefreshToken(String token){

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new RefreshTokenException("Refresh token not found"));

        if(LocalDateTime.now().isAfter(refreshToken.getExpiryDate())){

            refreshTokenRepository.delete(refreshToken);

            throw new RefreshTokenException("Refresh token expired");

        }

        return refreshToken;

    }

    @Override
    @Transactional
    public void deleteRefreshToken(User user){

        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

    }
}
