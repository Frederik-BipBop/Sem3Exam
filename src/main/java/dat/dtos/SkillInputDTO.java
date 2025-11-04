package dat.dtos;

import dat.enums.SkillCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillInputDTO {
    private String name;
    private String description;
    private SkillCategory category;
}
