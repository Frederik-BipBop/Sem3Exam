package dat.daos;

import dat.entities.Skill;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillDAO extends AbstractDAO<Skill, Long> {
    private static final Logger LOG = LoggerFactory.getLogger(SkillDAO.class);

    public SkillDAO(EntityManagerFactory emf) {
        super(emf, "SELECT s FROM Skill s", LOG);
    }

    @Override
    Class<Skill> persistenceClass() {
        return Skill.class;
    }
}
