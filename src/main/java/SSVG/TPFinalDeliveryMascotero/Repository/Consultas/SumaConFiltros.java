package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/// Totales de un listado: suma todas las filas que cumplen los filtros, no solo las de la pagina.
@Component
@RequiredArgsConstructor
public class SumaConFiltros {

    private final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public <T> BigDecimal sumar(Class<T> entidad, Specification<T> filtro, ExpresionColumna<T> valor) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<T> root = query.from(entidad);
        Expression<BigDecimal> monto = (Expression<BigDecimal>) valor.crear(root, query, cb);
        query.select(cb.coalesce(cb.sum(monto), BigDecimal.ZERO));
        query.where(filtro.toPredicate(root, query, cb));
        return entityManager.createQuery(query).getSingleResult();
    }
}
