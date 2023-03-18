package com.abnamro.recipe.securityConfig;

import com.abnamro.recipe.model.persistence.AuthorityDao;
import com.abnamro.recipe.model.persistence.UserDao;
import com.abnamro.recipe.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RecipeServiceUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        List<UserDao> userDao = loginRepository.findByEmail(username);
        if (userDao.size() > 0) {
            String hashPwd = passwordEncoder.encode(userDao.get(0).getPwd());
            if (passwordEncoder.matches(pwd, userDao.get(0).getPwd())) {
                return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthorities(userDao.get(0).getAuthorities()));
            } else {
                throw new BadCredentialsException("Invalid password!");
            }
        }else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<AuthorityDao> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    for (AuthorityDao authorityDao : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authorityDao.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
