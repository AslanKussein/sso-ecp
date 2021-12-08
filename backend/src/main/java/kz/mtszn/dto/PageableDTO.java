package kz.mtszn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "модель пагинации")
public class PageableDTO {

    @PositiveOrZero
    @ApiModelProperty(notes = "Начальная страница", required = true, example = "0")
    protected int pageNumber;
    @Positive
    @ApiModelProperty(notes = "Количество страниц", required = true, example = "10")
    protected int pageSize;
    @ApiModelProperty(notes = "Количество страница", required = true, example = "id")
    protected String sortBy;
    @ApiModelProperty(notes = "Направление сортировка")
    protected Sort.Direction direction = Sort.Direction.DESC;
}
