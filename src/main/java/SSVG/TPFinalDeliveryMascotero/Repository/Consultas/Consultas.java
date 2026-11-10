package SSVG.TPFinalDeliveryMascotero.Repository.Consultas;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/// Piezas comunes de los listados paginados: tamanio de pagina, busqueda por palabras y orden.
public final class Consultas {

    private static final int TAMANIO_MAXIMO = 100;

    private Consultas() {}

    public static PageRequest pagina(int page, int size) {
        return PageRequest.of(Math.max(page, 0), Math.clamp(size, 1, TAMANIO_MAXIMO));
    }

    /// Cada palabra escrita tiene que coincidir, en cualquier orden (ej: "perez luro 1234").
    public static Predicate cadaPalabra(CriteriaBuilder cb, String busqueda, Function<String, Predicate> coincide) {
        if (busqueda == null || busqueda.isBlank()) return cb.conjunction();
        Predicate[] palabras = Arrays.stream(busqueda.trim().split("\\s+"))
                .map(coincide)
                .toArray(Predicate[]::new);
        return cb.and(palabras);
    }

    /// Tambien compara sin espacios, para que "proplan" encuentre "Pro Plan".
    public static Predicate contiene(CriteriaBuilder cb, Expression<String> texto, String palabra) {
        String patron = "%" + escapar(palabra.toLowerCase()) + "%";
        Expression<String> minusculas = cb.lower(texto);
        Expression<String> sinEspacios = cb.function("replace", String.class, minusculas, cb.literal(" "), cb.literal(""));
        return cb.or(cb.like(minusculas, patron, '\\'), cb.like(sinEspacios, patron, '\\'));
    }

    @SafeVarargs
    public static Expression<String> unir(CriteriaBuilder cb, Expression<String> primero, Expression<String>... resto) {
        Expression<String> texto = primero;
        for (Expression<String> parte : resto) {
            texto = cb.concat(cb.concat(texto, " "), parte);
        }
        return texto;
    }

    /// "saldo-desc" ordena por esa columna. A igual valor, el registro mas nuevo arriba.
    public static <T> Specification<T> ordenadoPor(String orden, Map<String, ExpresionColumna<T>> columnas,
                                                   String columnaInicial, boolean ascendenteInicial) {
        String[] partes = orden == null ? new String[0] : orden.split("-");
        boolean columnaValida = partes.length == 2 && columnas.containsKey(partes[0]);
        String columna = columnaValida ? partes[0] : columnaInicial;
        boolean ascendente = columnaValida ? partes[1].equals("asc") : ascendenteInicial;

        return (root, query, cb) -> {
            // Spring reutiliza la Specification para contar: ahi no hay que ordenar
            if (query != null && query.getResultType() != Long.class) {
                Expression<?> valor = columnas.get(columna).crear(root, query, cb);
                query.orderBy(ascendente ? cb.asc(valor) : cb.desc(valor), cb.desc(root.get("id")));
            }
            return cb.conjunction();
        };
    }

    // % y _ son comodines de LIKE: si los escribe el usuario se buscan tal cual
    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
