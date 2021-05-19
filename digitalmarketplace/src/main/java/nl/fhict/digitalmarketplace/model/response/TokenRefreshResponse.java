package nl.fhict.digitalmarketplace.model.response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class TokenRefreshResponse {
    private String token;
    private String refreshToken;
    private LocalDateTime refreshTokenExp;

    public TokenRefreshResponse(String token, String refreshToken, LocalDateTime refreshTokenExp) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.refreshTokenExp = refreshTokenExp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getRefreshTokenExp() {
        return refreshTokenExp;
    }

    public void setRefreshTokenExp(LocalDateTime refreshTokenExp) {
        this.refreshTokenExp = refreshTokenExp;
    }
}
