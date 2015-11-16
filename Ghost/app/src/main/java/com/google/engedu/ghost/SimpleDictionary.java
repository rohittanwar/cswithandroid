package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    public static int search(ArrayList<String> a, int start, int end, String val){
        Log.d("start",a.get(start));
        Log.d("end",a.get(end));
        if(end<start)
            return -1;
        int middle = (start + end)/2;
        if(start == end){
            Log.d("The",a.get(middle));

            if(a.get(middle).startsWith(val)){
                return middle;
            }
            else{
                return -1;
            }
        }
        else if(a.get(middle).startsWith(val))
        {
            return middle;
        }
        else if(val.compareTo(a.get(middle))<0){
            return search(a, start, middle - 1, val);
        }
        else{
            return search(a, middle + 1, end, val);
        }
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix=="")
            return words.get(0);
        int index=search(words,0,words.size()-1,prefix);
        if(index!=-1)
            return words.get(index);
       /*
        for (String entry : words) {
            if (entry.startsWith(prefix))
                return entry;
        }
*/
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
