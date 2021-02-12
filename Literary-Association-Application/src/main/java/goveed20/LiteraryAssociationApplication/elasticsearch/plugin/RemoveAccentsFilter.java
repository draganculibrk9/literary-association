package goveed20.LiteraryAssociationApplication.elasticsearch.plugin;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class RemoveAccentsFilter extends TokenFilter {
    private LatCyrUtils LatCyrUtils;
    private final CharTermAttribute termAttribute;

    public RemoveAccentsFilter(TokenStream input) {
        super(input);
        termAttribute = input.addAttribute(CharTermAttribute.class);
    }


    @SuppressWarnings("static-access")
    @Override
    public boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            String text = termAttribute.toString();
            termAttribute.setEmpty();
            termAttribute.append(LatCyrUtils.removeAccents(text).replace("Dj", "D").replace("dj", "d"));
            return true;
        }
        return false;
    }

}