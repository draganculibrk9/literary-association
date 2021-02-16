package goveed20.LiteraryAssociationApplication.elasticsearch.units;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(indexName = "literary-association-books", replicas = 0, type = "book")
public class BookIndexingUnit {
    @Id
    @Field(type = FieldType.Long, store = true)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String genre;

    @Field(type = FieldType.Boolean, store = true)
    private Boolean openAccess;

    @Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String writers;

    @Field(type = FieldType.Text, store = true)
    private String basicInfo;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String text;
}
