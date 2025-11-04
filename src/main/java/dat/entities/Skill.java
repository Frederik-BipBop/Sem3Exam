package dat.entities;

import dat.enums.SkillCategory;
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
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    @OneToMany(mappedBy = "skill",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<CandidateSkill> candidateSkills = new HashSet<>();

    // helpers
    public boolean addCandidateSkill(CandidateSkill cs) {
        if (candidateSkills.add(cs)) cs.setSkill(this);
        return true;
    }

    public boolean removeCandidateSkill(CandidateSkill cs) {
        if (candidateSkills.remove(cs)) cs.setSkill(null);
        return true;
    }
}
