package dat.dtos;

import dat.entities.Candidate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateOutputDTO {
    private Long id;
    private String name;
    private String phone;
    private String education;
    private Set<Long> skillIds;

    public CandidateOutputDTO(Candidate candidate) {
        this.id = candidate.getId();
        this.name = candidate.getName();
        this.phone = candidate.getPhone();
        this.education = candidate.getEducation();
        if (candidate.getCandidateSkills() != null) {
            this.skillIds = candidate.getCandidateSkills().stream()
                    .map(cs -> cs.getSkill().getId())
                    .collect(Collectors.toSet());
        }
    }
}
