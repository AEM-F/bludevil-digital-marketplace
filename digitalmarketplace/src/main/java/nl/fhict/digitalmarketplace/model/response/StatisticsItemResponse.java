package nl.fhict.digitalmarketplace.model.response;

public class StatisticsItemResponse {
    private String name;
    private long value;

    public StatisticsItemResponse(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public StatisticsItemResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
