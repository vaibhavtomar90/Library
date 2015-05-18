package flipkart.pricing.apps.kaizen.utils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import org.hibernate.SessionFactory;

/**
 * @understands Allows execution of methods within a hibernate session
 */
@Named
@Transactional
public class HibernateSessionUtil<T> {
    @Inject
    private SessionFactory sessionFactory;

    public T withinSession(SessionBoundFunction<T> sessionBoundFunction) {
        return sessionBoundFunction.doInSession();

    }

    public void clearSession() {
        sessionFactory.getCurrentSession().clear();
    }
}
