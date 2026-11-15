package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPago;
import SSVG.TPFinalDeliveryMascotero.Model.Enums.EstadoPedido;
import SSVG.TPFinalDeliveryMascotero.Model.PagoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.PedidoEntity;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PedidoConsultas {

    public static final Map<String, ExpresionColumna<PedidoEntity>> COLUMNAS = Map.of(
            "id", (root, query, cb) -> root.get("id"),
            "fecha", (root, query, cb) -> root.get("fecha"),
            "cliente", (root, query, cb) -> nombreCliente(root, cb),
            "total", (root, query, cb) -> root.get("montoTotal"),
            "saldo", PedidoConsultas::saldo
    );

    private PedidoConsultas() {}

    public static Specification<PedidoEntity> filtrar(String busqueda, EstadoPedido estado, EstadoPago estadoPago,
                                                      boolean conDeuda, boolean verCancelados, LocalDate desde, LocalDate hasta) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();

            // Los cancelados se ocultan salvo que se pidan o se filtre justo por ese estado
            if (!verCancelados && estado != EstadoPedido.CANCELADO) {
                filtros.add(cb.notEqual(root.get("estadoPedido"), EstadoPedido.CANCELADO));
            }
            if (estado != null) filtros.add(cb.equal(root.get("estadoPedido"), estado));
            if (conDeuda) filtros.add(root.get("estadoPago").in(EstadoPago.DEUDA, EstadoPago.DEUDA_PARCIAL));
            else if (estadoPago != null) filtros.add(cb.equal(root.get("estadoPago"), estadoPago));

            if (desde != null) filtros.add(cb.greaterThanOrEqualTo(root.<LocalDate>get("fecha"), desde));
            if (hasta != null) filtros.add(cb.lessThanOrEqualTo(root.<LocalDate>get("fecha"), hasta));

            Expression<String> texto = Consultas.unir(cb, nombreCliente(root, cb), root.get("calle"),
                    root.get("numero").as(String.class), root.get("id").as(String.class));
            filtros.add(Consultas.cadaPalabra(cb, busqueda, palabra -> Consultas.contiene(cb, texto, palabra)));

            return cb.and(filtros.toArray(Predicate[]::new));
        };
    }

    /// Lo que falta cobrar. Un pedido cancelado no debe nada.
    public static Expression<BigDecimal> saldo(Root<PedidoEntity> root, AbstractQuery<?> query, CriteriaBuilder cb) {
        Subquery<BigDecimal> pagado = query.subquery(BigDecimal.class);
        Root<PagoEntity> pago = pagado.from(PagoEntity.class);
        pagado.select(cb.coalesce(cb.sum(pago.<BigDecimal>get("monto")), BigDecimal.ZERO))
                .where(cb.equal(pago.get("pedido"), root));

        return cb.<BigDecimal>selectCase()
                .when(cb.equal(root.get("estadoPedido"), EstadoPedido.CANCELADO), cb.literal(BigDecimal.ZERO))
                .otherwise(cb.diff(root.<BigDecimal>get("montoTotal"), pagado));
    }

    /// Para el total a cobrar: un pedido con saldo a favor no le resta a los demas.
    public static Expression<BigDecimal> saldoPositivo(Root<PedidoEntity> root, AbstractQuery<?> query, CriteriaBuilder cb) {
        Expression<BigDecimal> saldo = saldo(root, query, cb);
        return cb.<BigDecimal>selectCase()
                .when(cb.greaterThan(saldo, BigDecimal.ZERO), saldo)
                .otherwise(cb.literal(BigDecimal.ZERO));
    }

    private static Expression<String> nombreCliente(Root<PedidoEntity> root, CriteriaBuilder cb) {
        return Consultas.unir(cb, root.get("cliente").get("nombre"), root.get("cliente").get("apellido"));
    }
}
