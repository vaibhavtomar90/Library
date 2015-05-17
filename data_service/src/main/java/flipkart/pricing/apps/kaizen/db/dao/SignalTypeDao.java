package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.SignalType;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class SignalTypeDao extends AbstractDAO<SignalType> {

    @Inject
    public SignalTypeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public SignalType insertSignalType(SignalType signalType) {
        return persist(signalType);
    }

    public Map<String, SignalType> fetchNameSignalTypesMap() {
        List<SignalType> signalTypeList = fetchAll();
        Map<String, SignalType> signalTypesMap = new HashMap<>();
        for (SignalType signalType : signalTypeList) {
            signalTypesMap.put(signalType.getName(), signalType);
        }
        return signalTypesMap;
    }


    public List<SignalType> fetchAll() {
        Criteria criteria = currentSession().createCriteria(SignalType.class);
        return list(criteria);
    }

}
