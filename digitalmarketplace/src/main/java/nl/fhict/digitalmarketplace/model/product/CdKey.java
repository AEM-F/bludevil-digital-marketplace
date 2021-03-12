package nl.fhict.digitalmarketplace.model.product;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "CdKeys")
public class CdKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cdkey_id")
    @Positive
    private Integer id;
    @NotBlank
    @Column(name = "cdKey_sequence")
    private String sequence;
    @Positive
    private Integer productId;
    @Column(name = "cdKey_isUsed")
    private Boolean isUsed=false;

    public CdKey(Integer id, String sequence, Integer productId) {
        this.id = id;
        this.sequence = sequence;
        this.productId = productId;
    }

    public CdKey(String sequence, Integer productId) {
        this.sequence = sequence;
        this.productId = productId;
    }

    public CdKey() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    @Override
    public String toString() {
        return "CdKey{" +
                "id=" + id +
                ", sequence='" + sequence + '\'' +
                ", productId=" + productId +
                ", isUsed=" + isUsed +
                '}';
    }
}

