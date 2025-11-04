package dat.dtos;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateApiDTO {
    private Long id;
    private String name;
    private String phone;
    private String education;

    // Liste af kandidatens skills med market data
    private List<SkillApiDTO> skills;
}