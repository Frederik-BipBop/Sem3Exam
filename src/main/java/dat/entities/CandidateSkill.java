package dat.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "candidate_skills")
public class CandidateSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Skill skill;


}
