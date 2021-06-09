package kz.crtr.service.impl;

import kz.crtr.controller.token.LoginRequestDto;
import kz.crtr.controller.token.RefreshTokenRequestDto;
import kz.crtr.controller.token.TokenRequestDto;
import kz.crtr.dto.EDSInfoGson;
import kz.crtr.dto.LocalValue;
import kz.crtr.dto.UserTokenState;
import kz.crtr.exception.BadRequestException;
import kz.crtr.exception.NotFoundException;
import kz.crtr.models.Users;
import kz.crtr.models.repository.UsersRepository;
import kz.crtr.security.CurrentUser;
import kz.crtr.security.UserDetailsServiceImpl;
import kz.crtr.service.SignService;
import kz.crtr.util.EDSUtil;
import kz.crtr.util.JwtTokenUtil;
import kz.crtr.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class SignServiceImpl implements SignService {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenUtil;
    private final UsersRepository usersRepository;

    @Override
    public UserTokenState sign(final LocalValue language, final LoginRequestDto loginRequest) {
        Authentication authentication = null;
        if (StringUtils.isNotEmpty(loginRequest.getCertificate())) {
            final Users users = getUserByEds(loginRequest.getCertificate(), language);
            if (isNull(users)) {
                throw BadRequestException.notCorrectUserException(language);
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(users.getUsername());

            if (nonNull(userDetails)) {
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }
        } else {
            final UserDetails userDetails = loadUserByUsername(language, loginRequest.getUsername());

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), userDetails.getAuthorities())
            );
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

        return tokenUtil.generateToken(currentUser.getUser().getEmpId().toString(), currentUser.getUser().getUsername());
    }

    private UserDetails loadUserByUsername(final LocalValue localValue, final String username) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (isNull(userDetails)) {
            throw NotFoundException.userNotFound(localValue, username);
        }
        return userDetails;
    }

    private Users getUserByEds(final String certificate, final LocalValue language) {
        EDSUtil edsUtil = new EDSUtil();
        EDSInfoGson edsInfo = edsUtil.getInfo(certificate);

        if (isNull(edsInfo)) {
            throw BadRequestException.getEcpError(language);
        }

        if (isNull(edsInfo.getIin())) {
            throw BadRequestException.getEcpErrorIin(language);
        }

        if (!edsInfo.getValid()) {
            throw BadRequestException.getEcpErrorValid(language);
        }

        List<Users> list = usersRepository.findByUserDetail_IinAndBlockNot(edsInfo.getIin(), 1);
        if (list.size() > 1) {
            throw BadRequestException.notCorrectUserException(language);
        }
        return usersRepository.findByUserDetail_IinAndBlockNot(edsInfo.getIin(), 1).get(0);
    }

    @Override
    public UserTokenState refreshToken(final RefreshTokenRequestDto dto, final LocalValue language) {
        if (StringUtils.isEmpty(dto.getRefreshToken())) {
            throw BadRequestException.emptyRefreshToken(language);
        }
        return tokenUtil.generateToken(tokenUtil.getUserIdFromJWT(dto.getRefreshToken()), tokenUtil.getUserName(dto.getRefreshToken()));
    }

    @Override
    public UserTokenState validateToken(final TokenRequestDto dto) {
        return tokenUtil.getUserTokenState(dto.getToken());
    }
}
