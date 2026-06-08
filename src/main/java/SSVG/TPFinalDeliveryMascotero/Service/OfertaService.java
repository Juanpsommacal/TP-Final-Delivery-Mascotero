package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Mapper.OfertaMapper;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Compra.OfertaRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Response.OfertaResponseDTO;
import SSVG.TPFinalDeliveryMascotero.Model.OfertaEntity;
import SSVG.TPFinalDeliveryMascotero.Repository.OfertaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OfertaService {

    private final OfertaRepository repository;
    private final OfertaMapper mapper;

    public OfertaResponseDTO create(OfertaRequestDTO requestDTO) {

        validarFechas(requestDTO.getFechaInicio(), requestDTO.getFechaFin());

        OfertaEntity oferta = mapper.toEntity(requestDTO);

        OfertaEntity saved = repository.save(oferta);

        return mapper.toResponse(saved);
    }

    public OfertaResponseDTO getById(Long id) {

        OfertaEntity oferta = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Oferta no encontrada con id: " + id));

        return mapper.toResponse(oferta);
    }

    public List<OfertaResponseDTO> getAll() {

        List<OfertaEntity> ofertas = repository.findAll();

        return mapper.toResponseDTOList(ofertas);
    }

    public OfertaResponseDTO update(Long id, OfertaRequestDTO requestDTO) {

        OfertaEntity oferta = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Oferta no encontrada con id: " + id));

        validarFechas(requestDTO.getFechaInicio(), requestDTO.getFechaFin());

        oferta.setNombre(requestDTO.getNombre());
        oferta.setDescripcion(requestDTO.getDescripcion());
        oferta.setPorcentaje(requestDTO.getPorcentaje());
        oferta.setFechaInicio(requestDTO.getFechaInicio());
        oferta.setFechaFin(requestDTO.getFechaFin());

        OfertaEntity updated = repository.save(oferta);

        return mapper.toResponse(updated);
    }

    public void delete(Long id) {

        OfertaEntity oferta = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Oferta no encontrada con id: " + id));

        repository.delete(oferta);
    }


    private void validarFechas(LocalDate inicio, LocalDate fin) {

        LocalDate hoy = LocalDate.now();

        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }

        if (inicio.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser menor a la fecha actual");
        }

        if (fin.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser menor a la fecha actual");
        }

        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser menor a la fecha de inicio");
        }
    }
}