package flipkart.pricing.apps.kaizen.utils;

import javax.inject.Named;
import javax.transaction.Transactional;

/**
 * @understands Allows execution of methods within a hibernate session
 */
@Named
@Transactional
public class HibernateSessionUtil<T> {

    public T withinSession(SessionBoundFunction<T> sessionBoundFunction) {
        return sessionBoundFunction.doInSession();
    }
}
