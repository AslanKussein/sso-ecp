package kz.crtr.security;

import kz.crtr.models.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public Users getUser() {
        if (!getAuthentication().getPrincipal().equals("anonymousUser")) {
            CurrentUser currentUser = (CurrentUser) getAuthentication().getPrincipal();
            return currentUser.getUser();
        } else {
            return null;
        }
    }

    @Override
    public Boolean hasRole(String role) {
        Users user = getUser();
//        if (nonNull(user)
//                && nonNull(user.getUserDetail())
//                && nonNull(user.getUserDetail().getOrganization())
//                && nonNull(user.getUserDetail().getOrganization().getOrganizationRoleList())) {
//            return user.getUserDetail().getOrganization().getOrganizationRoleList().get(0).getSRoleId().getCode().equalsIgnoreCase(role);
//        }
        return false;
    }
}
