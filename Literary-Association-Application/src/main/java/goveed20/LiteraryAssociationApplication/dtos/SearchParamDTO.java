package goveed20.LiteraryAssociationApplication.dtos;

import goveed20.LiteraryAssociationApplication.elasticsearch.utils.BooleanOperator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SearchParamDTO {
    @NotBlank
    private String key;

    @NotNull
    private String value;

    @NotNull
    private Boolean phraze;

    @NotNull
    private BooleanOperator booleanOperator;
}
