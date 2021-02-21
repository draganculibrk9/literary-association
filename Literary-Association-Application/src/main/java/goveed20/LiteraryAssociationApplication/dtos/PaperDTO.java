package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaperDTO implements Comparator<PaperDTO> {
    private Long id;
    private String title;
    private MultipartFile file;
    private double similarProcent;

    @Override
    public int compare(PaperDTO o1, PaperDTO o2) {
        if (o1.getSimilarProcent() < o2.getSimilarProcent()) {
            return 1;
        } else {
            return -1;
        }
    }
}
