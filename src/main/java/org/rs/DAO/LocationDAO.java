package org.rs.DAO;

import org.rs.entity.Location;

import javax.persistence.*;
import java.util.List;

public class LocationDAO {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticketReservationPU");

    public static List<Location> findAllLocations() {
        EntityManager em = emf.createEntityManager();
        List<Location> allLocations = em.createQuery("SELECT l FROM Location l", Location.class).getResultList();
        em.close();
        return allLocations;
    }

    public static Location getLocationByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            String queryString = "SELECT l FROM Location l WHERE l.locationName = :name";
            TypedQuery<Location> query = em.createQuery(queryString, Location.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Handle the case where no result is found
            // Return null or handle it as per your requirement
            return null;
        } finally {
            // Ensure that the EntityManager is closed properly if it's not managed externally
            em.close();
        }
    }
}
