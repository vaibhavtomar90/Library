package flipkart.pricing.apps.kaizen.service.datatypes;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 05/05/15
 * Time: 12:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceComputation implements Serializable{

    private  long priceVersion;
    private  double mrp;
    private  double fsp;
    private  double fk_discount;


    private final String listingID;
    private final String computeContext;



    public PriceComputation(String listingID,long priceVersion, double mrp, double fsp,
                            double fk_discount,String computeContext) {
        this.priceVersion = priceVersion;
        this.mrp = mrp;
        this.fsp = fsp;
        this.fk_discount = fk_discount;
        this.listingID = listingID;
        this.computeContext = computeContext;
    }


    public String getComputeContext() {
        return computeContext;
    }

    public double getMrp() {
        return mrp;
    }

    public double getFsp() {
        return fsp;
    }

    public double getFk_discount() {
        return fk_discount;
    }

    public String getListingID() {
        return listingID;
    }

    public long getPriceVersion() {
        return priceVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceComputation)) return false;

        PriceComputation that = (PriceComputation) o;

        if (priceVersion != that.priceVersion) return false;
        if (!listingID.equals(that.listingID)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (priceVersion ^ (priceVersion >>> 32));
        result = 31 * result + listingID.hashCode();
        return result;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public void setFsp(double fsp) {
        this.fsp = fsp;
    }

    public void setFk_discount(double fk_discount) {
        this.fk_discount = fk_discount;
    }
}

