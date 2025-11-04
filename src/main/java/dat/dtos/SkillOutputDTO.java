package dat.dtos;

import dat.entities.Skill;
import dat.enums.SkillCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillOutputDTO {
    private Long id;
    private String name;
    private String description;
    private SkillCategory category;

    public SkillOutputDTO(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.description = skill.getDescription();
        this.category = skill.getCategory();
    }
}
