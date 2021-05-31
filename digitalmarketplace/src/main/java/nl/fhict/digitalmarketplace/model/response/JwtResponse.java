package nl.fhict.digitalmarketplace.model.response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Integer id;
    private LocalDateTime refreshTokenExp;

    public JwtResponse() {
    }

    public JwtResponse(String token, String refreshToken, Integer id, LocalDateTime refreshTokenExp) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.refreshTokenExp = refreshTokenExp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getRefreshTokenExp() {
        return refreshTokenExp;
    }

    public void setRefreshTokenExp(LocalDateTime refreshTokenExp) {
        this.refreshTokenExp = refreshTokenExp;
    }
}
