package goveed20.LiteraryAssociationApplication.elasticsearch.repositories;

import goveed20.LiteraryAssociationApplication.elasticsearch.units.BetaReaderIndexingUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BetaReaderIndexingUnitRepository extends ElasticsearchRepository<BetaReaderIndexingUnit, Long> {
}
