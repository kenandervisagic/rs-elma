package org.rs.DAO;

import org.rs.entity.Location;
import org.rs.entity.Place;
import org.rs.entity.User;

import javax.persistence.*;

import java.util.List;

import static org.rs.util.JpaUtil.getEntityManager;

public class UserDAO {
    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticketReservationPU");


    public static User getUserByUsernameAndPass(String username, String password) {
        EntityManager em = emf.createEntityManager();
        User user = null;

        try {
            String queryStr = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
            TypedQuery<User> query = em.createQuery(queryStr, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            user = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions or use a logging framework
        } finally {
            em.close();
        }
        return user;
    }

    public static void addUser(User user) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(user);
            transaction.commit();
            System.out.println("User added successfully");
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

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
    public static void addLocation(Location location) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(location);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle exceptions or use a logging framework
        } finally {
            em.close();
        }
    }
}
