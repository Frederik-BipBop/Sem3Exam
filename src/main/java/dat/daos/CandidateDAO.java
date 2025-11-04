package dat.daos;

import dat.entities.Candidate;
import dat.entities.CandidateSkill;
import dat.entities.Skill;
import dat.exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CandidateDAO extends AbstractDAO<Candidate, Long> {
    private static final Logger LOG = LoggerFactory.getLogger(CandidateDAO.class);
    public CandidateDAO(EntityManagerFactory emf) {
        super(emf, "SELECT c FROM Candidate c", LOG);
    }
    @Override Class<Candidate> persistenceClass() { return Candidate.class; }

    /* Linker en skill til en kandidat. Gør intet hvis link allerede findes. */
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

            // allerede linket? så gør ingenting
            boolean exists = c.getCandidateSkills().stream()
                    .anyMatch(cs -> cs.getSkill().getId().equals(skillId));
            if (!exists) {
                CandidateSkill link = CandidateSkill.builder().candidate(c).skill(s).build();
                c.addSkill(link);   // holder begge sider i sync via helper
                s.addCandidateSkill(link);
                em.persist(link);
            }

            tx.commit();
        } catch (DatabaseException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new DatabaseException(500, "DB error linking skill: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /* Fjerner link mellem kandidat og skill. Gør intet hvis link ikke findes. */
    public void removeSkillFromCandidate(Long candidateId, Long skillId) {
        EntityManager em = emf.createEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();

            Candidate c = em.find(Candidate.class, candidateId);
            if (c == null) throw new DatabaseException(404, "Candidate not found");

            CandidateSkill link = c.getCandidateSkills().stream()
                    .filter(cs -> cs.getSkill().getId().equals(skillId))
                    .findFirst().orElse(null);

            if (link != null) {
                c.removeSkill(link);
                em.remove(em.contains(link) ? link : em.merge(link));
            }

            tx.commit();
        } catch (DatabaseException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new DatabaseException(500, "DB error unlinking skill: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}