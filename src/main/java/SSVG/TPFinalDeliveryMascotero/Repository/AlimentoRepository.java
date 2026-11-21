package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EtapaVida;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.TipoAnimal;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentoRepository extends JpaRepository<AlimentoEntity, Long> {

    public List<AlimentoEntity> findByActivoTrue();

    // Candidatos a duplicado: mismo animal y etapa de vida. Marca, nombre y peso se comparan en AlimentoService.
    public List<AlimentoEntity> findByActivoTrueAndTipoAnimalAndEtapaVida(TipoAnimal tipoAnimal, EtapaVida etapaVida);
}
