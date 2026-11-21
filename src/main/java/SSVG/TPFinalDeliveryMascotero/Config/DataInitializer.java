package SSVG.TPFinalDeliveryMascotero.Config;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.RoleType;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.RoleEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Usuarios.UsuarioEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.RoleRepository;
import SSVG.TPFinalDeliveryMascotero.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    // Usuario superadmin que se crea al arrancar si no existe
    public static final String SUPERADMIN_EMAIL = "super@admin.com";
    private static final String SUPERADMIN_PASSWORD = "super";

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        permitirRolesNuevos();

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

    // Hibernate crea la columna del rol como ENUM('USER','ADMIN') y al actualizar el esquema no le agrega
    // valores nuevos, asi que SUPERADMIN no entraria. Se pasa la columna a VARCHAR una sola vez.
    private void permitirRolesNuevos() {
        List<String> tipo = jdbcTemplate.queryForList(
                "SELECT DATA_TYPE FROM information_schema.COLUMNS "
                        + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'roles' AND COLUMN_NAME = 'role_name'",
                String.class);
        if (!tipo.isEmpty() && "enum".equalsIgnoreCase(tipo.getFirst())) {
            jdbcTemplate.execute("ALTER TABLE roles MODIFY role_name VARCHAR(20) NOT NULL");
            System.out.println("Columna roles.role_name pasada a VARCHAR para admitir SUPERADMIN");
        }
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
