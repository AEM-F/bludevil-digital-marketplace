package nl.fhict.digitalmarketplace.model.product;

import javax.persistence.Entity;


@Entity
public class SoftwareProduct extends Product{
    public SoftwareProduct() {
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
