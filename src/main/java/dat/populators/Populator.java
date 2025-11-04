package dat.populators;

import dat.daos.CandidateDAO;
import dat.daos.CandidateSkillDAO;
import dat.daos.SkillDAO;
import dat.entities.Candidate;
import dat.entities.CandidateSkill;
import dat.entities.Skill;
import dat.enums.SkillCategory;
import jakarta.persistence.EntityManagerFactory;

public class Populator {

    public static void run(EntityManagerFactory emf) {
        // 0) Ryd tabeller og nulstil auto-increment, så IDs bliver 1,2,3...
        var em = emf.createEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            // Postgres: én linje er nok:
            em.createNativeQuery("TRUNCATE TABLE candidate_skills, candidates, skills RESTART IDENTITY CASCADE").executeUpdate();
            tx.commit();
        } finally {
            em.close();
        }
        var candidateDAO = new CandidateDAO(emf);
        var skillDAO = new SkillDAO(emf);
        var candidateSkillDAO = new CandidateSkillDAO(emf);
        // 1) Skills (IDs bliver: 1..4)
        Skill java = skillDAO.create(Skill.builder().name("Java")
                .description("General purpose")
                .category(SkillCategory.PROG_LANG).build());
        Skill js = skillDAO.create(Skill.builder()
                .name("JavaScript").description("Frontend & backend")
                .category(SkillCategory.PROG_LANG).build());
        Skill pg = skillDAO.create(Skill.builder()
                .name("PostgreSQL").description("Relational DB")
                .category(SkillCategory.DB).build());
        Skill docker = skillDAO.create(Skill.builder().name("Docker")
                .description("Containerization").category(SkillCategory.DEVOPS).build());
        // 2) Candidates
        Candidate anna = candidateDAO.create(Candidate.builder().name("Anna Larsen").phone("11111111").education("Software Engineer").build());
        Candidate bo = candidateDAO.create(Candidate.builder().name("Bo Jensen").phone("22222222").education("Datamatiker").build());
        Candidate carla = candidateDAO.create(Candidate.builder().name("Carla Christensen").phone("33333333").education("Datamatiker").build());
        Candidate david = candidateDAO.create(Candidate.builder().name("David Hansen").phone("44444444").education("Professionsbachelor").build());
        // 3) Links
        candidateSkillDAO.create(CandidateSkill.builder().candidate(anna).skill(java).build());    // Anna: Java
        candidateSkillDAO.create(CandidateSkill.builder().candidate(anna).skill(pg).build());      // Anna: PostgreSQL
        candidateSkillDAO.create(CandidateSkill.builder().candidate(bo).skill(js).build());        // Bo:   JavaScript
        candidateSkillDAO.create(CandidateSkill.builder().candidate(bo).skill(docker).build());    // Bo:   Docker
        // Carla og David starter uden skills
    }
}
