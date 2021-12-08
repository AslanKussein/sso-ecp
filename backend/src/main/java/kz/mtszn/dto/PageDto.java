package kz.mtszn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto<X> implements Serializable {
    private X content;
    private Integer totalElements;
    private Integer totalPages;
    private String query;

    public PageDto(X content, Integer totalElements, Integer totalPages) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
