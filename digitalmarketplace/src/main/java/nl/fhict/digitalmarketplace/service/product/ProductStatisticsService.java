package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.product.Genre;
import nl.fhict.digitalmarketplace.model.product.ProductPlatform;
import nl.fhict.digitalmarketplace.model.response.StatisticsItemResponse;
import nl.fhict.digitalmarketplace.repository.product.GenreRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductPlatformRepository;
import nl.fhict.digitalmarketplace.repository.product.ProductRepository;
import nl.fhict.digitalmarketplace.repository.product.VideoGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductStatisticsService implements IProductStatisticsService {
    private Logger logger = LoggerFactory.getLogger(ProductStatisticsService.class);
    private ProductRepository productRepository;
    private ProductPlatformRepository productPlatformRepository;
    private GenreRepository genreRepository;
    private VideoGameRepository videoGameRepository;

    public ProductStatisticsService(ProductRepository productRepository,
                                    ProductPlatformRepository productPlatformRepository,
                                    GenreRepository genreRepository,
                                    VideoGameRepository videoGameRepository) {
        this.productRepository = productRepository;
        this.productPlatformRepository = productPlatformRepository;
        this.genreRepository = genreRepository;
        this.videoGameRepository = videoGameRepository;
    }

    @Override
    public List<StatisticsItemResponse> countAllProducts(){
        List<StatisticsItemResponse> responses = new ArrayList<>();
        long productNr = productRepository.count();
        responses.add(new StatisticsItemResponse("products", productNr));
        return responses;
    }

    @Override
    public List<StatisticsItemResponse> countAllProductsByPlatforms() throws ResourceNotFoundException {
        List<StatisticsItemResponse> responses = new ArrayList<>();
        List<ProductPlatform> platforms = productPlatformRepository.findAll();
        if(!platforms.isEmpty()){
            for (ProductPlatform platform : platforms){
                long productNr = productRepository.countAllByProductPlatform_Name(platform.getName());
                responses.add(new StatisticsItemResponse(platform.getName(), productNr));
            }
            return responses;
        }else{
            throw new ResourceNotFoundException("No platforms were found");
        }
    }

    @Override
    public List<StatisticsItemResponse> countAllProductsByGenres() throws ResourceNotFoundException {
        List<StatisticsItemResponse> responses = new ArrayList<>();
        List<Genre> genres = genreRepository.findAll();
        if(!genres.isEmpty()){
            for (Genre genre : genres){
                long productNr = videoGameRepository.countAllByGenresGenreName(genre.getGenreName());
                responses.add(new StatisticsItemResponse(genre.getGenreName(), productNr));
            }
            return responses;
        }else{
            throw new ResourceNotFoundException("No genres were found");
        }
    }
}
