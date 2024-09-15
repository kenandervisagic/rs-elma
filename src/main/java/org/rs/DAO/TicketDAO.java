package org.rs.DAO;


import org.rs.entity.Ticket;
import org.rs.entity.User;
import org.rs.util.JpaUtil;

import javax.persistence.*;
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

    public static List<Ticket> getUserTickets(User user) {
        EntityManager entityManager = getEntityManager();
        try {
            // Fetch tickets based on the user
            TypedQuery<Ticket> query = entityManager.createQuery(
                    "SELECT t FROM Ticket t WHERE t.user = :user and t.status = 2", Ticket.class);
            query.setParameter("user", user);
            return query.getResultList();
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
