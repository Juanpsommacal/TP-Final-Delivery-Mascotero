package SSVG.TPFinalDeliveryMascotero.Model.Usuarios;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private RoleType roleName;
}
