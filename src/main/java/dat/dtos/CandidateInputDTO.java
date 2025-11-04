package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateInputDTO {
    private String name;
    private String phone;
    private String education;
    private Set<Long> skillIds; // id’er på skills kandidaten skal have
}
