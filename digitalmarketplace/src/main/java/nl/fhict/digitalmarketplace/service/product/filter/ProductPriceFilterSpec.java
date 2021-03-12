package nl.fhict.digitalmarketplace.service.product.filter;

import nl.fhict.digitalmarketplace.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ProductPriceFilterSpec extends ProductFilterSpec {
    private double price;

    public ProductPriceFilterSpec(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public Page<Product> applyFilter(Pageable pageable) {
       return super.getProductRepository().findAllByPrice(this.price, pageable);
    }
}
