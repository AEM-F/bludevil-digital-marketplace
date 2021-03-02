package nl.fhict.digitalmarketplace.model.product;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "genre_id")
    private Integer id;
    @NotBlank
    private String genreName;

    public Genre(@Positive Integer id,@NotBlank String genreName) {
        this.id = id;
        this.genreName = genreName;
    }

    public Genre(@NotBlank String genreName) {
        this.genreName = genreName;
    }

    public Genre() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", genreName='" + genreName + '\'' +
                '}';
    }
}
