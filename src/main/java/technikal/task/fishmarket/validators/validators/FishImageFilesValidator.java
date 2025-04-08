package technikal.task.fishmarket.validators.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.validators.constraints.FishImageFiles;

import java.util.List;

public class FishImageFilesValidator implements ConstraintValidator<FishImageFiles, List<MultipartFile>> {
    @Override
    public boolean isValid(List<MultipartFile> multipartFileList, ConstraintValidatorContext context) {
        if (multipartFileList == null || multipartFileList.isEmpty()) {
            return false;
        }

        return multipartFileList.stream().noneMatch((MultipartFile multipartFile) -> multipartFile.isEmpty());
    }
}
