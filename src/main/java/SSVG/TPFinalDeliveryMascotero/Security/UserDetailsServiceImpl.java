package SSVG.TPFinalDeliveryMascotero.Security;

import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.UsuarioEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl  implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("llefaaaaaaaa");
        UsuarioEntity userEntity = usuarioRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("El usuario: no existe"));
        String completeRole = "ROLE_".concat(userEntity.getRole().getRoleName().name());
       SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(completeRole);
        System.out.println("auto" + authorities);
        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                List.of(authorities)
        );
    }
}
