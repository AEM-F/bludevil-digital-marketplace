package nl.fhict.digitalmarketplace.model.response;

import java.util.List;

public class PaginationResponse<T> {
    private List<T> objectsList;
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    public PaginationResponse(List<T> objectsList, long totalElements, int pageNumber, int pageSize) {
        this.objectsList = objectsList;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public PaginationResponse() {
    }

    public List<T> getObjectsList() {
        return objectsList;
    }

    public void setObjectsList(List<T> objectsList) {
        this.objectsList = objectsList;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
