package SSVG.TPFinalDeliveryMascotero.Model.DTO.Response;

/// Mercaderia que hay que conseguir: lo que piden los pedidos en espera y no alcanza con el stock actual.
public record FaltanteProductoResponseDTO(Long productoId, String nombreProducto, int cantidadPedida,
                                          int stock, int faltante, int pedidos) {}
