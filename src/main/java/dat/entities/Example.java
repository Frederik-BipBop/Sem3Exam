package dat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode

@Entity
@Table(name = "Example")
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany (mappedBy = "guides")
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<Example> examples = new HashSet<>();

    public boolean addExample(Example example) {
        if (examples.add(example)) example.addExample(this);
        return true;
    }

    public boolean removeExample(Example example){
        if (examples.remove(example)) example.removeExample(this);
        return true;
    }
}