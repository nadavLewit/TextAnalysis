public class WordAndCount {
    private String word;
    private int count;
    public WordAndCount(String word, int count){
        this.word = word;
        this.count = count;

    }
    public String getWord(){
        return word;
    }
    public int getCount(){
        return count;
    }
    public void setWord(String word){
        this.word =word;
    }
    public void setCount(int count){
        this.count = count;
    }
}
