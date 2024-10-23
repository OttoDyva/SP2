package dat.dtos;

import dat.entities.Bars;
import dat.entities.Genre;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarsDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDate date;
    private Genre genre;
    private String authorName;
    private String authorDescription;

    public BarsDTO(Bars bars) {
        this.id = bars.getId();
        this.title = bars.getTitle();
        this.content = bars.getContent();
        this.date = bars.getDate();
        this.genre = bars.getGenre();
        this.authorName = bars.getAuthor() != null ? bars.getAuthor().getName() : null;
        this.authorDescription = bars.getAuthor() != null ? bars.getAuthor().getDescription() : null;
    }
}
