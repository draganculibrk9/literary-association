package goveed20.LiteraryAssociationApplication.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchQueryDTO {
    @NotNull
    @NotEmpty
    private List<SearchParamDTO> searchParams;

    @NotNull
    private Integer page;
}
