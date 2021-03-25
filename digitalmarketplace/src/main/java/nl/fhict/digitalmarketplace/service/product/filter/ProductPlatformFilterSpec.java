package nl.fhict.digitalmarketplace.service.product.filter;

import nl.fhict.digitalmarketplace.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ProductPlatformFilterSpec extends ProductFilterSpec{
    private String platformName;

    public ProductPlatformFilterSpec(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public Page<Product> applyFilter(Pageable pageable) {
        return super.getProductRepository().findAllByProductPlatform_Name(platformName, pageable);
    }
}
