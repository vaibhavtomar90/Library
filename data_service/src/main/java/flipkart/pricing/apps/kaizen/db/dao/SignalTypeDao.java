package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.SignalTypes;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignalTypeDao extends AbstractDAO<SignalTypes> {

    @Inject
    public SignalTypeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Map<String, SignalTypes> fetchNameSignalTypesMap() {
        List<SignalTypes> signalTypesList = fetchAll();
        Map<String, SignalTypes> signalTypesMap = new HashMap<>();
        for (SignalTypes signalType : signalTypesList) {
            signalTypesMap.put(signalType.getName(), signalType);
        }
        return signalTypesMap;
    }


    public List<SignalTypes> fetchAll() {
        Criteria criteria = currentSession().createCriteria(SignalTypes.class);
        return list(criteria);
    }

}
