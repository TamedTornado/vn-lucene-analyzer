package com.bk.vnanalyzer;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

/**
 * User: caomanhdat
 * Date: 5/22/13
 * Time: 3:28 PM
 */
public class VNAnalyzer extends StopwordAnalyzerBase {

    private byte typeSaving = 0;

    public VNAnalyzer(Version version) {
        super(version);
    }

    public VNAnalyzer(Version version, CharArraySet stopwords) {
        super(version, stopwords);
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader)
    {
    	return new HTMLStripCharFilter(reader);
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
        final Tokenizer src = new VNTokenizer(reader);
        TokenStream tok = new StandardFilter(matchVersion, src);
        tok = new LowerCaseFilter(matchVersion, tok);
        tok = new StopFilter(matchVersion, tok, stopwords);
        return new TokenStreamComponents(src, tok);
    }
}
