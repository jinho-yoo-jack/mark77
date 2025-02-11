package jack.labs.mark77.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class ReqAddNewProduct {
    @NotBlank
    private long productId;
    @NotBlank
    @Min(1)
    private String size;

}
