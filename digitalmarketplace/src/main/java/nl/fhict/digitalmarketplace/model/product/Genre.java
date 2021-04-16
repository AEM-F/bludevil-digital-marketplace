package nl.fhict.digitalmarketplace.model.product;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "Genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    @Column(name = "genre_id")
    private Integer id;
    @NotBlank
    @Column(name = "genre_name")
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
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }
        Genre other = (Genre) obj;
        if(this.id == null || other.getId() == null || !this.id.equals(other.getId())){
            return false;
        }
        if (this.genreName ==null || other.getGenreName() == null || !this.genreName.equals(other.getGenreName())){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", genreName='" + genreName + '\'' +
                '}';
    }
}
