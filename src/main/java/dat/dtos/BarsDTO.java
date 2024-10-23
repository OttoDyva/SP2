package dat.dtos;

import dat.entities.Bars;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarsDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDate date;
    private String genre;
    private Integer authorId;

    public BarsDTO(Bars bars) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.genre = genre;
        this.authorId = authorId;
    }
}
