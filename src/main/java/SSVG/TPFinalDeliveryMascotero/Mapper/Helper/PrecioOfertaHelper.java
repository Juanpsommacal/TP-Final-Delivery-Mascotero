package SSVG.TPFinalDeliveryMascotero.Mapper.Helper;

import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/// Calcula el precio con descuento de un producto. Devuelve null si no tiene oferta vigente.
public class PrecioOfertaHelper {

    public static BigDecimal calcularPrecioOferta(ProductoEntity entity) {
        OfertaEntity oferta = entity.getOferta();
        if (oferta == null) {
            return null;
        }

        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(oferta.getFechaInicio()) || hoy.isAfter(oferta.getFechaFin())) {
            return null;
        }

        BigDecimal descuento = entity.getPrecio()
                .multiply(BigDecimal.valueOf(oferta.getPorcentaje()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return entity.getPrecio().subtract(descuento);
    }
}
