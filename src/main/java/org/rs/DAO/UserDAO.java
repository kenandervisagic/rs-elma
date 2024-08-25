package org.rs.DAO;

import org.rs.entity.*;

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
            Role role = new Role();
            role.setRoleName(request.getRoleName());
            user.setRole(role);

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
}
