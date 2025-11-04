package dat.daos;

import dat.entities.Candidate;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CandidateDAO extends AbstractDAO<Candidate, Long> {
    private static final Logger LOG = LoggerFactory.getLogger(CandidateDAO.class);
    public CandidateDAO(EntityManagerFactory emf) {
        super(emf, "SELECT c FROM Candidate c", LOG);
    }
    @Override Class<Candidate> persistenceClass() { return Candidate.class; }
}