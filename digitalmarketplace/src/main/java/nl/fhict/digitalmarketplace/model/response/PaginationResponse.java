package nl.fhict.digitalmarketplace.model.response;

import java.util.List;

public class PaginationResponse<T> {
    private List<T> objectsList;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

    public PaginationResponse(List<T> objectsList, int totalPages, int pageNumber, int pageSize) {
        this.objectsList = objectsList;
        this.totalPages = totalPages;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
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
