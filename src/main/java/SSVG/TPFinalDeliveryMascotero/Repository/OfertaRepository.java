package SSVG.TPFinalDeliveryMascotero.Repository;

import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OfertaRepository extends JpaRepository<OfertaEntity, Long> {

    @Query("SELECT o FROM OfertaEntity o WHERE o.fechaFin < CURRENT_DATE")
    List<OfertaEntity> findExpiredOffers();
}
