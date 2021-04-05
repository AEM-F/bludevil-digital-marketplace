package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.service.product.IProductPlatformService;
import nl.fhict.digitalmarketplace.service.product.IProductService;
import nl.fhict.digitalmarketplace.service.product.filter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping(path = "/api/products")
public class ProductController {

    private IProductService productService;
    private IProductFilterService productFilterService;
    private IProductPlatformService platformService;
    private Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(IProductService productService, IProductFilterService productFilterService,IProductPlatformService platformService) {
        this.productService = productService;
        this.productFilterService = productFilterService;
        this.platformService = platformService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@RequestBody Product product) throws InvalidInputException, ResourceNotFoundException {
        Product productCreated = productService.createProduct(product);
        return ResponseEntity.ok(productCreated);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable(name = "id") Integer id) throws InvalidInputException, ResourceNotFoundException {
        Product returnedProduct = productService.getProductById(id);
        return ResponseEntity.ok(returnedProduct);
    }

    @GetMapping()
    public ResponseEntity<Object> getProducts(@RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      @RequestParam(name = "price", required = false) String price) throws ResourceNotFoundException, InvalidInputException {
        if(price != null){
            double priceConverted = Double.parseDouble(price);
            log.info("Attempting to get products by price: "+price);
            Page<Product> products = productFilterService.filterBy(page, size, new ProductPriceFilterSpec(priceConverted));
            PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalElements(), products.getNumber()+1,products.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
        else{
            log.info("Attempting to get products");
            Page<Product> products = productService.getProducts(page, size);
            PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalElements(), products.getNumber()+1,products.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
    }

    @GetMapping(path = {"/platform/{name}"})
    public ResponseEntity<Object> getProductsByPlatform(@PathVariable(name = "name") String platformName,
                                                @RequestParam(name = "page", defaultValue = "1") int page,
                                                @RequestParam(name = "size", defaultValue = "10") int size,
                                                @RequestParam(name = "price", required = false) String price) throws InvalidInputException, ResourceNotFoundException {
        platformService.getPlatformByName(platformName);
        if(price != null){
            double priceConverted = Double.parseDouble(price);
            log.info("Attempting to get products by price and platform: "+price+" "+platformName);
            Page<Product> products = productFilterService.filterBy(page, size, new ProductPriceAndPlatformFilterSpec(priceConverted, platformName));
            PaginationResponse<Product> paginationResponse = new PaginationResponse<>(products.getContent(), products.getTotalElements(), products.getNumber()+1,products.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
        else{
            log.info("Attempting to get products by platform: "+platformName);
            Page<Product> products = productFilterService.filterBy(page, size, new ProductPlatformFilterSpec(platformName));
            PaginationResponse<Product> paginationResponse = new PaginationResponse<>(products.getContent(), products.getTotalElements(), products.getNumber()+1,products.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
    }

    @GetMapping(path = {"/search/{name}"})
    public ResponseEntity<Object> getProductByName(@PathVariable(name = "name") String productName,@RequestParam(name = "page", defaultValue = "1") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size) throws InvalidInputException, ResourceNotFoundException {
        log.info("Attempting to get products by name: "+productName);
        Page<Product> products = productService.getProductsByName(page, size,productName);
        PaginationResponse<Product> paginationResponse = new PaginationResponse<>(products.getContent(), products.getTotalElements(), products.getNumber()+1,products.getSize());
        return ResponseEntity.ok(paginationResponse);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProduct(@RequestBody Product product,@PathVariable(name = "id") Integer id) throws InvalidInputException, ResourceNotFoundException {
        Product updatedProduct = productService.updateProduct(product, id);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Integer id) throws InvalidInputException, ResourceNotFoundException {
        Product deletedProduct = productService.deleteProductById(id);
        return ResponseEntity.ok(deletedProduct);
    }

}
