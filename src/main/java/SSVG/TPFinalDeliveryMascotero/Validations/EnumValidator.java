package SSVG.TPFinalDeliveryMascotero.Validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> valoresPermitidos;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        valoresPermitidos = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(enumValue ->
                        enumValue.name().toUpperCase())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return true;
        }

        return valoresPermitidos.contains(
                value.trim().toUpperCase()
        );
    }
}

