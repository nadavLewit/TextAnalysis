import java.util.ArrayList;

public class WordBucket {
    ArrayList <WordAndCount> wordBucket = new ArrayList<>();
    public WordBucket(){

    }
    public void add(WordAndCount item){
        wordBucket.add(item);
        for (int i = 0; i < wordBucket.size(); i++) {
            for (int j = i + 1; j < wordBucket.size(); j++) {
                if(wordBucket.get(j).getCount() <= wordBucket.get(i).getCount()){
                    wordBucket.set(i, wordBucket.get(j));
                }
            }
        }

    }
    public int getCountOf(String word){
        int count = 0;
        for (WordAndCount currWord : wordBucket) {
            if(word .equals(currWord.getWord())){
                count = currWord.getCount();
            }
        }
        return count;
    }
    public int size(){
        return wordBucket.size();
    }
    public int getNumUnique(){
        ArrayList<String> newList = new ArrayList<>();
        for (WordAndCount word : wordBucket) {
            if(!newList.contains(word)){
                newList.add(word.getWord());
            }
        }
        return newList.size();
    }
    public String getMostFreq() {
        return wordBucket.get(wordBucket.size() - 1).getWord();
    }
}

