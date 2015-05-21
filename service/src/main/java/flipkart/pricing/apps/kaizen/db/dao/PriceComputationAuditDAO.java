package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.db.model.PriceComputationAudit;
import flipkart.pricing.apps.kaizen.db.model.PriceComputationAudit;
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
public class PriceComputationAuditDAO extends AbstractDAO<PriceComputationAudit> {

    @Inject
    public PriceComputationAuditDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Long save(PriceComputationAudit priceComputationAudit){
        Long id = (Long)this.currentSession().save(priceComputationAudit);
        this.currentSession().flush(); // this is to get the generated timestamp, so that Hibernate flushes to actual DB
        this.currentSession().refresh(priceComputationAudit);
        return id;
    }

    public PriceComputationAudit getAuditEntryForVersion(Long version){
        throw new NotImplementedException();
    }

    public PriceComputationAudit getLatestPriceForListing(String listingID){
        throw new NotImplementedException();
    }
}
