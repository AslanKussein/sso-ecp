package kz.mtszn.controller.token;

import lombok.Data;

@Data
public class RefreshTokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
