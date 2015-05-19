package flipkart.pricing.apps.kaizen.testrules;


// Copied from data_service test rules to get it going, this needs to go in a JAR
// TODO
/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.rules.ExternalResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

@Component
@Repository
public class KaizenDBClearRule extends ExternalResource {

    private SessionFactory sessionFactory;

    @Inject
    public KaizenDBClearRule(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected void before() throws Throwable {

        super.before();

        Session session = null;
        try {
            session = sessionFactory.openSession();

            session.createSQLQuery("delete from ListingDataVersion").executeUpdate();
            session.createSQLQuery("delete from PriceComputationLatest").executeUpdate();
            session.createSQLQuery("delete from PriceComputationAudit").executeUpdate();
            session.flush();
        } catch (Exception ex) {

        } finally {
            session.close();
        }
    }

    public void clearSession() {
        sessionFactory.getCurrentSession().clear();
    }


    private Session createSession() {
        return sessionFactory.openSession();
    }

    private Transaction beginTransaction() {
        return sessionFactory.getCurrentSession().beginTransaction();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
