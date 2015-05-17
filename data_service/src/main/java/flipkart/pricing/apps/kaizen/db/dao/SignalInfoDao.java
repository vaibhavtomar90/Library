package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.SignalInfo;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class SignalInfoDao extends AbstractDAO<SignalInfo> {

    private static final String INSERT_SIGNAL_QUERY = "INSERT INTO SignalInfo (listingId, signalTypeId, value, version, qualifier) VALUES (:listingId, :signalTypeId, :value, :version, :qualifier)";
    private static final String UPDATE_SIGNAL_QUERY = "UPDATE SignalInfo SET value = :value, version = :version, qualifier = :qualifier WHERE listingId = :listingId AND signalTypeId = :signalTypeId AND version <= :version";

    @Inject
    public SignalInfoDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    private int insertSignal(SignalInfo signalInfo) {
       return currentSession().createSQLQuery(INSERT_SIGNAL_QUERY)
                              .setLong("listingId", signalInfo.getId().getListingId())
                              .setLong("signalTypeId", signalInfo.getId().getSignalTypeId())
                              .setString("value", signalInfo.getValue())
                              .setLong("version", signalInfo.getVersion())
                              .setString("qualifier", signalInfo.getQualifier()).executeUpdate();
    }

    private int updateSignal(SignalInfo signalInfo) {
       return currentSession().createSQLQuery(UPDATE_SIGNAL_QUERY)
                              .setString("value", signalInfo.getValue())
                              .setLong("version", signalInfo.getVersion())
                              .setString("qualifier", signalInfo.getQualifier())
                              .setLong("listingId", signalInfo.getId().getListingId())
                              .setLong("signalTypeId", signalInfo.getId().getSignalTypeId()).executeUpdate();
    }

    public List<SignalInfo> fetchSignals(Long listingId) {
       Criteria criteria = currentSession().createCriteria(SignalInfo.class).add(Restrictions.eq("id.listingId", listingId));
       return list(criteria);
    }

    public SignalInfo fetchSignals(Long listingId, Long signalTypeId) {
       Criteria criteria = currentSession().createCriteria(SignalInfo.class).add(Restrictions.eq("id.listingId", listingId)).add(Restrictions.eq("id.signalTypeId", signalTypeId));
       return uniqueResult(criteria);
    }

    public List<SignalInfo> fetchSignals(Long listingId, List<Long> signalTypeIds) {
        Criteria criteria = currentSession().createCriteria(SignalInfo.class).add(Restrictions.eq("id.listingId", listingId)).add(Restrictions.in("id.signalTypeId", signalTypeIds));
        return list(criteria);
    }

    public int insertOrUpdateSignal(SignalInfo signalInfo) {
        SignalInfo fetch = fetchSignals(signalInfo.getId().getListingId(), signalInfo.getId().getSignalTypeId());
        //TODO original dm had a try-catch block here, talk with Vivek. I don't think it's required since we already have listing level lock
        return (fetch != null) ? updateSignal(signalInfo) : insertSignal(signalInfo);
    }
}
