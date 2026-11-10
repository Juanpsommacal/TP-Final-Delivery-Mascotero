package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

import java.math.BigDecimal;

/// Precio al que se compro un producto la ultima vez, para sugerirlo en una compra nueva.
public record UltimoPrecioCompraResponseDTO(Long productoId, BigDecimal precioUnitario) {}
