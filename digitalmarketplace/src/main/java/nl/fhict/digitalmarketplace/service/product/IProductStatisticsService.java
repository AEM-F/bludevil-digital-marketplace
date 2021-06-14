package nl.fhict.digitalmarketplace.service.product;

import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.response.StatisticsItemResponse;

import java.util.List;

public interface IProductStatisticsService {
    List<StatisticsItemResponse> countAllProductsByPlatforms() throws ResourceNotFoundException;
    List<StatisticsItemResponse> countAllProductsByGenres() throws ResourceNotFoundException;
    public List<StatisticsItemResponse> countAllProducts();
}
