package kz.mtszn.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kz.mtszn.models.DBranch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@ApiModel("сущность юзера")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @ApiModelProperty(value = "ID пользователя")
    private Long empId;
    @ApiModelProperty(value = "логин пользователя")
    private String username;
    @ApiModelProperty(value = "бранч пользователя")
    private DBranch branch;
    @ApiModelProperty(value = "ФИО пользователя")
    private String fullName;
    @ApiModelProperty(value = "Дата рождения")
    private String birthDate;
    @ApiModelProperty(value = "Номер телефона")
    private String phone;
    @ApiModelProperty(value = "Почта")
    private String email;
    @ApiModelProperty(value = "доступные системы пользователя")
    private Set<String> availableSystems;
    @ApiModelProperty(value = "Данные из персон")
    private PersonDto personDto;
    @ApiModelProperty(value = "роль админа")
    private Boolean isAdmin;
}
