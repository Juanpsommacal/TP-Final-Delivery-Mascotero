package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyUpdateFieldException;
import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.InvalidWeightRangeException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Mapper.AntipulgasMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Antipulgas.AntipulgasUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.Categorias.AntipulgasEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.AntipulgasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Pruebas del patron Template Method: los pasos comunes los hace ProductoBaseService
// y el gancho validateSpecificRules lo completa AntipulgasService
class AntipulgasServiceTest {

    private AntipulgasRepository repository;
    private AntipulgasService service;
    private AntipulgasEntity antipulgas;

    @BeforeEach
    void setUp() {
        repository = mock(AntipulgasRepository.class);
        service = new AntipulgasService(repository, mock(AntipulgasMapper.class));

        antipulgas = new AntipulgasEntity();
        antipulgas.setId(3L);
        antipulgas.setNombre("Pipeta");
        antipulgas.setKgMin(2.0);
        antipulgas.setKgMax(10.0);
        when(repository.findById(3L)).thenReturn(Optional.of(antipulgas));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void actualizaCamposComunesYEspecificos() {
        AntipulgasUpdateRequestDTO request = new AntipulgasUpdateRequestDTO();
        request.setNombre("Pipeta Max");
        request.setKgMax(20.0);

        service.updateAntipulgas(request, 3L);

        assertThat(antipulgas.getNombre()).isEqualTo("Pipeta Max");
        assertThat(antipulgas.getKgMax()).isEqualTo(20.0);
        verify(repository).save(antipulgas);
    }

    @Test
    void elGanchoValidaElRangoDePeso() {
        AntipulgasUpdateRequestDTO request = new AntipulgasUpdateRequestDTO();
        request.setKgMin(15.0);

        assertThatThrownBy(() -> service.updateAntipulgas(request, 3L))
                .isInstanceOf(InvalidWeightRangeException.class)
                .hasMessage("El peso minimo no puede ser superior al peso maximo");
        verify(repository, never()).save(any());
    }

    @Test
    void noSeEditaUnProductoDadoDeBaja() {
        antipulgas.setActivo(false);

        assertThatThrownBy(() -> service.updateAntipulgas(new AntipulgasUpdateRequestDTO(), 3L))
                .isInstanceOf(InactiveResourceException.class)
                .hasMessage("El producto esta dado de baja");
    }

    @Test
    void elNombreNoPuedeSerSoloEspacios() {
        AntipulgasUpdateRequestDTO request = new AntipulgasUpdateRequestDTO();
        request.setNombre("   ");

        assertThatThrownBy(() -> service.updateAntipulgas(request, 3L))
                .isInstanceOf(EmptyUpdateFieldException.class)
                .hasMessage("El nombre no puede ser solo espacios en blanco");
    }

    @Test
    void elMensajeDeInexistenteNombraElTipo() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getEntityById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("El antipulgas con la ID: 99 no existe");
    }

    @Test
    void laBajaEsLogica() {
        service.deleteById(3L);

        assertThat(antipulgas.getActivo()).isFalse();
        assertThatThrownBy(() -> service.deleteById(3L))
                .isInstanceOf(InactiveResourceException.class)
                .hasMessage("El producto ya fue eliminado");
    }
}
