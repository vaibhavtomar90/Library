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
import javax.transaction.Transactional;

@Component
@Repository
public class HibernateSessionTestRulePC extends ExternalResource {

    private SessionFactory sessionFactory;

    @Inject
    public HibernateSessionTestRulePC(SessionFactory sessionFactory){
            this.sessionFactory = sessionFactory;
    }

    @Override
    protected void before() throws Throwable {
        super.before();

        Session session = null;
        try {
        session= sessionFactory.openSession();

        session.createSQLQuery("delete from ListingDataVersion").executeUpdate();
        session.createSQLQuery("delete from PriceComputationAudit").executeUpdate();
        session.createSQLQuery("delete from PriceReConComputation").executeUpdate();
        }catch (Exception ex){

        }
        finally {
            session.close();
        }
    }


//    @Override
//    public Statement apply(Statement statement, Description description) {
//        return new Statement() {
//
//            @Override
//            public void evaluate() throws Throwable {
////                sessionFactory = createSessionFactory();
////                createSession();
////                beginTransaction();
////                sessionFactory.getCurrentSession().setFlushMode(FlushMode.ALWAYS);
////                try {
////                    statement.evaluate();
////                } finally {
////                    shutdown();
////                }
//            }
//
//        };
//    }

    public void clearSession() {
        sessionFactory.getCurrentSession().clear();
    }

//    private void shutdown() {
//        try {
//            try {
//                try {
//                    sessionFactory.getCurrentSession().createSQLQuery("truncate ListingDataVersion").executeUpdate();
//                    sessionFactory.getCurrentSession().createSQLQuery("truncate PriceComputationAudit").executeUpdate();
//                    sessionFactory.getCurrentSession().createSQLQuery("truncate PriceReConComputation").executeUpdate();
//                    //sessionFactory.getCurrentSession().getTransaction().rollback();
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                sessionFactory.getCurrentSession().close();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            sessionFactory.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private SessionFactory createSessionFactory() {
//        Configuration annotatedConfiguration = new Configuration();
//        annotatedConfiguration.addAnnotatedClass(ListingInfo.class)
//                .addAnnotatedClass(SignalTypes.class)
//                .addAnnotatedClass(SignalDataTypes.class)
//                .addAnnotatedClass(Signal.class);
//
//        annotatedConfiguration.setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");
//        annotatedConfiguration.setProperty(Environment.URL, "jdbc:mysql://127.0.0.1:3306/pricing_kaizen");
//        annotatedConfiguration.setProperty(Environment.USER, "root");
//        annotatedConfiguration.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
//        annotatedConfiguration.setProperty(Environment.SHOW_SQL, "true");
//        annotatedConfiguration.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//        annotatedConfiguration.setProperty(Environment.C3P0_MAX_SIZE, "5");
//        annotatedConfiguration.setProperty(Environment.HBM2DDL_AUTO, "validate");
//
//        final StandardServiceRegistry serviceRegistry =  new StandardServiceRegistryBuilder().applySettings(annotatedConfiguration.getProperties()).build();
//        return annotatedConfiguration.buildSessionFactory(serviceRegistry);
//    }

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
