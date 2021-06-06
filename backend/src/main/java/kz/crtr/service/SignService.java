package kz.crtr.service;

import kz.crtr.controller.token.LoginRequestDto;
import kz.crtr.dto.LocalValue;
import kz.crtr.dto.UserTokenState;

public interface SignService {
    UserTokenState sign(final LocalValue language, final LoginRequestDto loginRequest);
}
