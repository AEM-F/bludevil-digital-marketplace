package nl.fhict.digitalmarketplace.controller.product;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Product;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.response.MessageDTO;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.service.product.IProductService;
import nl.fhict.digitalmarketplace.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(path = "/api/products/")
public class ProductController {

    private IProductService productService;
    private Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
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

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
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

    @RequestMapping(path = {"pageable/{page}/{size}"},method = RequestMethod.GET)
    public ResponseEntity getProducts(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size){

        try {
            LOG.info("Attempting to get all products");
            Page<Product> products = productService.getProducts(page, size);
            PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalPages(), products.getNumber()+1,products.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
        catch (InvalidInputException | NumberFormatException e){
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

    @RequestMapping(path = {"pageable/{page}/{size}/price/{price}"},method = RequestMethod.GET)
    public ResponseEntity getProductsByPrice(@PathVariable(name = "page") int page,@PathVariable(name = "size") int size,@PathVariable(name = "price") String price){
        try {
            LOG.info("Attempting to get products by price: "+price);
            Page<Product> products = productService.getProductsByPrice(page, size, price);
            PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalPages(), products.getNumber()+1,products.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
        catch (InvalidInputException | NumberFormatException e){
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

    @RequestMapping(path = {"pageable/{page}/{size}/platform/{platformName}"},method = RequestMethod.GET)
    public ResponseEntity getProductsByPlatform(@PathVariable(name = "page") int page,@PathVariable(name = "size") int size, @PathVariable(name = "platformName") String platform){
        try{
            LOG.info("Attempting to get products by platform: "+platform);
            Page<Product> products = productService.getProductsByPlatform(page, size,platform.toLowerCase(Locale.ROOT));
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

    @RequestMapping(path = {"pageable/{page}/{size}/price/{price}/platform/{platformName}"},method = RequestMethod.GET)
    public ResponseEntity getProductsByPriceAndPlatform(@PathVariable(name = "page") int page,@PathVariable(name = "size") int size,@PathVariable(name = "price") String price, @PathVariable(name = "platformName") String platform){
        try {
            LOG.info("Attempting to get products by price and platform: "+price+" "+platform);
            Page<Product> products = productService.getProductsByPriceAndPlatform(page, size, price, platform.toLowerCase(Locale.ROOT));
            PaginationResponse<Product> paginationResponse = new PaginationResponse<Product>(products.getContent(), products.getTotalPages(), products.getNumber()+1,products.getSize());
            return ResponseEntity.ok(paginationResponse);
        }
        catch (InvalidInputException | NumberFormatException e){
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

    @RequestMapping(path = {"pageable/{page}/{size}/productName/{productName}"},method = RequestMethod.GET)
    public ResponseEntity getProductByName(@PathVariable(name = "page") int page,@PathVariable(name = "size") int size,@PathVariable(name = "productName") String productName){
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

    @RequestMapping(path = "{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
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
