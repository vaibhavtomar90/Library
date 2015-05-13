package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.db.model.ReConAuditRecord;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import java.util.Collection;
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
@Transactional
public class PriceReConComputationDAO extends AbstractDAO<ReConAuditRecord> {

    @Inject
    public PriceReConComputationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<ReConAuditRecord> getReConRecords(Long fromVersion, int noOfRecords) {

        Criteria criteria = currentSession().createCriteria(ReConAuditRecord.class).
                add(Restrictions.ge("priceAuditRecord.id", new Long(fromVersion))).setMaxResults(noOfRecords);

        return criteria.list();
    }


    public ReConAuditRecord get(String id) {
        return super.get(id);
    }

    public ReConAuditRecord getByVersion(Long versionId) {

        Criteria criteria = this.currentSession().createCriteria(ReConAuditRecord.class)
                .add(Restrictions.eq("pricingAuditRecord.id", versionId));

        List records = criteria.list();

        return records.size() > 0 ? (ReConAuditRecord) records.get(0) : null;
    }

    public void upsert(ReConAuditRecord record) {
        this.currentSession().saveOrUpdate(record);
        this.currentSession().flush();
    }
}

