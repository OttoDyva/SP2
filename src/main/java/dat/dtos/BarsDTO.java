package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarsDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDate date;
    private String genre;
    private Long authorId;
}
