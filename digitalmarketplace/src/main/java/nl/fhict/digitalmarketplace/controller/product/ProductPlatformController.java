package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/productPlatforms/")
public class ProductPlatformController {

    private Logger LOG = LoggerFactory.getLogger(ProductPlatformController.class);
    private ProductPlatformRepository productPlatformRepository;

    public ProductPlatformController(ProductPlatformRepository productPlatformRepository) {
        this.productPlatformRepository = productPlatformRepository;
    }
}
