package goveed20.LiteraryAssociationApplication.elasticsearch.units;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(indexName = "literary-association-beta-readers", replicas = 0, type = "beta-reader")
public class BetaReaderIndexingUnit {
    @Id
    @Field(type = FieldType.Long, store = true)
    private Long id;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String name;

    @Field(type = FieldType.Keyword, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private List<String> genres;
}
