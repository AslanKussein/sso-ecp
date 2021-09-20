package kz.mtszn.service;

import kz.mtszn.controller.token.LoginRequestDto;
import kz.mtszn.controller.token.RefreshTokenRequestDto;
import kz.mtszn.controller.token.TokenRequestDto;
import kz.mtszn.dto.LocalValue;
import kz.mtszn.dto.UserTokenState;

public interface SignService {
    UserTokenState sign(final LocalValue language, final LoginRequestDto loginRequest);

    UserTokenState refreshToken(final RefreshTokenRequestDto dto, final LocalValue language);

    UserTokenState validateToken(final TokenRequestDto dto);
}
