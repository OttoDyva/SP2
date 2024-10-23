package dat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="bars")
@Data
@Builder
@ToString
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
    private Author author;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;
}
