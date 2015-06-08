package sk.lkcm.organizer.server.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.lkcm.organizer.server.entity.OrganizerEvent;

@Repository("organizerEventDao")
@Transactional
public class OrganizerEventDao {
    
    
    private static final String ENTITY_NAME = OrganizerEvent.class.getSimpleName();
    private static final String EVENT_STARTING = "SELECT e FROM " + ENTITY_NAME +  " e  WHERE e.userId=?1 AND " +
            "e.start=?2";
    private static final String STARTING_BETWEEN = "SELECT e FROM " + ENTITY_NAME +  " e  WHERE e.userId=?1 AND " +
            "e.start >= ?2 AND e.start <=?3  ORDER BY e.start ASC" ;
    private static final String DELETE_WITH_USER_ID = "DELETE from " + ENTITY_NAME + " e WHERE e.userId=?1";

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static Logger logger = LoggerFactory.getLogger(OrganizerEventDao.class);
    
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public void saveEvent(OrganizerEvent event) {
        logger.info("Merging " + event);
        entityManager.merge(event);
    }
    
    public void removeEvent(int id) {
        logger.info("Removing  OrganizerEntry id:" + id);
        OrganizerEvent oe = entityManager.find(OrganizerEvent.class, id);
        entityManager.remove(oe);
        
    }
    
    public int saveNewEvent(OrganizerEvent event) {
        logger.info("Persisting " + event);
        entityManager.persist(event);
        logger.info("Persisted with id " + event.getId());
        return event.getId();
    }
    
   public void removeAllUserEvents(int userId) {
       logger.info("Deleting all events with userId=" + userId);
       Query query = entityManager.createQuery(DELETE_WITH_USER_ID);
       query.setParameter(1, userId);
       query.executeUpdate();
   }
    
    public OrganizerEvent retrieveEvents(int userId, Date date) {
        
        logger.info("Retrieving Event for user " + userId + " time " + DATE_FORMAT.format(date));
        
        TypedQuery<OrganizerEvent> q = entityManager.createQuery(EVENT_STARTING, OrganizerEvent.class);
        q.setParameter(1,userId).setParameter(2, date);
        OrganizerEvent e = q.getSingleResult();
        
        return e;
    }

    
    public List<OrganizerEvent> retrieveEventsBetween(int userId, Date startDate, Date endDate){
        
        logger.info("Retrieving Events for user " + userId + " starting between " 
                +DATE_FORMAT.format(startDate) + 
                " and " + DATE_FORMAT.format(endDate));
        
        TypedQuery<OrganizerEvent> q = entityManager.createQuery(STARTING_BETWEEN, OrganizerEvent.class );
        q.setParameter(1,userId).setParameter(2, startDate).setParameter(3, endDate);
        
        logger.info("Found " + q.getResultList().size() + " results.");
        
        return q.getResultList();
    }
    

}
