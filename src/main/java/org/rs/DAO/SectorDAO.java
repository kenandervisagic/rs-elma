package org.rs.DAO;

import org.rs.entity.Sector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class SectorDAO {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ticketReservationPU");

    public static List<Sector> getSectorsForLocation(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            // Create a query to fetch sectors based on the location ID
            TypedQuery<Sector> query = em.createQuery(
                    "SELECT s FROM Sector s WHERE s.location.id = :locationId", Sector.class);
            query.setParameter("locationId", id);

            // Execute the query and return the list of sectors
            return query.getResultList();
        } finally {
            // Close the EntityManager to release resources
            em.close();
        }
    }
}
