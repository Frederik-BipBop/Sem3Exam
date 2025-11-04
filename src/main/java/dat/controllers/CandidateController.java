package dat.controllers;

import dat.daos.CandidateDAO;
import dat.dtos.CandidateInputDTO;
import dat.dtos.CandidateOutputDTO;
import dat.entities.Candidate;
import dat.entities.CandidateSkill;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CandidateController implements IController<CandidateInputDTO, Long> {

    private final CandidateDAO candidateDAO;

    public CandidateController(EntityManagerFactory emf) {
        this.candidateDAO = new CandidateDAO(emf);
    }

    @Override
    public void read(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        if (!validatePrimaryKey(id)) throw new ApiException(400, "Invalid id");

        Candidate c = candidateDAO.read(id).orElse(null);
        if (c == null) throw new ApiException(404, "Candidate " + id + " not found");

        ctx.status(200).json(toOutput(c), CandidateOutputDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        List<Candidate> list = candidateDAO.readAll();
        List<CandidateOutputDTO> out = new ArrayList<>();
        for (Candidate c : list) out.add(toOutput(c));
        ctx.status(200).json(out, CandidateOutputDTO.class);
    }

    @Override
    public void create(Context ctx) {
        CandidateInputDTO input = validateEntity(
                ctx.bodyValidator(CandidateInputDTO.class)
                        .check(d -> d.getName() != null && !d.getName().isBlank(), "name is required")
                        .check(d -> d.getPhone() != null && !d.getPhone().isBlank(), "phone is required")
                        .get()
        );

        Candidate saved = candidateDAO.create(Candidate.builder()
                .name(input.getName())
                .phone(input.getPhone())
                .education(input.getEducation())
                .build());

        if (input.getSkillIds() != null) {
            for (Long sid : input.getSkillIds()) {
                candidateDAO.addSkillToCandidate(saved.getId(), sid);
            }
            Candidate reloaded = candidateDAO.read(saved.getId()).orElse(null);
            if (reloaded != null) saved = reloaded;
        }

        ctx.status(201).json(toOutput(saved), CandidateOutputDTO.class);
    }

    @Override
    public void update(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        if (!validatePrimaryKey(id)) throw new ApiException(400, "Invalid id");

        CandidateInputDTO input = validateEntity(ctx.bodyValidator(CandidateInputDTO.class).get());

        Candidate existing = candidateDAO.read(id).orElse(null);
        if (existing == null) throw new ApiException(404, "Candidate " + id + " not found");

        if (input.getName() != null)       existing.setName(input.getName());
        if (input.getPhone() != null)      existing.setPhone(input.getPhone());
        if (input.getEducation() != null)  existing.setEducation(input.getEducation());

        Candidate saved = candidateDAO.update(id, existing);

        if (input.getSkillIds() != null) {
            // fjern alle nuværende links
            Set<Long> current = toOutput(saved).getSkillIds();
            for (Long sid : current) candidateDAO.removeSkillFromCandidate(id, sid);
            // tilføj nye
            for (Long sid : input.getSkillIds()) candidateDAO.addSkillToCandidate(id, sid);

            Candidate reloaded = candidateDAO.read(id).orElse(null);
            if (reloaded != null) saved = reloaded;
        }

        ctx.status(200).json(toOutput(saved), CandidateOutputDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        if (!validatePrimaryKey(id)) throw new ApiException(400, "Invalid id");
        candidateDAO.delete(id);
        ctx.status(204);
    }

    // Ekstra: link/unlink endpoints
    public void addSkill(Context ctx) {
        Long candidateId = ctx.pathParamAsClass("candidateId", Long.class).get();
        Long skillId     = ctx.pathParamAsClass("skillId", Long.class).get();
        if (!validatePrimaryKey(candidateId) || !validatePrimaryKey(skillId))
            throw new ApiException(400, "Invalid id");

        Candidate c = candidateDAO.read(candidateId).orElse(null);
        if (c == null) throw new ApiException(404, "Candidate " + candidateId + " not found");

        candidateDAO.addSkillToCandidate(candidateId, skillId);
        Candidate updated = candidateDAO.read(candidateId).orElse(c);
        ctx.status(200).json(toOutput(updated), CandidateOutputDTO.class);
    }

    public void removeSkill(Context ctx) {
        Long candidateId = ctx.pathParamAsClass("candidateId", Long.class).get();
        Long skillId     = ctx.pathParamAsClass("skillId", Long.class).get();
        if (!validatePrimaryKey(candidateId) || !validatePrimaryKey(skillId))
            throw new ApiException(400, "Invalid id");

        Candidate c = candidateDAO.read(candidateId).orElse(null);
        if (c == null) throw new ApiException(404, "Candidate " + candidateId + " not found");

        candidateDAO.removeSkillFromCandidate(candidateId, skillId);
        Candidate updated = candidateDAO.read(candidateId).orElse(c);
        ctx.status(200).json(toOutput(updated), CandidateOutputDTO.class);
    }

    @Override
    public boolean validatePrimaryKey(Long id) {
        return id != null && id > 0;
    }

    @Override
    public CandidateInputDTO validateEntity(CandidateInputDTO dto) {
        return dto;
    }

    private CandidateOutputDTO toOutput(Candidate c) {
        Set<Long> skillIds = new HashSet<>();
        for (CandidateSkill cs : c.getCandidateSkills()) {
            if (cs.getSkill() != null && cs.getSkill().getId() != null) {
                skillIds.add(cs.getSkill().getId());
            }
        }
        return CandidateOutputDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .phone(c.getPhone())
                .education(c.getEducation())
                .skillIds(skillIds)
                .build();
    }
}
