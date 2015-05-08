package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.Signal;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import javax.inject.Inject;
import java.util.List;

public class SignalDao extends AbstractDAO<Signal> {

    private static final String INSERT_SIGNAL_QUERY = "INSERT INTO signals (listing_id, signal_type_id, value, version, server_timestamp, qualifier) VALUES (:listing_id, :signal_type_id, :value, :version, :server_timestamp, :qualifier)";
    private static final String UPDATE_SIGNAL_QUERY = "UPDATE signals SET value = :value, version = :version, server_timestamp = :server_timestamp, qualifier = :qualifier WHERE listing_id = :listing_id AND signal_type_id = :signal_type_id AND version <= :version";

    @Inject
    public SignalDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    private int insertSignal(Signal signal) {
       return currentSession().createSQLQuery(INSERT_SIGNAL_QUERY)
                              .setLong("listing_id", signal.getId().getListingId())
                              .setLong("signal_type_id", signal.getId().getSignalTypeId())
                              .setString("value", signal.getValue())
                              .setLong("version", signal.getVersion())
                              .setTimestamp("server_timestamp", signal.getServerTimestamp())
                              .setString("qualifier", signal.getQualifier()).executeUpdate();
    }

    private int updateSignal(Signal signal) {
       return currentSession().createSQLQuery(UPDATE_SIGNAL_QUERY)
                              .setString("value", signal.getValue())
                              .setLong("version", signal.getVersion())
                              .setTimestamp("server_timestamp", signal.getServerTimestamp())
                              .setString("qualifier", signal.getQualifier())
                              .setLong("listing_id", signal.getId().getListingId())
                              .setLong("signal_type_id", signal.getId().getSignalTypeId()).executeUpdate();
    }

    public List<Signal> fetchSignals(Long listingId) {
       Criteria criteria = currentSession().createCriteria(Signal.class).add(Restrictions.eq("id.listingId", listingId));
       return list(criteria);
    }

    public Signal fetchSignals(Long listingId, Long signalTypeId) {
       Criteria criteria = currentSession().createCriteria(Signal.class).add(Restrictions.eq("id.listingId", listingId)).add(Restrictions.eq("id.signalTypeId", signalTypeId));
       return uniqueResult(criteria);
    }

    public List<Signal> fetchSignals(Long listingId, List<Long> signalTypeIds) {
        Criteria criteria = currentSession().createCriteria(Signal.class).add(Restrictions.eq("id.listingId", listingId)).add(Restrictions.in("id.signalTypeId", signalTypeIds));
        return list(criteria);
    }

    public int insertOrUpdateSignal(Signal signal) {
        Signal fetch = fetchSignals(signal.getId().getListingId(), signal.getId().getSignalTypeId());
        //TODO original dm had a try-catch block here, talk with Vivek. I don't think it's required
        //TODO since we already have listing level lock
        return (fetch != null) ? updateSignal(signal) : insertSignal(signal);
    }
}
