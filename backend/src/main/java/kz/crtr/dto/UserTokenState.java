package kz.crtr.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class UserTokenState {
    private Long empId;
    private String username;
    private String accessToken;
    private String refreshToken;
    private Date expiresIn;
}
