package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.Signal;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import javax.inject.Inject;
import java.util.List;

public class SignalDao extends AbstractDAO<Signal> {

    @Inject
    public SignalDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    private int insertSignal(Signal signal) { return 0; }

    private int updateSignal(Signal signal) { return 0; }

    public List<Signal> fetchAllForListing(Long listingId) { return null; }

    public Signal fetchSignal(Long listingId, Long signalId) { return null; }

    public int insertOrUpdateSignal(Signal signal) {
        Signal fetch = fetchSignal(signal.getId().getListingId(), signal.getId().getSignalTypeId());
        if (fetch != null) {
            return updateSignal(signal);
        } else {
            try {
                return insertSignal(signal);
            } catch (ConstraintViolationException e) {
                return updateSignal(signal);
            }
        }
    }
}
