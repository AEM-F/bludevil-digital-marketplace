package nl.fhict.digitalmarketplace.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VideoGame.class, name = "videogame"),
        @JsonSubTypes.Type(value = SoftwareProduct.class, name = "softwareproduct")
})
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    @Positive
    private Integer id;
    @NotBlank
    private String name;
    @ManyToOne
    @JoinColumn(name = "platform_id")
    @Valid
    private ProductPlatform productPlatform;
    @PositiveOrZero
    private Integer quantity;
    @PositiveOrZero
    private double price;
    @NotBlank
    private String description;
    @NotBlank
    private String imageUrl;
    @NotBlank
    private String systemRequirements;
    private Boolean isActive=true;
    private String type;

    public Product(){}

    public Product(@NotBlank String name, @Valid ProductPlatform productPlatform, @PositiveOrZero Integer quantity, @PositiveOrZero double price, @NotBlank String description, @NotBlank String imageUrl, @NotBlank String systemRequirements, Boolean isActive) {
        this.name = name;
        this.productPlatform = productPlatform;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.systemRequirements = systemRequirements;
        this.isActive = isActive;
    }

    public Product(@Positive Integer id, @NotBlank String name, @Valid ProductPlatform productPlatform, @PositiveOrZero Integer quantity, @PositiveOrZero double price, @NotBlank String description, @NotBlank String imageUrl, @NotBlank String systemRequirements, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.productPlatform = productPlatform;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.systemRequirements = systemRequirements;
        this.isActive = isActive;
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

    public ProductPlatform getProductPlatform() {
        return productPlatform;
    }

    public void setProductPlatform(ProductPlatform productPlatform) {
        this.productPlatform = productPlatform;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
            this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getSystemRequirements() {
        return systemRequirements;
    }

    public void setSystemRequirements(String systemRequirements) {
        this.systemRequirements = systemRequirements;
    }

    public String getType() {
        return type;
    }

    @Transient
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productPlatform=" + productPlatform +
                ", quantity=" + quantity +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", systemRequirements='" + systemRequirements + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
