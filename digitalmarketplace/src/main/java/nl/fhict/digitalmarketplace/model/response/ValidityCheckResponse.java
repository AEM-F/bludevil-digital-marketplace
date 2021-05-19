package nl.fhict.digitalmarketplace.model.response;

public class ValidityCheckResponse {
    private boolean isValid;

    public ValidityCheckResponse(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
