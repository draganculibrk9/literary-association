package goveed20.LiteraryAssociationApplication.elasticsearch.utils;

import goveed20.LiteraryAssociationApplication.elasticsearch.units.BookIndexingUnit;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultMapper implements SearchResultMapper {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
        List<BookIndexingUnit> chunk = new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits()) {
            if (searchResponse.getHits().getHits().length <= 0) {
                return null;
            }
            Map<String, Object> source = hit.getSourceAsMap();
            String highlight = "";
            try {
                highlight = String.format("...%s...", hit.getHighlightFields().get("text").fragments()[0].toString());
            } catch (Exception ignored) {

            }
            BookIndexingUnit searchResult = BookIndexingUnit.builder()
                    .id(((Integer) source.get("id")).longValue())
                    .title((String) source.get("title"))
                    .basicInfo((String) source.get("basicInfo"))
                    .text(highlight)
                    .openAccess((Boolean) source.get("openAccess"))
                    .build();
            chunk.add(searchResult);
        }

        if (chunk.size() > 0) {
            return new AggregatedPageImpl(chunk, pageable, searchResponse.getHits().getTotalHits(),
                    searchResponse.getAggregations(), searchResponse.getScrollId(),
                    searchResponse.getHits().getMaxScore());
        }
        return null;
    }
}
