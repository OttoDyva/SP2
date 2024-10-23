package dat.entities;

import dat.dtos.BarsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="bars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bars_id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude  // Exclude from toString to avoid circular reference
    @EqualsAndHashCode.Exclude  // Exclude from equals and hashCode to avoid circular reference
    private Author author;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    public Bars(BarsDTO barsDTO) {
        this.id = barsDTO.getId();
        this.title = barsDTO.getTitle();
        this.content = barsDTO.getContent();
        this.date = barsDTO.getDate();
        this.genre = Genre.valueOf(String.valueOf(barsDTO.getGenre()));
    }
}
