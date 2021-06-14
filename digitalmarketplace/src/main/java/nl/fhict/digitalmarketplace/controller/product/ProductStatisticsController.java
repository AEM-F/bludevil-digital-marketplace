package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.service.product.IProductStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/products/statistics")
public class ProductStatisticsController {
    private IProductStatisticsService productStatisticsService;

    public ProductStatisticsController(IProductStatisticsService productStatisticsService) {
        this.productStatisticsService = productStatisticsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/countPlatforms")
    public ResponseEntity<Object> countAllProductsByPlatforms() throws ResourceNotFoundException {
        return ResponseEntity.ok(productStatisticsService.countAllProductsByPlatforms());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/countGenres")
    public ResponseEntity<Object> countAllProductsGenres() throws ResourceNotFoundException {
        return ResponseEntity.ok(productStatisticsService.countAllProductsByGenres());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/countAll")
    public ResponseEntity<Object> countAllProducts(){
        return ResponseEntity.ok(productStatisticsService.countAllProducts());
    }
}
