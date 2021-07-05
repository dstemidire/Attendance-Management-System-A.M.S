/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author AHMOD
 */
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Main data access object, use for all kinds of DML(data manipulation language) etc.
 *
 * @author AHMOD
 */
public class HibernateMain<E> {
    //for holding query result performed by date query language statement.
    private List<E> selectList;
    

     /**
     * use for all kind of query language
     *
     * @param  selectStatement
     *         The SQL query language statement
     * @param entityObject 
     *        The class object to performed query on.
     * @return generic list object.
     *         The result of data query language.
     */
    public List<E> selectSession(String selectStatement, Class entityObject) {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
    StandardServiceRegistryBuilder serviceRegistry
                = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        configuration.addAnnotatedClass(entityObject);
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry.build());

        Session selectSessionObject;
        selectSessionObject = sessionFactory.openSession();
        try {
            Transaction transact = selectSessionObject.beginTransaction();
            SQLQuery SqlQuery = selectSessionObject.createSQLQuery(selectStatement).addEntity(entityObject);
            selectList = SqlQuery.list();
            transact.commit();

        } catch (Exception e) {
            e.getMessage();
        } finally {
            selectSessionObject.close();
        }
        return selectList;
    }
    

    Transaction insertTransact;

    /**
     * use for all kind of query language
     *
     * @param  insertObject
     *         Data object to perform insert operation.
     * @param entityObject 
     *        The class object to performed sql insert operation.
     * @return boolean object.
     *         true if insert operation was successful or false if not.
     */
    public boolean insertSession(Object insertObject, Class entityObject) {
         Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
    StandardServiceRegistryBuilder serviceRegistry
                = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        configuration.addAnnotatedClass(entityObject);
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry.build());

        Session insertSessionObject = sessionFactory.openSession();
        try {
            insertTransact = insertSessionObject.beginTransaction();
            insertSessionObject.save(insertObject);
            insertTransact.commit();

        } catch (Exception e) {
            e.getMessage();
        } finally {
            insertSessionObject.close();
        }
        return insertTransact.wasCommitted();
    }
    
    Transaction updateTransact;
    
      /**
     * use for all kind of query language
     *
     * @param  updateObject
     *         Data object to perform update operation.
     * @param entityObject 
     *        The class object to performed sql update operation.
     * @return boolean object.
     *         true if insert operation was successful or false if not.
     */
     public boolean updateSession(Object updateObject, Class entityObject) {
          Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
    StandardServiceRegistryBuilder serviceRegistry
                = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        configuration.addAnnotatedClass(entityObject);
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry.build());

        Session updateSessionObject = sessionFactory.openSession();
        try {
            updateTransact = updateSessionObject.beginTransaction();
            updateSessionObject.update(updateObject);
            updateTransact.commit();

        } catch (Exception e) {
            e.getMessage();
        } finally {
            updateSessionObject.close();
        }
        return updateTransact.wasCommitted();
    }

}
