package kz.mtszn.security;

import kz.mtszn.models.Users;
import kz.mtszn.models.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author HP
 */
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Users user = userRepository.findByUserNameIgnoreCaseAndBlockNotContaining(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//
//        for (UserRole uRole : user.getUserRoleList()) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(uRole.getSRole().getCode()));
//        }
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return buildUserForAuthentication(user, grantedAuthorities);
    }

    private User buildUserForAuthentication(Users user, Set<GrantedAuthority> authorities) {
        CurrentUser currentUser = new CurrentUser(user.getUsername(), user.getPassword(), authorities);
        currentUser.setUser(user);
        return currentUser;
    }
}
