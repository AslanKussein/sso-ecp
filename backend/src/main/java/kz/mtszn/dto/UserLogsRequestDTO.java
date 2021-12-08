package kz.mtszn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@ApiModel("Логируем действия юзеров")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogsRequestDTO {
    @ApiModelProperty(value = "ID пользователя")
    private Long empId;
    @ApiModelProperty(value = "логин пользователя")
    private String username;
    @ApiModelProperty(value = "тело сообщения")
    private String eventMessage;
    @ApiModelProperty(value = "время действия")
    private ZonedDateTime eventDate;
}
