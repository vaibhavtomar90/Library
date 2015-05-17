package flipkart.pricing.apps.kaizen.service.datatypes;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 14/05/15
 * Time: 12:21 AM
 * Description :Simple POJO ,to move around the pricing bits
 */
public class PricingData implements Serializable {

    private final Double fsp;
    private final Double mrp;
    private final Double fk_discount;

    public PricingData(Double fsp, Double mrp, Double fk_discount) {
        this.fsp = fsp;
        this.mrp = mrp;
        this.fk_discount = fk_discount;
    }

    public Double getFsp() {
        return fsp;
    }

    public Double getMrp() {
        return mrp;
    }

    public Double getFk_discount() {
        return fk_discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PricingData)) return false;

        PricingData that = (PricingData) o;

        if (!fk_discount.equals(that.fk_discount)) return false;
        if (!fsp.equals(that.fsp)) return false;
        if (!mrp.equals(that.mrp)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fsp.hashCode();
        result = 31 * result + mrp.hashCode();
        result = 31 * result + fk_discount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(50);
        builder.append("PricingData={").append("mrp=[").append(this.getMrp()).append("],")
                .append("fsp=[").append(this.getFsp()).append("],").append("fk_discount=[")
                .append(this.getFk_discount()).append("]}");

        return builder.toString();
    }
}
