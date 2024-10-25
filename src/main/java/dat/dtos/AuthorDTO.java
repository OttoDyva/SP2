package dat.dtos;

import dat.entities.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private Integer id;
    private String name;
    private String description;
    private Set<BarsDTO> bars;

    public AuthorDTO(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.description = author.getDescription();
        this.bars = author.getBars().stream()
                .map(BarsDTO::new)
                .collect(Collectors.toSet());
    }
}
