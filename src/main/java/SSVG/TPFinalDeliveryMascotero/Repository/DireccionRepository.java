package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<DireccionEntity, Long> {

    // Misma comparacion que "hasDireccion" de ClienteService: calle sin espacios ni mayusculas, numero, piso y depto.
    // Las observaciones no cuentan, son un dato de la direccion y no la identifican
    @Query("""
            SELECT d FROM DireccionEntity d
            WHERE LOWER(TRIM(d.calle)) = LOWER(TRIM(:calle))
              AND d.numero = :numero
              AND ((:piso IS NULL AND d.piso IS NULL) OR d.piso = :piso)
              AND LOWER(TRIM(COALESCE(d.departamento, ''))) = LOWER(TRIM(COALESCE(:departamento, '')))
            """)
    List<DireccionEntity> findIguales(String calle, Integer numero, Integer piso, String departamento);
}
