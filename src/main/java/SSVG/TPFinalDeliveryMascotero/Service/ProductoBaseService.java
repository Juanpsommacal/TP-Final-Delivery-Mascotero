package SSVG.TPFinalDeliveryMascotero.Service;

import SSVG.TPFinalDeliveryMascotero.Exception.EmptyUpdateFieldException;
import SSVG.TPFinalDeliveryMascotero.Exception.InactiveResourceException;
import SSVG.TPFinalDeliveryMascotero.Exception.ResourceNotFoundException;
import SSVG.TPFinalDeliveryMascotero.Model.DTO.Request.Producto.ProductoUpdateRequestDTO;
import SSVG.TPFinalDeliveryMascotero.Model.Producto.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Template Method: el algoritmo de edicion y la baja logica se escriben una sola vez, aca.
// Cada tipo de producto solo completa lo que le es propio.
//   E: la entidad (AlimentoEntity, AntipulgasEntity)
//   U: el DTO de edicion
//   R: el DTO de respuesta
public abstract class ProductoBaseService<E extends ProductoEntity, U extends ProductoUpdateRequestDTO, R> {

    protected final JpaRepository<E, Long> repository;

    protected ProductoBaseService(JpaRepository<E, Long> repository) {
        this.repository = repository;
    }

    // Template Method: el esqueleto del algoritmo. Es final para que las hijas no cambien el orden de los pasos.
    public final R update(U request, Long id) {
        E entity = getEntityById(id);
        validateActive(entity);                 // paso fijo
        updateCommonFields(entity, request);    // paso fijo (compartido)
        updateSpecificFields(entity, request);  // operacion primitiva
        validateSpecificRules(entity);          // gancho opcional
        return toResponse(repository.save(entity));
    }

    ///----- Operaciones primitivas y gancho -----

    // Carga los campos propios del tipo de producto
    protected abstract void updateSpecificFields(E entity, U request);

    // Convierte la entidad en el DTO de respuesta del tipo
    protected abstract R toResponse(E entity);

    // Nombre del tipo para los mensajes de error ("alimento", "antipulgas")
    protected abstract String nombreTipo();

    // Gancho: reglas propias del tipo despues de cargar los datos. Por defecto no valida nada.
    protected void validateSpecificRules(E entity) { }

    ///----- Pasos comunes -----

    public E getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El " + nombreTipo() + " con la ID: " + id + " no existe"));
    }

    // Baja logica
    public void deleteById(Long id) {
        E entity = getEntityById(id);
        if (!entity.getActivo())
            throw new InactiveResourceException("El producto ya fue eliminado");

        entity.setActivo(false);
        repository.save(entity);
    }

    private void validateActive(E entity) {
        if (!entity.getActivo()) {
            throw new InactiveResourceException("El producto esta dado de baja");
        }
    }

    private void updateCommonFields(E entity, U request) {
        if (request.getNombre() != null) {
            if (request.getNombre().trim().isEmpty()) {
                throw new EmptyUpdateFieldException("El nombre no puede ser solo espacios en blanco");
            }
            entity.setNombre(request.getNombre());
        }

        if (request.getDescripcion() != null) {
            if (request.getDescripcion().trim().isEmpty()) {
                throw new EmptyUpdateFieldException("La descripcion no puede ser solo espacios en blanco");
            }
            entity.setDescripcion(request.getDescripcion());
        }

        if (request.getPrecio() != null) {
            entity.setPrecio(request.getPrecio());
        }

        if (request.getStock() != null) {
            entity.setStock(request.getStock());
        }

        if (request.getMarca() != null) {
            if (request.getMarca().trim().isEmpty()) {
                throw new EmptyUpdateFieldException("La marca no puede ser solo espacios en blanco");
            }
            entity.setMarca(request.getMarca());
        }
    }
}
