package kz.crtr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@ApiModel("Сущность после аутентификации")
@Builder
@Data
public class UserTokenState {
    @ApiModelProperty(value = "ID пользователя")
    private Long empId;
    @ApiModelProperty(value = "логин пользователя")
    private String username;
    @ApiModelProperty(value = "токен")
    private String accessToken;
    @ApiModelProperty(value = "токен для обновления")
    private String refreshToken;
    @ApiModelProperty(value = "срок жизни")
    private Date expiresIn;
    @ApiModelProperty(value = "валидность")
    private Boolean active;
}
