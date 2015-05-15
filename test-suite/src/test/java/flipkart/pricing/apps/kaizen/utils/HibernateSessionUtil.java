package flipkart.pricing.apps.kaizen.utils;


import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class HibernateSessionUtil {

    @Inject
    private SessionFactory sessionFactory;

    public void clearSession() {
        sessionFactory.getCurrentSession().clear();
    }
}
