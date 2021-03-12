package nl.fhict.digitalmarketplace.model.product;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "ProductPlatforms")
public class ProductPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    @Positive
    private Integer id;
    @NotBlank
    @Column(name = "platform_name")
    private String name;

    public ProductPlatform(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductPlatform(String name) {
        this.name = name;
    }

    public ProductPlatform() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductPlatform{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}


