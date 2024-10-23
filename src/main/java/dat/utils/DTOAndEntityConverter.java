package dat.utils;

import dat.dtos.AuthorDTO;
import dat.dtos.BarsDTO;
import dat.entities.Author;
import dat.entities.Bars;
import dat.entities.Genre;

import java.util.Set;
import java.util.stream.Collectors;


public class DTOAndEntityConverter {

    public Author convertAuthorDTOToAuthorEntity(AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return null;
        }

        return Author.builder()
                .id(authorDTO.getId())
                .name(authorDTO.getName())
                .description(authorDTO.getDescription())
                .bars(convertDTOBarsSetToEntities(authorDTO.getBars()))
                .build();
    }

    public AuthorDTO convertAuthorEntityToAuthorDTO(Author author) {
        if (author == null) {
            return null;
        }

        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getDescription(),
                convertBarsSetFromAuthorEntityToBarsDTO(author.getBars())
        );
    }

    private Bars convertBarsDTOToBarsEntity(BarsDTO barsDTO) {
        if (barsDTO == null) {
            return null;
        }

        return Bars.builder()
                .id(barsDTO.getId())
                .title(barsDTO.getTitle())
                .content(barsDTO.getContent())
                .date(barsDTO.getDate())
                .genre(Genre.valueOf(barsDTO.getGenre()))
                .author(Author.builder().id(barsDTO.getAuthorId()).build())
                .build();
    }

    private BarsDTO convertBarsEntityToBarsDTO(Bars bars) {
        if (bars == null) {
            return null;
        }
        return new BarsDTO(
                bars.getId(),
                bars.getTitle(),
                bars.getContent(),
                bars.getDate(),
                bars.getGenre().name(),
                bars.getAuthor() != null ? bars.getAuthor().getId() : null
        );
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////
    //Hjælpe metoder til AuthorBarSets
    private Set<Bars> convertDTOBarsSetToEntities(Set<BarsDTO> barsDTOSet) {
        return barsDTOSet.stream()
                .map(this::convertBarsDTOToBarsEntity)
                .collect(Collectors.toSet());
    }

    private Set<BarsDTO> convertBarsSetFromAuthorEntityToBarsDTO(Set<Bars> barsSet) {
        return barsSet.stream()
                .map(this::convertBarsEntityToBarsDTO)
                .collect(Collectors.toSet());
    }
}
