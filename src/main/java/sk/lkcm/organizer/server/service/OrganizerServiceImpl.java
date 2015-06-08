package sk.lkcm.organizer.server.service;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sk.lkcm.organizer.server.dao.OrganizerEventDao;
import sk.lkcm.organizer.server.dao.UserDao;
import sk.lkcm.organizer.server.entity.OrganizerEvent;
import sk.lkcm.organizer.server.entity.User;
import sk.lkcm.organizer.shared.EventDTO;
import sk.lkcm.organizer.shared.NoSessionException;
import sk.lkcm.organizer.shared.OrganizerService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("organizer-service")
@RemoteServiceRelativePath("organizerService")
public class OrganizerServiceImpl implements OrganizerService, GwtService {

    private final static Logger logger = LoggerFactory
            .getLogger(OrganizerServiceImpl.class);
    private final static String USER_ATTRIBUTE = "organizer.user";
    private final static String IS_DEMO = "organizer.demo";
    private final static String DEMO_USER_NAME = "demo_user";

    @Inject
    private OrganizerEventDao organizerDao;
    @Inject
    private UserDao userDao;

    private ThreadLocal<HttpServletRequest> localRequest = new ThreadLocal<>();

    @Override
    public void prepareForRpcCall(HttpServletRequest request) {
        localRequest.set(request);
    }

    @Override
    public void rpcCallDone() {
        localRequest.remove();
    }

    @Override
    public List<EventDTO> getEvents(Date start, Date end) {
        logger.info("Getting events from " + start + " to " + end);
        int userId = getUserFromSession().getId();
        List<OrganizerEvent> events = organizerDao.retrieveEventsBetween(
                userId, start, end);
        List<EventDTO> dtos = new ArrayList<>();
        for (OrganizerEvent event : events)
            dtos.add(createEventDTO(event));
        return dtos;
    }

    @Override
    public void saveEvent(EventDTO eventDto) {
        logger.info("Saving event " + eventDto);
        int userId = getUserFromSession().getId();
        OrganizerEvent event = createOrganizerEvent(eventDto, userId);
        organizerDao.saveEvent(event);
    }

    @Override
    public int createNewEvent(EventDTO eventDto) {
        logger.info("Creating event " + eventDto);
        int userId = getUserFromSession().getId();
        OrganizerEvent event = createOrganizerEvent(eventDto, userId);
        return organizerDao.saveNewEvent(event);
    }

    @Override
    public void deleteEvent(int id) {
        logger.info("Deleting event with id " + id);
        organizerDao.removeEvent(id);
    }

    @Override
    public String logUserIn(String name, String password) {
        logger.info("Request for login: " + name + ", " + password);
        User user = userDao.selectUser(name);

        if (user == null) {
            logger.info("User does not exist");
            return OrganizerService.NO_USER;
        }

        if (!user.getPassword().equals(password)) {
            logger.info("User password does not match");
            return OrganizerService.BAD_PASSWORD;
        }
        logger.info("User logged in successfully");

        HttpSession session = localRequest.get().getSession();
        session.setAttribute(USER_ATTRIBUTE, user);

        return name;
    }

    public void logOut() {
        checkSession();
        HttpSession s = localRequest.get().getSession(false);
        logger.info("Loging out user");

        Boolean isDemo = (Boolean) s.getAttribute(IS_DEMO);

        // If this was demo session then delete the user
        if (isDemo != null && isDemo) {
            logger.info("Cleaning up temp demo user data");
            User user = getUserFromSession();
            userDao.deleteUser(user);
            organizerDao.removeAllUserEvents(user.getId());
        }

        s.invalidate();
    }

    public User getUserFromSession() {
        HttpSession s = localRequest.get().getSession(false);
        if (s == null) {
            logger.info("Session is missing");
            throw new NoSessionException();
        }

        User user = (User) s.getAttribute(USER_ATTRIBUTE);
        if (user == null)
            throw new IllegalStateException(
                    "No user object in the current session");

        return user;
    }

