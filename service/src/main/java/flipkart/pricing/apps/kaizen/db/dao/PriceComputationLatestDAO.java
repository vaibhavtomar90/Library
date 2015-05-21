package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.db.model.PriceComputationLatest;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 06/05/15
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Repository
public class PriceComputationLatestDAO extends AbstractDAO<PriceComputationLatest> {

    @Inject
    public PriceComputationLatestDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<PriceComputationLatest> getReConRecords(Long fromVersion, int noOfRecords) {

        Criteria criteria = currentSession().createCriteria(PriceComputationLatest.class).
                add(Restrictions.ge("priceComputationAudit.id", new Long(fromVersion))).
                addOrder(Order.asc("priceComputationAudit.id")).
                setMaxResults(noOfRecords);

        return criteria.list();
    }

    public PriceComputationLatest get(String id) {
        return super.get(id);
    }

    public PriceComputationLatest getByVersion(Long versionId) {

        Criteria criteria = this.currentSession().createCriteria(PriceComputationLatest.class)
                .add(Restrictions.eq("priceComputationAudit.id", versionId));

        List records = criteria.list();

        return records.size() > 0 ? (PriceComputationLatest) records.get(0) : null;
    }

    public void upsert(PriceComputationLatest record) {
        this.currentSession().saveOrUpdate(record);
    }
}

