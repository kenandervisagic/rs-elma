package org.rs.DAO;

import org.rs.entity.Location;
import org.rs.entity.Place;

import javax.persistence.*;
import java.util.List;

import static org.rs.util.JpaUtil.getEntityManager;

public class PlaceDAO {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticketReservationPU");

    public static void addPlace(Place place) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(place);
            transaction.commit();
            System.out.println("Place added successfully");
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error adding place: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static List<Place> getAllPlaces() {
        EntityManager em = emf.createEntityManager();
        List<Place> places = null;
        try {
            TypedQuery<Place> query = em.createQuery("SELECT p FROM Place p", Place.class);
            places = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions or use a logging framework
        } finally {
            em.close();
        }
        return places;
    }

    public static Place getPlaceByLocation(Location location) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();

            // Assuming there is a relationship between Place and Location
            String queryString = "SELECT p FROM Place p WHERE :location MEMBER OF p.locations";
            TypedQuery<Place> query = em.createQuery(queryString, Place.class);
            query.setParameter("location", location);

            return query.getSingleResult();
        } catch (NoResultException e) {
            // Handle the case where no result is found
            System.out.println("No Place found for the provided location.");
            return null; // Or handle it as per your requirement
        } finally {
            if (em != null) {
                em.close(); // Ensure EntityManager is closed properly
            }
        }
    }
}
