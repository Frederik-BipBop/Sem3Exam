package dat.daos;

import dat.exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
@Getter
public abstract class AbstractDAO<t, i> implements IDAO<t, i> {
    @Getter
    private final Logger LOGGER;
    private EntityManagerFactory emf;
    private final String getAllQuery;

    public AbstractDAO(EntityManagerFactory emf, String getAllQuery, Logger LOGGER) {
        this.emf = emf;
        this.getAllQuery = getAllQuery;
        this.LOGGER = LOGGER;
    }

    @Override
    public Optional<t> read(i id) throws DatabaseException {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(persistenceClass(), id));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseException(500, e.getMessage());
        }
    }

    @Override
    public List<t> readAll() throws DatabaseException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<t> q = em.createQuery(
                    getAllQuery, persistenceClass()
            );
            return q.getResultList();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new DatabaseException(500, e.getMessage());
        }
    }

    @Override
    public t create(t t) throws DatabaseException {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                em.persist(t);
                em.getTransaction().commit();
                return t;
            } catch (Exception e) {
                em.getTransaction().rollback();
                LOGGER.error(e.getMessage());
                throw new DatabaseException(500, e.getMessage());
            }
        }
    }

    @Override
    public t update(i i, t t) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                t existing = em.find(persistenceClass(), i);
                if (existing == null) {
                    return null;
                }
                em.merge(t);
                em.getTransaction().commit();
                return existing;
            } catch (Exception e) {
                em.getTransaction().rollback();
                LOGGER.error(e.getMessage());
                throw new DatabaseException(500, e.getMessage());
            }
        }
    }

    @Override
    public boolean delete(i i) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                t existing = em.find(persistenceClass(), i);
                if (existing == null) {
                    throw new EntityNotFoundException("No " + persistenceClass().getName() + " was found with this id");
                }
                em.remove(existing);
                em.getTransaction().commit();
                return true;
            } catch (Exception e) {
                em.getTransaction().rollback();
                LOGGER.error(e.getMessage());
                throw new DatabaseException(500, e.getMessage());
            }
        }
    }

    abstract Class<t> persistenceClass();
}
