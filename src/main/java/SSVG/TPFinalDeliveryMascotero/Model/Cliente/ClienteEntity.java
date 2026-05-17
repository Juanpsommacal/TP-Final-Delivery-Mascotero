package SSVG.TPFinalDeliveryMascotero.Model.Cliente;

import jakarta.persistence.*;

@Entity
@Table(name = "Clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String telefono;
}
