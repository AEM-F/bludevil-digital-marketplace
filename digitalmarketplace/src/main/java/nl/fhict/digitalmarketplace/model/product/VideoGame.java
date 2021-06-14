package nl.fhict.digitalmarketplace.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class VideoGame extends Product{
    @Column(name = "product_releaseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
//    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date releaseDate;
    @ManyToMany(targetEntity = Genre.class, cascade = CascadeType.DETACH, fetch= FetchType.EAGER)
    @JoinTable(name = "Product_Genres", joinColumns = {@JoinColumn(name = "product_id")}, inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private List<Genre> genres;

    public VideoGame() {
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(@NotBlank Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(@Valid List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String getType() {
        return "videogame";
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
