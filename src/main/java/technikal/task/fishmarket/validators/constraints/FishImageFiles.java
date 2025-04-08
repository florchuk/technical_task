package technikal.task.fishmarket.validators.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import technikal.task.fishmarket.validators.validators.FishImageFilesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FishImageFilesValidator.class)
public @interface FishImageFiles {
    String message() default "{technikal.task.fishmarket.validators.constraints.FishImageFiles.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
