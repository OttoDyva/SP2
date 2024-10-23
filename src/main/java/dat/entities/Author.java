package dat.entities;

import dat.dtos.AuthorDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="author")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude  // Exclude from toString to avoid circular reference
    @EqualsAndHashCode.Exclude  // Exclude from equals and hashCode to avoid circular reference
    private Set<Bars> bars = new HashSet<>();


    public Author(AuthorDTO authorDTO) {
        this.id = authorDTO.getId();
        this.name = authorDTO.getName();
        this.description = authorDTO.getDescription();
        this.bars = authorDTO.getBars().stream()
                .map(Bars::new) // Mapping Bars to BarsDTO
                .collect(Collectors.toSet());
    }
}
