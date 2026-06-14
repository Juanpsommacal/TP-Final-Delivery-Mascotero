package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Login.LoginRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Usuario.UsuarioCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.LoginResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.UsuarioResponseDTO;

import SSVG.TPFinalDeliveryMascotero.Security.Jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;


    public UsuarioResponseDTO register(UsuarioCreateRequestDTO dto){
        return usuarioService.createUsuario(dto);
    }

    public LoginResponseDTO login(LoginRequestDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
     return new LoginResponseDTO(token);
    }
}
