package com.example.Voice.To_Do.List.Analyzers;

import com.example.Voice.To_Do.List.Utils.StemmerHelper;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
@Component
public class MyAnalyzer extends Analyzer {
    private final CharArraySet stopWords;
    private final Set<String> protectedTerms;

    public MyAnalyzer(Set<String> stopwordsList, Set<String> protectedTermsList) {
//        HashSet<String> str = new HashSet<>();
//        str.add("for");
        this.stopWords = new CharArraySet(stopwordsList, true);
        this.protectedTerms = new HashSet<>(protectedTermsList);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        TokenStream tokenStream = new StopFilter(tokenizer, stopWords);
        tokenStream = new TokenFilter(tokenStream) {
            @Override
            public boolean incrementToken() throws IOException {
                if(!input.incrementToken()){
                    return false;
                }
                CharTermAttribute termAttribute = getAttribute(CharTermAttribute.class);
                String term = termAttribute.toString();
//                return protectedTerms.contains(term);
                if(protectedTerms.contains(term)){
                    return true;
                }
                return false;
            }
        };
        tokenStream = new PorterStemFilter(tokenStream);
        return new TokenStreamComponents(tokenizer, tokenStream);

//        Tokenizer source = new LowerCaseTokenizer();
//        return new TokenStreamComponents(source, new PorterStemFilter(source));
    }

    public String stem(String text){
        if(text == null || text.isEmpty()){
            return text;
        }
        Pair<String,String> placeHolders = StemmerHelper.getPlaceHolders(text, protectedTerms);
        System.out.println(text);
        text = placeHolders.getFirst();
        try(TokenStream tokenStream = tokenStream(null, new StringReader(text))){
            StringBuilder result = new StringBuilder();
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);

            tokenStream.reset();
            //on the basis of space it has words
            while(tokenStream.incrementToken()){
                result.append(charTermAttribute.toString()).append(" ");
            }
            tokenStream.end();
            String stemmedText = result.toString();
            stemmedText = stemmedText.replace("_PLACEHOLDER_", placeHolders.getSecond());
//            if(placeHolders.getSecond() != null){
//            }
            return stemmedText.trim();
        } catch (Exception e) {
            throw new RuntimeException("Error during text processing", e);
        }
    }

//    public static void main(String[] args) {
//        Set<String> stringSet = new HashSet<>();
//        stringSet.add("Nikes Shoes");
//        MyAnalyzer myAnalyzer = new MyAnalyzer(new HashSet<>(), stringSet);
//        String variable = myAnalyzer.stem("Nikes Shoes for Mens");
//        System.out.println(variable);
//    }
}
