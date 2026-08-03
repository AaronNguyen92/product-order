package com.dat.productorder.service;

import com.dat.productorder.entity.RefreshToken;
import com.dat.productorder.entity.User;
import com.dat.productorder.security.CustomUserDetails;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(CustomUserDetails userDetails);
    RefreshToken verifyRefreshToken(String token);
    void deleteRefreshToken(User user);
}
