package kz.mtszn.controller.token;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequestDto {
    @Valid
    @NotNull
    private String username;
    @Valid
    @NotNull
    private String password;
    @Valid
    @NotNull
    private String certificate;
}
