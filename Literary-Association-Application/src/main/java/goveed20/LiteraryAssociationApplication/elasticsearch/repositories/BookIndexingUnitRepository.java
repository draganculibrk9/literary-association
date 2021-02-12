package goveed20.LiteraryAssociationApplication.elasticsearch.repositories;

import goveed20.LiteraryAssociationApplication.elasticsearch.units.BookIndexingUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookIndexingUnitRepository extends ElasticsearchRepository<BookIndexingUnit, Long> {
}
