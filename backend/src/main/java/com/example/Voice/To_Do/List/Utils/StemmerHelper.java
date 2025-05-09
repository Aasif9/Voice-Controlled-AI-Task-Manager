package com.example.Voice.To_Do.List.Utils;

import com.example.Voice.To_Do.List.Constants.CommonConstants;
import org.springframework.data.util.Pair;

import java.util.Set;

public class StemmerHelper {
    public static Pair<String, String> getPlaceHolders(String text, Set<String> protectedWords) {
        String variable = null;
        for (String term : protectedWords) {
            if (text.contains(term)) {
                variable = term;
                text = text.replace(term, CommonConstants.PLACEHOLDER);
            }
        }
        return Pair.of(text, variable);
    }
}
