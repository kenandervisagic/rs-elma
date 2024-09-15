package org.rs.DAO;

import org.rs.entity.*;
import org.rs.util.JpaUtil;

import javax.persistence.*;
import java.util.List;

public class EventDAO {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticketReservationPU");

    public static EventCategory getCategoryByName(String categoryName) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EventCategory> query = em.createQuery("SELECT c FROM EventCategory c WHERE c.categoryName = :name", EventCategory.class);
            query.setParameter("name", categoryName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Handle no result case
        } finally {
            em.close();
        }
    }
    public static boolean saveEventRequest(EventRequest event) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        boolean isSuccess = false;
        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.persist(event);
            transaction.commit();

            isSuccess = true; // Operation was successful
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Rollback the transaction in case of an error
            }
            e.printStackTrace(); // Log or handle the exception as needed
        } finally {
            if (em != null) {
                em.close(); // Ensure EntityManager is closed
            }
        }

        return isSuccess;
    }

    public static boolean updateEventRequest(EventRequest event) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(event); // Use merge to update an existing entity
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public Event findEventById(Long id) {
        EntityManager em = emf.createEntityManager();
        Event event = em.find(Event.class, id);
        em.close();
        return event;
    }

    public Event findEventByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Event> eventName = em.createQuery("SELECT e FROM Event e WHERE e.eventName = :name", Event.class).
                    setParameter("name", name).
                    getResultList();
            return eventName.isEmpty() ? null : eventName.get(0);
        } finally {
            em.close();
        }
    }

    public List<Event> findEventsByOrganizer(Long organizerId) {
        EntityManager em = emf.createEntityManager();
        List<Event> eventOrganizers = em.createQuery("SELECT e FROM Event e WHERE e.organizer.id = :organizerId", Event.class)
                .setParameter("organizerId", organizerId)
                .getResultList();
        em.close();
        return eventOrganizers;
    }

    public List<Event> findAllEvents() {
        EntityManager em = emf.createEntityManager();
        List<Event> allEvents = em.createQuery("SELECT e FROM Event e", Event.class).getResultList();
        em.close();
        return allEvents;
    }

    public List<Event> findAllEventsByOrganizer(EventOrganizer organizer) {
        EntityManager em = emf.createEntityManager();
        try {
            // Pretpostavka: Organizator ima identifikator (npr. id) koji povezuje događaje sa organizatorom
            List<Event> allEventOrganizers = em.createQuery("SELECT e FROM Event e WHERE e.id = :id", Event.class).
                    setParameter("id", organizer.getId()).
                    getResultList();
            return allEventOrganizers;
        } finally {
            em.close();
        }
    }

    public void updateEvent(Event event) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(event);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void deleteEvent(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Event event = em.find(Event.class, id);
        if (event != null) {
            em.remove(event);
        }
        em.getTransaction().commit();
        em.close();
    }

    public static void approveRequest(EventRequest eventRequest) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Create a new User based on the Request
            Event event = new Event();
            event.setOrganizer(eventRequest.getOrganizer());
            event.setCategory(eventRequest.getCategory());
            event.setSubCategory(eventRequest.getSubCategory());
            event.setEventName(eventRequest.getEventName());
            event.setDescription(eventRequest.getDescription());
            event.setEventDate(eventRequest.getEventDate());
            event.setEventTime(eventRequest.getEventTime());
            event.setMaxTickets(eventRequest.getMaxTickets());
            event.setLocationEntity(eventRequest.getLocationEntity());
            event.setTickets(eventRequest.getTickets());

            em.persist(event);
            em.remove(em.contains(eventRequest) ? eventRequest : em.merge(eventRequest));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void rejectRequest(EventRequest request) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(request) ? request : em.merge(request));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static EventSubCategory getSubCategoryByName(String subCategoryName) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EventSubCategory> query = em.createQuery("SELECT sc FROM EventSubCategory sc WHERE sc.subCategoryName = :name", EventSubCategory.class);
            query.setParameter("name", subCategoryName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Handle no result case
        } finally {
            em.close();
        }
    }

    public static List<Event> getEventsByFilters(String categoryName, String subCategoryName, int page, int size) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder queryStr = new StringBuilder("SELECT e FROM Event e WHERE 1=1");

            EventCategory category = (categoryName != null) ? getCategoryByName(categoryName) : null;
            EventSubCategory subCategory = (subCategoryName != null) ? getSubCategoryByName(subCategoryName) : null;

            if (category != null) {
                queryStr.append(" AND e.category = :category");
            }
            if (subCategory != null) {
                queryStr.append(" AND e.subCategory = :subCategory");
            }
            queryStr.append(" ORDER BY e.eventDate DESC");

            TypedQuery<Event> query = em.createQuery(queryStr.toString(), Event.class);

            if (category != null) {
                query.setParameter("category", category);
            }
            if (subCategory != null) {
                query.setParameter("subCategory", subCategory);
            }

            query.setFirstResult(page * size);
            query.setMaxResults(size);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static int getTotalNumberOfFilteredEvents(String categoryName, String subCategoryName) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder queryStr = new StringBuilder("SELECT COUNT(e) FROM Event e WHERE 1=1");

            EventCategory category = (categoryName != null) ? getCategoryByName(categoryName) : null;
            EventSubCategory subCategory = (subCategoryName != null) ? getSubCategoryByName(subCategoryName) : null;

            if (category != null) {
                queryStr.append(" AND e.category = :category");
            }
            if (subCategory != null) {
                queryStr.append(" AND e.subCategory = :subCategory");
            }

            TypedQuery<Long> query = em.createQuery(queryStr.toString(), Long.class);

            if (category != null) {
                query.setParameter("category", category);
            }
            if (subCategory != null) {
                query.setParameter("subCategory", subCategory);
            }

            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    public static List<EventCategory> getAllCategories() {
        EntityManager em = emf.createEntityManager();
        List<EventCategory> categories = null;
        try {
            TypedQuery<EventCategory> query = em.createQuery("SELECT c FROM EventCategory c", EventCategory.class);
            categories = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions or use a logging framework
        } finally {
            em.close();
        }
        return categories;
    }

    public static List<EventRequest> getAllEventRequest() {
        EntityManager em = emf.createEntityManager();
        List<EventRequest> requests = null;
        try {
            TypedQuery<EventRequest> query = em.createQuery("SELECT c FROM EventRequest c", EventRequest.class);
            requests = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions or use a logging framework
        } finally {
            em.close();
        }
        return requests;
    }

    public static List<EventSubCategory> getSubCategoriesByCategory(String categoryName) {
        EntityManager em = emf.createEntityManager();
        List<EventSubCategory> subCategories = null;
        try {
            // Find the category ID based on category name
            TypedQuery<EventCategory> categoryQuery = em.createQuery(
                    "SELECT c FROM EventCategory c WHERE c.categoryName = :categoryName",
                    EventCategory.class);
            categoryQuery.setParameter("categoryName", categoryName);
            EventCategory category = categoryQuery.getSingleResult();

            // Find subcategories based on category ID
            TypedQuery<EventSubCategory> subCategoryQuery = em.createQuery(
                    "SELECT sc FROM EventSubCategory sc WHERE sc.category = :category",
                    EventSubCategory.class);
            subCategoryQuery.setParameter("category", category);
            subCategories = subCategoryQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions or use a logging framework
        } finally {
            em.close();
        }
        return subCategories;
    }

    public static int getTotalNumberOfEvents() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(e) FROM Event e", Long.class).getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }

    public static List<Event> getEventsByPage(int page, int pageSize) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Event> events = em.createQuery("SELECT e FROM Event e", Event.class)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        em.close();
        return events;
    }

    public static List<Event> getTop4Events() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e ORDER BY e.eventDate DESC", Event.class);
        query.setMaxResults(4);
        return query.getResultList();
    }

    public static List<Event> searchEventsByCriterion(String criterion, String value, int page, int size) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT e FROM Event e WHERE 1=1");

            parseCriterion(criterion, jpql);

            jpql.append(" ORDER BY e.eventDate DESC"); // You can adjust the sorting as needed

            TypedQuery<Event> query = em.createQuery(jpql.toString(), Event.class);
            if (value != null && !value.trim().isEmpty()) {
                query.setParameter("value", "%" + value + "%");
            }
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    private static void parseCriterion(String criterion, StringBuilder jpql) {
        if ("Naziv".equals(criterion)) {
            jpql.append(" AND e.eventName LIKE :value");
        } else if ("Vrsta".equals(criterion)) {
            jpql.append(" AND e.category.categoryName LIKE :value");
        } else if ("Podvrsta".equals(criterion)) {
            jpql.append(" AND e.subCategory.subCategoryName LIKE :value");
        } else if ("Lokacija".equals(criterion)) {
            jpql.append(" AND e.locationEntity.locationName LIKE :value");
        } else if ("Mjesto".equals(criterion)) {
            jpql.append(" AND e.place LIKE :value");
        }
    }

    public static int getTotalNumberOfFilteredEventsByCriterion(String criterion, String value) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(e) FROM Event e WHERE 1=1");

            parseCriterion(criterion, jpql);

            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
            if (value != null && !value.trim().isEmpty()) {
                query.setParameter("value", "%" + value + "%");
            }
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

}
