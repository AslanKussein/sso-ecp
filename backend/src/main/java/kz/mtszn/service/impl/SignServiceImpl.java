package kz.mtszn.service.impl;

import io.jsonwebtoken.Claims;
import kz.mtszn.controller.token.LoginRequestDto;
import kz.mtszn.controller.token.RefreshTokenRequestDto;
import kz.mtszn.controller.token.TokenRequestDto;
import kz.mtszn.dto.EDSInfoGson;
import kz.mtszn.dto.LocalValue;
import kz.mtszn.dto.UserTokenState;
import kz.mtszn.exception.BadRequestException;
import kz.mtszn.exception.NotFoundException;
import kz.mtszn.models.BlockUser;
import kz.mtszn.models.Users;
import kz.mtszn.models.repository.BlockUserRepository;
import kz.mtszn.models.repository.UsersRepository;
import kz.mtszn.security.CurrentUser;
import kz.mtszn.security.UserDetailsServiceImpl;
import kz.mtszn.service.SignService;
import kz.mtszn.util.EDSUtil;
import kz.mtszn.util.JwtTokenUtil;
import kz.mtszn.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
        final Users users;
        if (StringUtils.isNotEmpty(loginRequest.getCertificate())) {
            users = getUserByEds(loginRequest.getCertificate(), language);

            if (isNull(users)) {
                throw BadRequestException.notCorrectUserException(language);
            }
            UserDetails userDetails = getUserDetails(language, users.getUsername());

            if (nonNull(userDetails)) {
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }
        } else {
            final UserDetails userDetail = getUserDetails(language, loginRequest.getUsername());

            users = usersRepository.findByUsernameIgnoreCase(userDetail.getUsername());

            checkFailureCounterBlockUser(users.getEmpId(), language);

            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), userDetail.getAuthorities())
                );
            } catch (AuthenticationException e) {
                checkAndBlockUser(users.getEmpId(), language);
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

        return tokenUtil.generateToken(currentUser.getUser().getEmpId().toString(), currentUser.getUser().getUsername());
    }

    private void checkFailureCounterBlockUser(final Long empId, final LocalValue language) {
        BlockUser blockUser = blockUserRepository.findByEmpIdAndFailurecounter(empId, 5L);
        if (nonNull(blockUser)) {
            throw BadRequestException.userBlock(language);
        }
    }

    private void checkAndBlockUser(final Long empId, final LocalValue language) {
        BlockUser blockUser = blockUserRepository.findByEmpId(empId);
        if (nonNull(blockUser)) {
            blockUser.setFailurecounter(blockUser.getFailurecounter() + 1);

        } else {
            blockUser = new BlockUser();
            blockUser.setFailurecounter(1L);
            blockUser.setEmpId(empId);
        }
        blockUser.setBlockDate(Instant.now());
        blockUserRepository.save(blockUser);
        throw BadRequestException.notCorrectUserException(language);
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
