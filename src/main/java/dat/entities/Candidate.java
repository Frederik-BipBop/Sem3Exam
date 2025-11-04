package dat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 20)
    private String name;

    @Setter
    @Column(nullable = false, length = 11)
    private String phone;

    @Setter
    @Column(nullable = false, length = 50)
    private String education;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<CandidateSkill> candidateSkills = new HashSet<>();

    // helpers
    public boolean addSkill(CandidateSkill cs) {
        if (candidateSkills.add(cs)) cs.setCandidate(this);
        return true;
    }

    public boolean removeSkill(CandidateSkill cs) {
        if (candidateSkills.remove(cs)) cs.setCandidate(null);
        return true;
    }
}
