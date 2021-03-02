package nl.fhict.digitalmarketplace.model.product;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Entity
public class VideoGame extends Product{
    private String releaseDate;
    @Valid
    @ManyToMany(targetEntity = Genre.class, cascade = CascadeType.REMOVE)
    @JoinTable(name = "PRODUCT_GENRE", joinColumns = {@JoinColumn(name = "product_id")}, inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private List<Genre> genres;

    public VideoGame() {
    }

    public VideoGame(@NotBlank String name, @Valid ProductPlatform productPlatform, @PositiveOrZero Integer quantity, @PositiveOrZero double price, @NotBlank String description, @NotBlank String imageUrl, @NotBlank String systemRequirements, Boolean isActive, String releaseDate, @Valid List<Genre> genres) {
        super(name, productPlatform, quantity, price, description, imageUrl, systemRequirements, isActive);
        this.releaseDate = releaseDate;
        this.genres = genres;
    }

    public VideoGame(@Positive Integer id, @NotBlank String name, @Valid ProductPlatform productPlatform, @PositiveOrZero Integer quantity, @PositiveOrZero double price, @NotBlank String description, @NotBlank String imageUrl, @NotBlank String systemRequirements, Boolean isActive, String releaseDate, @Valid List<Genre> genres) {
        super(id, name, productPlatform, quantity, price, description, imageUrl, systemRequirements, isActive);
        this.releaseDate = releaseDate;
        this.genres = genres;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "VideoGame{" +
                super.toString()+
                "releaseDate='" + releaseDate + '\'' +
                ", genres=" + genres +
                '}';
    }
}
