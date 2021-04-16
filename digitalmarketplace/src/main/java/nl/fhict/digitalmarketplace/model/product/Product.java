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
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VideoGame.class, name = "videogame"),
        @JsonSubTypes.Type(value = SoftwareProduct.class, name = "softwareproduct")
})
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;
    @Column(name = "product_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "platform_id")
    private ProductPlatform productPlatform;
    @Column(name = "product_qty")
    private Integer quantity;
    @Column(name = "product_price")
    private double price;
    @Column(name = "product_description", length = 2000)
    private String description;
    @Column(name = "product_imageUrl")
    private String imageUrl;
    @Column(name = "product_systemReq", length = 2000)
    private String systemRequirements;
    @Column(name = "product_isActive")
    private Boolean isActive=true;
    @Transient
    @JsonProperty("type")
    private String type;

    protected Product(){}

    public Integer getId() {
        return id;
    }

    public void setId(@Positive Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public ProductPlatform getProductPlatform() {
        return productPlatform;
    }

    public void setProductPlatform(@Valid ProductPlatform productPlatform) {
        this.productPlatform = productPlatform;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(@PositiveOrZero Integer quantity) {
            this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(@PositiveOrZero double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@NotBlank String imageUrl) {
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

    public void setSystemRequirements(@NotBlank String systemRequirements) {
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
