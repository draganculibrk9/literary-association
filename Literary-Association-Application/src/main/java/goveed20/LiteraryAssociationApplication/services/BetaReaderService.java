package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.elasticsearch.units.BetaReaderIndexingUnit;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetaReaderService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<BetaReaderIndexingUnit> getBetaReaders(String genre, Double latitude, Double longitude) {
        QueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(latitude, longitude)
                .distance(100, DistanceUnit.KILOMETERS);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(queryBuilder);
        boolQueryBuilder.must(QueryBuilders.matchQuery("genres", genre));

        SearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();

        return elasticsearchTemplate.queryForList(query, BetaReaderIndexingUnit.class);
    }
}
