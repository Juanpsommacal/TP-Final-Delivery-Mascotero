package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ProductoConsultas {

    public static final Map<String, ExpresionColumna<ProductoEntity>> COLUMNAS = Map.of(
            "nombre", (root, query, cb) -> nombreCompleto(root, cb),
            "precio", ProductoConsultas::precioVigente,
            "stock", (root, query, cb) -> root.get("stock")
    );

    private ProductoConsultas() {}

    public static Specification<ProductoEntity> filtrar(String busqueda, String tipo, Integer stockMaximo) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();
            filtros.add(cb.isTrue(root.get("activo")));

            if ("ALIMENTO".equals(tipo)) filtros.add(cb.equal(root.type(), AlimentoEntity.class));
            else if ("ANTIPULGAS".equals(tipo)) filtros.add(cb.equal(root.type(), AntipulgasEntity.class));

            if (stockMaximo != null) filtros.add(cb.lessThanOrEqualTo(root.get("stock"), stockMaximo));

            Expression<String> texto = nombreCompleto(root, cb);
            filtros.add(Consultas.cadaPalabra(cb, busqueda, palabra -> Consultas.contiene(cb, texto, palabra)));

            return cb.and(filtros.toArray(Predicate[]::new));
        };
    }

    private static Expression<String> nombreCompleto(Root<ProductoEntity> root, CriteriaBuilder cb) {
        return Consultas.unir(cb, root.get("marca"), root.get("nombre"));
    }

    // Mismo calculo que PrecioOfertaHelper: si la oferta esta vigente se ordena por el precio con descuento
    private static Expression<Number> precioVigente(Root<ProductoEntity> root, AbstractQuery<?> query, CriteriaBuilder cb) {
        Join<ProductoEntity, OfertaEntity> oferta = root.join("oferta", JoinType.LEFT);
        LocalDate hoy = LocalDate.now();
        Predicate vigente = cb.and(
                cb.lessThanOrEqualTo(oferta.<LocalDate>get("fechaInicio"), hoy),
                cb.greaterThanOrEqualTo(oferta.<LocalDate>get("fechaFin"), hoy));

        Expression<BigDecimal> precio = root.get("precio");
        Expression<Number> factor = cb.diff(1, cb.quot(oferta.<Double>get("porcentaje"), 100));
        return cb.<Number>selectCase()
                .when(vigente, cb.prod(precio, factor))
                .otherwise(precio);
    }
}
