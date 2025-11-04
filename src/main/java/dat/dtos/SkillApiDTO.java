package dat.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillApiDTO {
    private Long id;
    private String name;
    private String slug;
    private String category;

    private Integer popularityScore; // fra ekstern API
    private Integer averageSalary;   // fra ekstern API
}
