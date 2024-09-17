package org.rs.DAO;


import org.rs.entity.Event;
import org.rs.entity.Ticket;
import org.rs.entity.User;
import org.rs.util.JpaUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.rs.util.JpaUtil.getEntityManager;

public class TicketDAO {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticketReservationPU");

    public static void addTicket(Ticket ticket, Integer status) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Set the status before persisting
            ticket.setStatus(status);

            entityManager.persist(ticket);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
    public static int getUserTicketsForEvent(User user, Event event) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(t) FROM Ticket t WHERE t.user = :user AND t.event = :event AND t.status IN (0,1)", Long.class);
            query.setParameter("user", user);
            query.setParameter("event", event);
            Long count = query.getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }
    public static int getPurchasedUserTicketsForEvent(User user, Event event) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(t) FROM Ticket t WHERE t.user = :user AND t.event = :event AND t.status = 0", Long.class);
            query.setParameter("user", user);
            query.setParameter("event", event);
            Long count = query.getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }

    public static int getBasketUserTicketsForEvent(User user, Event event) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(t) FROM Ticket t WHERE t.user = :user AND t.event = :event AND t.status = 2", Long.class);
            query.setParameter("user", user);
            query.setParameter("event", event);
            Long count = query.getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            em.close();
        }
    }
    public static List<Ticket> getUserTickets(User user, int status) {
        EntityManager entityManager = getEntityManager();
        try {
            // Fetch tickets based on the user
            TypedQuery<Ticket> query = entityManager.createQuery(
                    "SELECT t FROM Ticket t WHERE t.user = :user and t.status = :status", Ticket.class);
            query.setParameter("user", user);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }
    public static List<Ticket> getTicketsByStatus0(User user, int page, int size) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ticket> query = em.createQuery(
                    "SELECT t FROM Ticket t WHERE t.user = :user AND t.status = 0", Ticket.class);
            query.setParameter("user", user);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static int getTotalNumberOfTicketsByStatus0(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(t) FROM Ticket t WHERE t.user = :user AND t.status = 0", Long.class);
            query.setParameter("user", user);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    public static int getSoldEventTicketsNumber(Event event) {
        EntityManager entityManager = getEntityManager();
        try {
            // Fetch the count of sold tickets for the event
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Ticket t WHERE t.event = :event AND t.user IS NOT NULL AND t.status != 2", Long.class);
            query.setParameter("event", event);

            Long soldTickets = query.getSingleResult();
            return soldTickets != null ? soldTickets.intValue() : 0;
        } finally {
            entityManager.close();
        }
    }

    public static boolean deleteTicket(Ticket ticket) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Ticket managedTicket = em.find(Ticket.class, ticket.getId());
            if (managedTicket != null) {
                em.remove(managedTicket);
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Update the status of a ticket (set status to 0 when purchased)
    public static boolean updateTicketStatus(Ticket ticket) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Ticket managedTicket = em.find(Ticket.class, ticket.getId());
            if (managedTicket != null) {
                managedTicket.setStatus(ticket.getStatus()); // Update the status
                managedTicket.setSeatNumber(ticket.getSeatNumber());
                em.merge(managedTicket);
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    public static boolean deleteTicketsByStatus2(User user) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Query query = em.createQuery("DELETE FROM Ticket t WHERE t.user = :user and t.status = :status");
            query.setParameter("user", user);
            query.setParameter("status", 2);
            int deletedCount = query.executeUpdate();
            transaction.commit();
            return deletedCount > 0;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    public static boolean purchaseUserTickets(User user) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Query query = em.createQuery("UPDATE Ticket t SET t.status = :status WHERE t.user = :user");
            query.setParameter("status", 0);
            query.setParameter("user", user);
            int updatedCount = query.executeUpdate();
            transaction.commit();
            return updatedCount > 0;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}
