package kz.crtr.service.impl;

import kz.crtr.controller.token.LoginRequestDto;
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

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class SignServiceImpl implements SignService {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenUtil;
    private final UsersRepository usersRepository;

    @Override
    public UserTokenState sign(final LocalValue language, final LoginRequestDto loginRequest) {
        Authentication authentication;

        validate(loginRequest, language);

        final UserDetails userDetails = loadUserByUsername(language, loginRequest.getUsername());

        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), userDetails.getAuthorities())
        );

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

        return usersRepository.findByUserDetail_Iin(edsInfo.getIin());
    }

    private void validate(final LoginRequestDto loginRequest, final LocalValue language) {
        if (StringUtils.isEmpty(loginRequest.getCertificate())) {
            throw BadRequestException.chooseEsp(language);
        }

        if (StringUtils.isEmpty(loginRequest.getUsername()) && StringUtils.isEmpty(loginRequest.getPassword())) {
            throw BadRequestException.emptyLoginOrPassword(language);
        }

        final Users users = getUserByEds(loginRequest.getCertificate(), language);

        if (!users.getUsername().equalsIgnoreCase(loginRequest.getUsername())) {
            throw BadRequestException.userNotEqualsEcpUser(language);
        }
    }
}
