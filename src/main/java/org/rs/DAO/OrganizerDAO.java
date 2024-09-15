package org.rs.DAO;

import org.rs.entity.EventOrganizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class OrganizerDAO {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticketReservationPU");

    public void saveOrganizer(EventOrganizer organizer) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(organizer);
        em.getTransaction().commit();
        em.close();
    }

    public EventOrganizer findOrganizerById(Long id) {
        EntityManager em = emf.createEntityManager();
        EventOrganizer organizer = em.find(EventOrganizer.class, id);
        em.close();
        return organizer;
    }

    public EventOrganizer findOrganizerByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        EventOrganizer organizer = em.createQuery("SELECT o FROM EventOrganizer o WHERE o.email = :email", EventOrganizer.class)
                .setParameter("email", email)
                .getSingleResult();
        em.close();
        return organizer;
    }

    public List<EventOrganizer> findAllOrganizers() {
        EntityManager em = emf.createEntityManager();
        List<EventOrganizer> organizers = em.createQuery("SELECT o FROM EventOrganizer o", EventOrganizer.class).getResultList();
        em.close();
        return organizers;
    }

    public void deleteOrganizer(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        EventOrganizer organizer = em.find(EventOrganizer.class, id);
        if (organizer != null) {
            em.remove(organizer);
        }
        em.getTransaction().commit();
        em.close();
    }
}