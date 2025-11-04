package dat.daos;

import dat.entities.CandidateSkill;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CandidateSkillDAO extends AbstractDAO<CandidateSkill, Long> {
    private static final Logger LOG = LoggerFactory.getLogger(CandidateSkillDAO.class);

    public CandidateSkillDAO(EntityManagerFactory emf) {
        super(emf, "SELECT cs FROM CandidateSkill cs", LOG);
    }

    @Override
    Class<CandidateSkill> persistenceClass() {
        return CandidateSkill.class;
    }
}
