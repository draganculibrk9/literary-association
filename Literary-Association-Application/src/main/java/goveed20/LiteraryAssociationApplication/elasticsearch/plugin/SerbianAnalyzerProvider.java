package goveed20.LiteraryAssociationApplication.elasticsearch.plugin;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;

@SuppressWarnings("rawtypes")
public class SerbianAnalyzerProvider extends AbstractIndexAnalyzerProvider {

    public static final String NAME = "serbian_analyzer";
    private final SerbianAnalyzer analyzer;

    public SerbianAnalyzerProvider(IndexSettings indexSettings, String name, Settings settings) {
        super(indexSettings, name, settings);
        analyzer = new SerbianAnalyzer();
        analyzer.setVersion(version);
    }

    @Override
    public SerbianAnalyzer get() {
        return analyzer;
    }

}