    /**
     * Determines the logged user name.
     *
     * @return the logged user name
     */
    @Override
    public String getLoggedUser() {
        logger.info("Checking if user is logged in");
        HttpSession s = localRequest.get().getSession(false);
        if (s == null) {
            logger.info("A session does not exist ");
            return OrganizerService.NO_USER;
        } else {
            Object o = s.getAttribute(USER_ATTRIBUTE);
            if (o == null) {
                logger.info("A session does not contain " + USER_ATTRIBUTE
                        + " attribute");
                return OrganizerService.NO_USER;
            } else {
                User user = (User) o;
                logger.info("A session exists username='" + user.getName()
                        + "', id=" + user.getId());
                return user.getName();
            }
        }
    }

    /**
     * Verifies if the session is valid.
     *
     * @throws NoSessionException
     *             if the session does not exist
     */
    private void checkSession() {
        HttpSession s = localRequest.get().getSession(false);
        if (s == null) {
            logger.info("Session is missing");
            throw new NoSessionException();
        }
    }

    @Override
    public String runDemo() {
        logger.debug("running demo");
        User user = createTmpDemoUser();
        userDao.saveUser(user);
        fillDemoEvents(user);

        HttpSession session = localRequest.get().getSession(true);
        session.setAttribute(USER_ATTRIBUTE, user);
        session.setAttribute(IS_DEMO, true);

        return user.getName();
    }

    /**
     * Creates demo events assigned to a given user account. The demo events are
     * copied from the canonical demo user which must be created in the
     * database, if not exception is thrown. This method is intended to be used
     * with a temporary demo user.
     *
     *
     * @param user
     *            a user account
     * @throws IllegalStateException
     *             if the canonical demo user is not present in the database
     */
    private void fillDemoEvents(User user) {
        User theDemoUser = userDao.selectUser(DEMO_USER_NAME);

        if (theDemoUser == null)
            throw new IllegalStateException(
                    "No demo user in the database. Cannot run demo.");

        // Retrieve all the events from the Demo user.
        Date start = new DateTime().withYear(0).toDate();
        Date end = new DateTime().withYear(3000).toDate();
        List<OrganizerEvent> allEvents = organizerDao.retrieveEventsBetween(
                theDemoUser.getId(), start, end);

        if (allEvents.size() == 0)
            return;

        OrganizerEvent firstEvent = allEvents.get(0);

        DateMidnight firstDay = new DateMidnight(firstEvent.getStart());
        DateMidnight today = new DateMidnight().withDayOfWeek(1);

        // Will be negative if today is after firstDay and positive otherwise.
        int dayDiff = Days.daysBetween(firstDay
                , today).getDays();
        //Shift it all one week sooner
        dayDiff -= 7;

        for (OrganizerEvent event : allEvents) {
            event.setId(0);
            // Shift the date by the difference in the direction where today is.
            DateTime startDate = new DateTime(event.getStart())
                    .plusDays(dayDiff);
            DateTime endDate = null;
            if (event.getEnd() != null)
                endDate = new DateTime(event.getEnd()).plusDays(dayDiff);

            event.setStart(startDate.toDate());
            if (endDate != null)
                event.setEnd(endDate.toDate());

            event.setUserId(user.getId());
            organizerDao.saveNewEvent(event);
        }
    }

    /**
     * Creates a temporary demo user for the duration of the demo session.
     *
     * @return new demo user account
     */
    private User createTmpDemoUser() {
        User user = new User();

        Random ran = new Random(new Date().getTime());
        int id = new Date().hashCode() + ran.nextInt(10000);
        id = id % 1000000;
        String name = "demo-user-" + id;

        user.setName(name);
        user.setPassword(name);

        return user;
    }

    /** ********************* Utils *********************** */
    private static EventDTO createEventDTO(OrganizerEvent event) {
        EventDTO dto = new EventDTO();
        dto.setName(event.getName());
        dto.setTimeStart(event.getStart());
        dto.setTimeEnd(event.getEnd());
        dto.setId(event.getId());
        return dto;
    }

    private static OrganizerEvent createOrganizerEvent(EventDTO dto, int userId) {
        OrganizerEvent event = new OrganizerEvent();
        event.setName(dto.getName());
        event.setStart(dto.getTimeStart());
        event.setEnd(dto.getTimeEnd());
        event.setId(dto.getId());
        event.setUserId(userId);
        return event;
    }
}
