package dat.daos;

import dat.entities.Candidate;
import dat.entities.CandidateSkill;
import dat.entities.Skill;
import dat.exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class CandidateDAO extends AbstractDAO<Candidate, Long> {
    private static final Logger LOG = LoggerFactory.getLogger(CandidateDAO.class);

    public CandidateDAO(EntityManagerFactory emf) {
        // baseQuery bruges kun af AbstractDAO.readAll(), men vi overrider readAll/read alligevel
        super(emf, "SELECT c FROM Candidate c", LOG);
    }

    @Override
    Class<Candidate> persistenceClass() {
        return Candidate.class;
    }

    /* ========= READS med JOIN FETCH (fixer LazyInitialization) ========= */

    @Override
    public List<Candidate> readAll() throws DatabaseException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("""
                        SELECT DISTINCT c
                        FROM Candidate c
                        LEFT JOIN FETCH c.candidateSkills cs
                        LEFT JOIN FETCH cs.skill s
                    """, Candidate.class).getResultList();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new DatabaseException(500, e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Candidate> read(Long id) throws DatabaseException {
        EntityManager em = emf.createEntityManager();
        try {
            var list = em.createQuery("""
                                SELECT c
                                FROM Candidate c
                                LEFT JOIN FETCH c.candidateSkills cs
                                LEFT JOIN FETCH cs.skill s
                                WHERE c.id = :id
                            """, Candidate.class)
                    .setParameter("id", id)
                    .getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new DatabaseException(500, e.getMessage());
        } finally {
            em.close();
        }
    }

    /* ========= UPDATE der returnerer den MERGEDE entity (ikke "existing") ========= */

    @Override
    public Candidate update(Long id, Candidate detachedWithChanges) {
        var em = emf.createEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            Candidate merged = em.merge(detachedWithChanges);
            tx.commit();
            return read(id).orElse(merged);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new dat.exceptions.DatabaseException(500, e.getMessage());
        } finally {
            em.close();
        }
    }

    /* ========= DELETE der rydder join-rækker først (FK-safe) ========= */

    @Override
    public boolean delete(Long id) throws DatabaseException {
        EntityManager em = emf.createEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            // 1) Slet links i join-tabellen
            em.createQuery("""
                        DELETE FROM CandidateSkill cs
                        WHERE cs.candidate.id = :cid
                    """).setParameter("cid", id).executeUpdate();
            // 2) Slet kandidaten
            Candidate c = em.find(Candidate.class, id);
            if (c != null) em.remove(c);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            LOG.error(e.getMessage());
            throw new DatabaseException(500, e.getMessage());
        } finally {
            em.close();
        }
    }

    /* ========= LINK / UNLINK ========= */

    /**
     * Linker en skill til en kandidat. Gør intet hvis link allerede findes.
     */
    public void addSkillToCandidate(Long candidateId, Long skillId) {
        EntityManager em = emf.createEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            Candidate c = em.find(Candidate.class, candidateId);
            Skill s = em.find(Skill.class, skillId);
            if (c == null || s == null) {
                throw new DatabaseException(404, "Candidate or Skill not found");
            }
            Long exists = em.createQuery("""
                                SELECT COUNT(cs)
                                FROM CandidateSkill cs
                                WHERE cs.candidate.id = :cid AND cs.skill.id = :sid
                            """, Long.class)
                    .setParameter("cid", candidateId)
                    .setParameter("sid", skillId)
                    .getSingleResult();
            if (exists == 0) {
                CandidateSkill link = CandidateSkill.builder()
                        .candidate(c)
                        .skill(s)
                        .build();
                em.persist(link);
                // hold in-memory sider i sync (hvis dine helpers ikke gør det)
                c.getCandidateSkills().add(link);
                s.getCandidateSkills().add(link);
            }
            tx.commit();
        } catch (DatabaseException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            LOG.error(e.getMessage());
            throw new DatabaseException(500, "DB error linking skill: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Fjerner link mellem kandidat og skill. Gør intet hvis link ikke findes.
     */
    public void removeSkillFromCandidate(Long candidateId, Long skillId) {
        EntityManager em = emf.createEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            var links = em.createQuery("""
                                SELECT cs
                                FROM CandidateSkill cs
                                WHERE cs.candidate.id = :cid AND cs.skill.id = :sid
                            """, CandidateSkill.class)
                    .setParameter("cid", candidateId)
                    .setParameter("sid", skillId)
                    .getResultList();
            for (var cs : links) {
                em.remove(em.contains(cs) ? cs : em.merge(cs));
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            LOG.error(e.getMessage());
            throw new DatabaseException(500, "DB error unlinking skill: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
