package org.rs.DAO;

import org.rs.entity.Event;
import org.rs.entity.EventCategory;
import org.rs.entity.EventSubCategory;
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
