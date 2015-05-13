package flipkart.pricing.apps.kaizen.testrules;


import flipkart.pricing.apps.kaizen.db.model.ListingInfo;
import flipkart.pricing.apps.kaizen.db.model.SignalDataType;
import flipkart.pricing.apps.kaizen.db.model.SignalInfo;
import flipkart.pricing.apps.kaizen.db.model.SignalType;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class HibernateSessionTestRule implements TestRule {

    private SessionFactory sessionFactory;


    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                sessionFactory = createSessionFactory();
                createSession();
                beginTransaction();
                sessionFactory.getCurrentSession().setFlushMode(FlushMode.ALWAYS);
                try {
                    statement.evaluate();
                } finally {
                    shutdown();
                }
            }

        };
    }

    public void clearSession() {
        sessionFactory.getCurrentSession().clear();
    }

    private void shutdown() {
        try {
            try {
                try {
                    sessionFactory.getCurrentSession().createSQLQuery("truncate SignalInfo").executeUpdate();
                    sessionFactory.getCurrentSession().createSQLQuery("truncate ListingInfo").executeUpdate();
                    sessionFactory.getCurrentSession().createSQLQuery("truncate SignalType").executeUpdate();
                    sessionFactory.getCurrentSession().getTransaction().rollback();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sessionFactory.getCurrentSession().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            sessionFactory.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SessionFactory createSessionFactory() {
        Configuration annotatedConfiguration = new Configuration();
        annotatedConfiguration.addAnnotatedClass(ListingInfo.class)
                .addAnnotatedClass(SignalType.class)
                .addAnnotatedClass(SignalDataType.class)
                .addAnnotatedClass(SignalInfo.class);

        annotatedConfiguration.setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");
        annotatedConfiguration.setProperty(Environment.URL, "jdbc:mysql://127.0.0.1:3306/pricing_kaizen");
        annotatedConfiguration.setProperty(Environment.USER, "root");
        annotatedConfiguration.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        annotatedConfiguration.setProperty(Environment.SHOW_SQL, "true");
        annotatedConfiguration.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        annotatedConfiguration.setProperty(Environment.C3P0_MAX_SIZE, "5");
        annotatedConfiguration.setProperty(Environment.HBM2DDL_AUTO, "create");

        final StandardServiceRegistry serviceRegistry =  new StandardServiceRegistryBuilder().applySettings(annotatedConfiguration.getProperties()).build();
        return annotatedConfiguration.buildSessionFactory(serviceRegistry);
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
