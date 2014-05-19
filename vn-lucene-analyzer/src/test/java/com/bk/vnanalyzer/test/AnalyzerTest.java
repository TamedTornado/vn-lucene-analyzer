package com.bk.vnanalyzer.test;

import java.io.IOException;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bk.vnanalyzer.VNAnalyzer;
import com.bk.vnanalyzer.ViStopWordsProvider;

/**
 * User: caomanhdat
 * Date: 6/26/13
 * Time: 8:39 AM
 */
public class AnalyzerTest {
    IndexReader indexReader;
    
    @Before
    public void createDocument() throws IOException {
        Directory d = new RAMDirectory();
        VNAnalyzer viAnalyzer = new VNAnalyzer(Version.LUCENE_48,ViStopWordsProvider.getStopWordsFromClasspath("stopwords.txt"));

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48,viAnalyzer);
        IndexWriter indexWriter = new IndexWriter(d, config);


        FieldType fieldType = new FieldType();
        fieldType.setIndexed(true);
        fieldType.setStored(true);
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorOffsets(true);

        Document doc1 = new Document();
        //Theo UBND quận 9, đoạn từ ngã ba rạch Bà Ký đến ngã ba rạch Vàm Tắc đã cạn kiệt nguồn tài nguyên cát.
        doc1.add(new Field("content","Theo UBND quận 9, đoạn từ ngã ba rạch Bà Ký đến ngã ba rạch Vàm Tắc đã cạn kiệt nguồn tài nguyên cát.", fieldType));
        Document doc2 = new Document();
        doc2.add(new Field("content","viá»‡n cÃ´ng nghá»‡ thÃ´ng tin Ä‘áº¡i há»�c bÃ¡ch khoa hÃ  ná»™i bkviews", fieldType));
        indexWriter.addDocument(doc1);
        indexWriter.addDocument(doc2);
        indexWriter.close();

        indexReader = DirectoryReader.open(d);
    }

    @Test
    public void searchTest() throws IOException {

        IndexSearcher searcher = new IndexSearcher(indexReader);
        TopDocs result = searcher.search(new TermQuery(new Term("content", "bkviews")), 5);
        Assert.assertEquals(1,result.scoreDocs.length);
    }

    @Test
    public void termFreqTest() throws IOException {

        HashMap<String,Integer> termFreq = new HashMap<String, Integer>();
        termFreq.put("sinh viÃªn",1);
        termFreq.put("viá»‡n",1);
        termFreq.put("bÃ¡ch khoa",2);
        termFreq.put("cÃ´ng nghá»‡ thÃ´ng tin",1);
        termFreq.put("hÃ  ná»™i",2);
//        Fields
        Fields fields = MultiFields.getFields(indexReader);
        Terms terms = fields.terms("content");
        TermsEnum termsEnum = terms.iterator(null);
        BytesRef text;
        while ((text = termsEnum.next()) != null) {
            System.out.println(text.utf8ToString() + " " + termsEnum.docFreq());
            if(termFreq.get(text.utf8ToString())!= null) Assert.assertEquals((int)termFreq.get(text.utf8ToString()),termsEnum.docFreq());
        }
    }

//    @Test
//    public void termOffsetTest() throws IOException {
//
//        String docContent = indexReader.document(0).get("content");
//        Terms tfvs = indexReader.getTermVector(0,"content");
//        TermsEnum terms = tfvs.iterator(null);
//        BytesRef term = null;
//        while ((term = terms.next()) != null) {
//            DocsAndPositionsEnum docsAndPositionsEnum = terms.docsAndPositions(MultiFields.getLiveDocs(indexReader),null);
//            docsAndPositionsEnum.nextDoc();
//            docsAndPositionsEnum.nextPosition();
//            
//            System.out.printf("Term %s length: %d %d/%d %d/%d\n", term.utf8ToString(), term.utf8ToString().length(), docContent.indexOf(term.utf8ToString()), docsAndPositionsEnum.startOffset(),
//            		docContent.indexOf(term.utf8ToString())+term.utf8ToString().length(),docsAndPositionsEnum.endOffset());
//            
//            Assert.assertEquals(docContent.indexOf(term.utf8ToString()), docsAndPositionsEnum.startOffset());
//            Assert.assertEquals(docContent.indexOf(term.utf8ToString())+term.utf8ToString().length(),docsAndPositionsEnum.endOffset());
//        }
//    }
}
