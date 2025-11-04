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
        var candidateDAO = new CandidateDAO(emf);
        var skillDAO = new SkillDAO(emf);
        var candidateSkillDAO = new CandidateSkillDAO(emf);

        // --- Skills ---
        Skill java = Skill.builder()
                .name("Java")
                .description("General purpose language")
                .category(SkillCategory.PROG_LANG)
                .build();
        Skill js = Skill.builder()
                .name("JavaScript")
                .description("Frontend & backend")
                .category(SkillCategory.PROG_LANG)
                .build();
        Skill pg = Skill.builder()
                .name("PostgreSQL")
                .description("Relational database")
                .category(SkillCategory.DB)
                .build();
        Skill docker = Skill.builder()
                .name("Docker")
                .description("Containerization")
                .category(SkillCategory.DEVOPS)
                .build();

        skillDAO.create(java);
        skillDAO.create(js);
        skillDAO.create(pg);
        skillDAO.create(docker);

        // --- Candidates ---
        Candidate anna = Candidate.builder()
                .name("Anna Larsen")
                .phone("11111111")
                .education("B.Sc. Computer Science")
                .build();
        Candidate bo = Candidate.builder()
                .name("Bo Jensen")
                .phone("22222222")
                .education("AP Datamatiker")
                .build();

        candidateDAO.create(anna);
        candidateDAO.create(bo);

        // --- Join rows ---
        candidateSkillDAO.create(CandidateSkill.builder().candidate(anna).skill(java).build());
        candidateSkillDAO.create(CandidateSkill.builder().candidate(anna).skill(pg).build());
        candidateSkillDAO.create(CandidateSkill.builder().candidate(bo).skill(js).build());
        candidateSkillDAO.create(CandidateSkill.builder().candidate(bo).skill(docker).build());
    }
}

