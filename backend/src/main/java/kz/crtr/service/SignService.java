package kz.crtr.service;

import kz.crtr.controller.token.LoginRequestDto;
import kz.crtr.controller.token.RefreshTokenRequestDto;
import kz.crtr.controller.token.TokenRequestDto;
import kz.crtr.dto.LocalValue;
import kz.crtr.dto.UserTokenState;

public interface SignService {
    UserTokenState sign(final LocalValue language, final LoginRequestDto loginRequest);

    UserTokenState refreshToken(final RefreshTokenRequestDto dto, final LocalValue language);

    UserTokenState validateToken(final TokenRequestDto dto);
}
