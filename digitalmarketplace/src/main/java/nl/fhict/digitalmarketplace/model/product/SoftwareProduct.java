package nl.fhict.digitalmarketplace.model.product;

import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity
public class SoftwareProduct extends Product{
    public SoftwareProduct() {
    }

    public SoftwareProduct(@NotBlank String name, @Valid ProductPlatform productPlatform, @PositiveOrZero Integer quantity, @PositiveOrZero double price, @NotBlank String description, @NotBlank String imageUrl, @NotBlank String systemRequirements, Boolean isActive) {
        super(name, productPlatform, quantity, price, description, imageUrl, systemRequirements, isActive);
    }

    public SoftwareProduct(@Positive Integer id, @NotBlank String name, @Valid ProductPlatform productPlatform, @PositiveOrZero Integer quantity, @PositiveOrZero double price, @NotBlank String description, @NotBlank String imageUrl, @NotBlank String systemRequirements, Boolean isActive) {
        super(id, name, productPlatform, quantity, price, description, imageUrl, systemRequirements, isActive);
    }

    @Override
    public String getType() {
        return "softwareproduct";
    }

    @Override
    public String toString() {
        return "SoftwareProduct{"+super.toString() + "}";
    }
}
