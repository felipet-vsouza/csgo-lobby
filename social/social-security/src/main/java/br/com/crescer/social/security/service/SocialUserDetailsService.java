package br.com.crescer.social.security.service;

import br.com.crescer.social.security.enumeration.SocialRoles;
import br.com.crescer.social.service.UsuarioService;
import br.com.crescer.social.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Carlos H. Nonnemacher
 */
@Service
public class SocialUserDetailsService implements UserDetailsService {

    @Autowired
    UsuarioService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = service.findByEmail(email);
        if (email.isEmpty() || usuario == null) {
            throw new UsernameNotFoundException(String.format("User with username=%s was not found", email));
        }
        return new User(usuario.getEmail(), usuario.getPassword(), SocialRoles.valuesToList());
    }

}
