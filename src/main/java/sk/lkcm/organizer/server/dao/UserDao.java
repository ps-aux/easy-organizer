package sk.lkcm.organizer.server.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.lkcm.organizer.server.entity.User;


@Repository("userDao")
@Transactional
public class UserDao {
    
    private static final String ENTITY_NAME = User.class.getSimpleName();
    private final static Logger logger = LoggerFactory.getLogger(UserDao.class);
    
    
    private static final String SELECT = "SELECT u FROM " + ENTITY_NAME + " u  WHERE u.name = ?1";
    
    @PersistenceContext
    private EntityManager entityManager;

    public void saveUser(User user) {
        logger.info("Persisting  " + user);
        entityManager.persist(user);
    }
    
   
    /**
     * Removes a user.
     * @param user the user whose data should be removed
     */
    public void deleteUser(User user){
        logger.info("Removing user " + user);
        User found = entityManager.find(User.class, user.getId());
        entityManager.remove(found);
    }
    
    public User selectUser(String name) {
        logger.info("Retreiving user with name '" + name + "'");
        TypedQuery<User> q = entityManager.createQuery(SELECT, User.class);
        q.setParameter(1, name);
        User user;

        try{
            user = q.getSingleResult();
        }catch(NoResultException ex){
            logger.info("No user found");
            return null;
        }

        return user;
    }
}
