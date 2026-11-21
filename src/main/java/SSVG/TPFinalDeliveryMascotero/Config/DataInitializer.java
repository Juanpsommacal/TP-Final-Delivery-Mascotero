package SSVG.TPFinalDeliveryMascotero.Config;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.RoleType;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.RoleEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.UsuarioEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.RoleRepository;
import SSVG.TPFinalDeliveryMascotero.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    // Usuario superadmin que se crea al arrancar si no existe
    public static final String SUPERADMIN_EMAIL = "super@admin.com";
    private static final String SUPERADMIN_PASSWORD = "super";

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        for (RoleType nombreRol : RoleType.values()) {
            if (!roleRepository.existsByRoleName(nombreRol)) {
                RoleEntity nuevoRol = new RoleEntity();
                nuevoRol.setRoleName(nombreRol);
                roleRepository.save(nuevoRol);
                System.out.println("Rol creado automáticamente: " + nombreRol);
            }
        }

        crearSuperadmin();
    }

    private void crearSuperadmin() {
        if (usuarioRepository.existsByEmail(SUPERADMIN_EMAIL)) {
            return;
        }
        UsuarioEntity superadmin = new UsuarioEntity();
        superadmin.setNombre("Super");
        superadmin.setApellido("Admin");
        superadmin.setEmail(SUPERADMIN_EMAIL);
        superadmin.setPassword(passwordEncoder.encode(SUPERADMIN_PASSWORD));
        superadmin.setActive(true);
        superadmin.setRole(roleRepository.findByRoleName(RoleType.SUPERADMIN).orElseThrow());
        usuarioRepository.save(superadmin);
        System.out.println("Usuario superadmin creado: " + SUPERADMIN_EMAIL);
    }
}
