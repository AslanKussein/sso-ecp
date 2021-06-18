package kz.crtr.service.impl;

import io.jsonwebtoken.Claims;
import kz.crtr.controller.token.LoginRequestDto;
import kz.crtr.controller.token.RefreshTokenRequestDto;
import kz.crtr.controller.token.TokenRequestDto;
import kz.crtr.dto.EDSInfoGson;
import kz.crtr.dto.LocalValue;
import kz.crtr.dto.UserTokenState;
import kz.crtr.exception.BadRequestException;
import kz.crtr.exception.NotFoundException;
import kz.crtr.models.Users;
import kz.crtr.models.repository.BlockUserRepository;
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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class SignServiceImpl implements SignService {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenUtil;
    private final UsersRepository usersRepository;
    private final BlockUserRepository blockUserRepository;

    @Override
    public UserTokenState sign(final LocalValue language, final LoginRequestDto loginRequest) {
        Authentication authentication = null;
        if (StringUtils.isNotEmpty(loginRequest.getCertificate())) {
            final Users users = getUserByEds(loginRequest.getCertificate(), language);
            if (isNull(users)) {
                throw BadRequestException.notCorrectUserException(language);
            }
            UserDetails userDetails = getUserDetails(language, users.getUsername());

            if (nonNull(userDetails)) {
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }
        } else {
            final UserDetails userDetail = getUserDetails(language, loginRequest.getUsername());

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), userDetail.getAuthorities())
            );

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

        return tokenUtil.generateToken(currentUser.getUser().getEmpId().toString(), currentUser.getUser().getUsername());
    }

    private UserDetails getUserDetails(final LocalValue language, final String username) {
        try {
            return loadUserByUsername(language, username);
        } catch (Exception e) {
            throw BadRequestException.notCorrectUserException(language);
        }
    }

    private UserDetails loadUserByUsername(final LocalValue localValue, final String username) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (isNull(userDetails)) {
            throw NotFoundException.userNotFound(localValue, username);
        }
        return userDetails;
    }

    private Users getUserByEds(final String certificate, final LocalValue language) {
        try {
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

            return usersRepository.findByUserDetail_IinAndBlockNot(edsInfo.getIin());
        } catch (Exception e) {
            throw BadRequestException.notCorrectUserException(language);
        }
    }

    @Override
    public UserTokenState refreshToken(final RefreshTokenRequestDto dto, final LocalValue language) {
        if (StringUtils.isEmpty(dto.getRefreshToken())) {
            throw BadRequestException.emptyRefreshToken(language);
        }

        Claims claims = tokenUtil.getAllClaimsFromToken(dto.getRefreshToken());

        return tokenUtil.generateToken(claims.getId(), claims.getSubject());
    }

    @Override
    public UserTokenState validateToken(final TokenRequestDto dto) {
        return tokenUtil.getUserTokenState(dto.getToken());
    }
}
