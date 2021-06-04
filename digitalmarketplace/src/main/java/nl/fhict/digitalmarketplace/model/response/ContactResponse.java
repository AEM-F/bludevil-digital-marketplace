package nl.fhict.digitalmarketplace.model.response;

public class ContactResponse {
    private Integer id;
    private String contactName;
    private String contactProfileImage;

    public ContactResponse(Integer id, String contactName, String contactProfileImage) {
        this.id = id;
        this.contactName = contactName;
        this.contactProfileImage = contactProfileImage;
    }

    public ContactResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactProfileImage() {
        return contactProfileImage;
    }

    public void setContactProfileImage(String contactProfileImage) {
        this.contactProfileImage = contactProfileImage;
    }
}
