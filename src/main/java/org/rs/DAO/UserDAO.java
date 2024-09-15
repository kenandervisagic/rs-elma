package org.rs.DAO;

import org.rs.entity.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static org.rs.util.JpaUtil.getEntityManager;

public class UserDAO {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticketReservationPU");

    public static User getUserByUsernameAndPass(String username, String password) {
        EntityManager em = emf.createEntityManager();
        User user;

        try {
            String queryStr = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
            TypedQuery<User> query = em.createQuery(queryStr, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            user = query.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace(); // Handle exceptions or use a logging framework
            return null;
        } finally {
            em.close();
        }
        return user;
    }


    public static void addUserRequest(UserRequest userRequest) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(userRequest);
            transaction.commit();
            System.out.println("User request added successfully");
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error adding user request: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static List<UserRequest> getAllRequests() {
        EntityManager em = emf.createEntityManager();
        List<UserRequest> requests = null;
        try {
            TypedQuery<UserRequest> query = em.createQuery("SELECT p FROM UserRequest p", UserRequest.class);
            requests = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions or use a logging framework
        } finally {
            em.close();
        }
        return requests;
    }


    public static void approveRequest(UserRequest request) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Create a new User based on the Request
            User user = new User();
            user.setFullName(request.getFullName());
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setBalance(0.00);
            user.setRole(request.getRole());

            // Persist the User and remove the Request
            em.persist(user);
            em.remove(em.contains(request) ? request : em.merge(request));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void rejectRequest(UserRequest request) {
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

    public static void changeBalance(User user, double newBalance) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Find the user by ID
            User managedUser = em.find(User.class, user.getId());
            if (managedUser != null) {
                // Update the balance
                managedUser.setBalance(user.getBalance() - newBalance);

                // Merge changes to the database
                em.merge(managedUser);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Rollback the transaction in case of error
            }
            e.printStackTrace();
        }
    }

    public static void addLocationWithSectors(Location location, Set<Sector> sectors) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Persist the Location
            em.persist(location);

            // Persist each Sector, setting the Location reference
            for (Sector sector : sectors) {
                sector.setLocation(location);
                em.persist(sector);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
