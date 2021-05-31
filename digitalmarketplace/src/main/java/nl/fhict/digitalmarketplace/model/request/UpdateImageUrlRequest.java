package nl.fhict.digitalmarketplace.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Valid
public class UpdateImageUrlRequest {
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String imageUrl;

    public UpdateImageUrlRequest(String refreshToken, String imageUrl) {
        this.refreshToken = refreshToken;
        this.imageUrl = imageUrl;
    }

    public UpdateImageUrlRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
