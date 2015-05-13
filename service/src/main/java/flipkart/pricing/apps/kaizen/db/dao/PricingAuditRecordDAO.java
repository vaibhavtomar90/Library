package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.db.model.PricingAuditRecord;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 06/05/15
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Repository
@Transactional
public class PricingAuditRecordDAO extends AbstractDAO<PricingAuditRecord> {

    @Inject
    public PricingAuditRecordDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Long save(PricingAuditRecord priceComputationAudit){

        return (Long) this.currentSession().save(priceComputationAudit);
    }

    public PricingAuditRecord getAuditEntryForVersion(Long version){
        throw new NotImplementedException();
    }

    public PricingAuditRecord getLatestPriceForListing(String listingID){
        throw new NotImplementedException();
    }
}
