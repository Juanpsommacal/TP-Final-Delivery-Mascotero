package SSVG.TPFinalDeliveryMascotero.Security;

import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.RoleType;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.UsuarioEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl  implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity userEntity = usuarioRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("El usuario: no existe"));
        RoleType rol = userEntity.getRole().getRoleName();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.name()));
        // El superadmin tambien es admin: asi pasa todos los hasRole('ADMIN') del back y los chequeos del front
        if (rol == RoleType.SUPERADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + RoleType.ADMIN.name()));
        }
        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isActive(),
                true,
                true,
                true,
                authorities
        );
    }
}
