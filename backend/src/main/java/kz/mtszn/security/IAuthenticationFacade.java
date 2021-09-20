package kz.mtszn.security;

import kz.mtszn.models.Users;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    Users getUser();

    Boolean hasRole(String role);
}
