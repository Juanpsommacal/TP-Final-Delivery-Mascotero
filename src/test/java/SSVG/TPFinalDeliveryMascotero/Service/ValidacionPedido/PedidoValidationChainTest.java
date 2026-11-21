package SSVG.TPFinalDeliveryMascotero.Service.ValidacionPedido;

import SSVG.TPFinalDeliveryMascotero.Exception.DuplicateResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.InsufficientStockException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotAssociatedException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.DireccionMapper;
import SSVG.TPFinalDeliveryMascotero.Model.ClienteEntity;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.DetallePedido.DetallePedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Direccion.DireccionCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Pedido.PedidoCreateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DireccionEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AlimentoEntity;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import SSVG.TPFinalDeliveryMascotero.Service.ClienteService;
import SSVG.TPFinalDeliveryMascotero.Service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Pruebas del patron Chain of Responsibility: cada eslabon por separado y la cadena completa
class PedidoValidationChainTest {

    private ClienteService clienteService;
    private ProductoService productoService;
    private DireccionMapper direccionMapper;
    private PedidoValidationChain chain;

    private final ClienteEntity cliente = new ClienteEntity();
    private final DireccionEntity direccion = new DireccionEntity();

    @BeforeEach
    void armarCadena() {
        clienteService = mock(ClienteService.class);
        productoService = mock(ProductoService.class);
        direccionMapper = mock(DireccionMapper.class);

        when(clienteService.getEntityById(1L)).thenReturn(cliente);
        when(direccionMapper.toEntity(any())).thenReturn(direccion);
        when(clienteService.hasDireccion(cliente, direccion)).thenReturn(true);

        chain = new PedidoValidationChain(
                new ClienteExisteHandler(clienteService),
                new DireccionDelClienteHandler(clienteService, direccionMapper),
                new ProductosRepetidosHandler(),
                new ProductosActivosHandler(productoService));
    }

    private static PedidoCreateRequestDTO request(Long... productoIds) {
        List<DetallePedidoCreateRequestDTO> detalles = java.util.Arrays.stream(productoIds)
                .map(id -> new DetallePedidoCreateRequestDTO(id, 1))
                .toList();
        return new PedidoCreateRequestDTO(1L, new DireccionCreateRequestDTO(), detalles);
    }

    private ProductoEntity producto(Long id, boolean activo) {
        AlimentoEntity producto = new AlimentoEntity();
        producto.setId(id);
        producto.setMarca("Marca" + id);
        producto.setNombre("Producto" + id);
        producto.setActivo(activo);
        when(productoService.getEntityById(id)).thenReturn(producto);
        return producto;
    }

    @Test
    void conTodoValidoDejaClienteDireccionYProductosEnElContexto() {
        ProductoEntity p1 = producto(10L, true);
        ProductoEntity p2 = producto(20L, true);
        PedidoValidationContext contexto = new PedidoValidationContext(request(10L, 20L));

        chain.validar(contexto);

        assertThat(contexto.getCliente()).isSameAs(cliente);
        assertThat(contexto.getDireccion()).isSameAs(direccion);
        assertThat(contexto.getProducto(10L)).isSameAs(p1);
        assertThat(contexto.getProducto(20L)).isSameAs(p2);
    }

    @Test
    void siElClienteNoExisteSeCortaEnElPrimerEslabon() {
        when(clienteService.getEntityById(1L)).thenThrow(new ResourceNotFoundException("El cliente no existe"));

        assertThatThrownBy(() -> chain.validar(new PedidoValidationContext(request(10L))))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(clienteService, never()).hasDireccion(any(), any());
        verifyNoInteractions(productoService);
    }

    @Test
    void laDireccionTieneQueSerDelCliente() {
        when(clienteService.hasDireccion(cliente, direccion)).thenReturn(false);

        assertThatThrownBy(() -> chain.validar(new PedidoValidationContext(request(10L))))
                .isInstanceOf(ResourceNotAssociatedException.class)
                .hasMessage("El cliente no tiene esa direccion asociada");
        verifyNoInteractions(productoService);
    }

    @Test
    void losProductosRepetidosSeRechazanAntesDeBuscarlos() {
        assertThatThrownBy(() -> chain.validar(new PedidoValidationContext(request(10L, 10L))))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("No se puede repetir el mismo Producto en el detalle del pedido");
        verifyNoInteractions(productoService);
    }

    @Test
    void losProductosInactivosSeInformanTodosJuntos() {
        producto(10L, false);
        producto(20L, true);
        producto(30L, false);

        assertThatThrownBy(() -> chain.validar(new PedidoValidationContext(request(10L, 20L, 30L))))
                .isInstanceOfSatisfying(InsufficientStockException.class, e ->
                        assertThat(e.getErrorsMap().get("Productos inactivos: "))
                                .containsExactly("Marca10 Producto10", "Marca30 Producto30"));
    }

    @Test
    void cadaProductoSeBuscaUnaSolaVez() {
        producto(10L, true);
        producto(20L, true);

        chain.validar(new PedidoValidationContext(request(10L, 20L)));

        verify(productoService, times(1)).getEntityById(10L);
        verify(productoService, times(1)).getEntityById(20L);
    }
}
