package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.service.product.IProductPlatformService;
import nl.fhict.digitalmarketplace.service.product.IProductService;
import nl.fhict.digitalmarketplace.service.product.filter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    private Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(IProductService productService, IProductFilterService productFilterService,IProductPlatformService platformService) {
        this.productService = productService;
        this.productFilterService = productFilterService;
        this.platformService = platformService;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createProduct(@RequestBody Product product){
        try {
            Product productCreated = productService.createProduct(product);
            return ResponseEntity.ok(productCreated);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getProduct(@PathVariable(name = "id") Integer id){
        Product returnedProduct = null;
        try {
            returnedProduct = productService.getProductById(id);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

        return ResponseEntity.ok(returnedProduct);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProducts(@RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      @RequestParam(name = "price", required = false) String price){
        try {
            if(price != null){
                double priceConverted = Double.parseDouble(price);
                LOG.info("Attempting to get products by price: "+price);
                Page<Product> products = productFilterService.filterBy(page, size, new ProductPriceFilterSpec(priceConverted));
                PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalPages(), products.getNumber()+1,products.getSize());
                return ResponseEntity.ok(paginationResponse);
            }
            else{
                LOG.info("Attempting to get products");
                Page<Product> products = productService.getProducts(page, size);
                PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalPages(), products.getNumber()+1,products.getSize());
                return ResponseEntity.ok(paginationResponse);
            }
        }
        catch (NumberFormatException | InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        } catch (ResourceNotFoundException e) {
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @RequestMapping(path = {"/platform/{name}"}, method = RequestMethod.GET)
    public ResponseEntity getProductsByPlatform(@PathVariable(name = "name") String platformName,
                                                @RequestParam(name = "page", defaultValue = "1") int page,
                                                @RequestParam(name = "size", defaultValue = "10") int size,
                                                @RequestParam(name = "price", required = false) String price){
        try {
            platformService.getPlatformByName(platformName);
            if(price != null){
                double priceConverted = Double.parseDouble(price);
                LOG.info("Attempting to get products by price and platform: "+price+" "+platformName);
                Page<Product> products = productFilterService.filterBy(page, size, new ProductPriceAndPlatformFilterSpec(priceConverted, platformName));
                PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalPages(), products.getNumber()+1,products.getSize());
                return ResponseEntity.ok(paginationResponse);
            }
            else{
                LOG.info("Attempting to get products by platform: "+platformName);
                Page<Product> products = productFilterService.filterBy(page, size, new ProductPlatformFilterSpec(platformName));
                PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalPages(), products.getNumber()+1,products.getSize());
                return ResponseEntity.ok(paginationResponse);
            }
        } catch (NumberFormatException | InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        } catch (ResourceNotFoundException e) {
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @RequestMapping(path = {"/search/{name}"}, method = RequestMethod.GET)
    public ResponseEntity getProductByName(@PathVariable(name = "name") String productName,@RequestParam(name = "page", defaultValue = "1") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size){
        try {
            LOG.info("Attempting to get products by name: "+productName);
            Page<Product> products = productService.getProductsByName(page, size,productName);
            PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalPages(), products.getNumber()+1,products.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateProduct(@RequestBody Product product,@PathVariable(name = "id") Integer id){
        try {
            Product updatedProduct = productService.updateProduct(product, id);
            return ResponseEntity.ok(updatedProduct);
        }
        catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable Integer id){
        try {
            Product deletedProduct = productService.deleteProductById(id);
            return ResponseEntity.ok(deletedProduct);
        }catch (InvalidInputException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        catch (ResourceNotFoundException e){
            LOG.error(e.getMessage());
            MessageDTO msg = new MessageDTO();
            msg.setMessage(e.getMessage());
            msg.setType("ERROR");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

}
