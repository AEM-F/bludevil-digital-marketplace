package nl.fhict.digitalmarketplace.service.product.filter;

import nl.fhict.digitalmarketplace.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ProductPriceAndPlatformFilterSpec extends ProductFilterSpec{
    private double price;
    private String platformName;

    public ProductPriceAndPlatformFilterSpec(double price, String platformName) {
        this.price = price;
        this.platformName = platformName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public Page<Product> applyFilter(Pageable pageable) {
        return super.getProductRepository().findAllByProductPlatform_NameAndPrice(platformName, price, pageable);
    }
}
