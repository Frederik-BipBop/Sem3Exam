package dat.controllers;

import dat.daos.SkillDAO;
import dat.dtos.SkillInputDTO;
import dat.dtos.SkillOutputDTO;
import dat.entities.Skill;
import dat.enums.SkillCategory;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class SkillController implements IController<SkillInputDTO, Long> {

    private final SkillDAO skillDAO;

    public SkillController(EntityManagerFactory emf) {
        this.skillDAO = new SkillDAO(emf);
    }

    @Override
    public void read(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        if (!validatePrimaryKey(id)) throw new ApiException(400, "Invalid id");

        Skill s = skillDAO.read(id).orElse(null);
        if (s == null) throw new ApiException(404, "Skill " + id + " not found");

        ctx.status(200).json(new SkillOutputDTO(s), SkillOutputDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        List<Skill> list = skillDAO.readAll();
        List<SkillOutputDTO> out = new ArrayList<>();
        for (Skill s : list) out.add(new SkillOutputDTO(s));
        ctx.status(200).json(out, SkillOutputDTO.class);
    }

    @Override
    public void create(Context ctx) {
        SkillInputDTO input = validateEntity(
                ctx.bodyValidator(SkillInputDTO.class)
                        .check(d -> d.getName() != null && !d.getName().isBlank(), "name is required")
                        .check(d -> d.getCategory() != null, "category is required")
                        .get()
        );

        Skill saved = skillDAO.create(Skill.builder()
                .name(input.getName())
                .description(input.getDescription())
                .category(input.getCategory())
                .build());

        ctx.status(201).json(new SkillOutputDTO(saved), SkillOutputDTO.class);
    }

    @Override
    public void update(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        if (!validatePrimaryKey(id)) throw new ApiException(400, "Invalid id");

        SkillInputDTO input = validateEntity(ctx.bodyValidator(SkillInputDTO.class).get());

        Skill existing = skillDAO.read(id).orElse(null);
        if (existing == null) throw new ApiException(404, "Skill " + id + " not found");

        if (input.getName() != null)        existing.setName(input.getName());
        if (input.getDescription() != null) existing.setDescription(input.getDescription());
        if (input.getCategory() != null)    existing.setCategory(input.getCategory()); // enum from DTO

        Skill saved = skillDAO.update(id, existing);
        ctx.status(200).json(new SkillOutputDTO(saved), SkillOutputDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        if (!validatePrimaryKey(id)) throw new ApiException(400, "Invalid id");
        skillDAO.delete(id);
        ctx.status(204);
    }

    @Override
    public boolean validatePrimaryKey(Long id) {
        return id != null && id > 0;
    }

    @Override
    public SkillInputDTO validateEntity(SkillInputDTO dto) {
        return dto;
    }
}
