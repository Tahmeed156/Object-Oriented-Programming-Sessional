package sample;

public class WordCount {

    private static int srl=1;
    Integer id;
    String word;
    Integer count;

    public WordCount(String str, int c) {
        this.id = srl++;
        this.word = str;
        this.count = c;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


}
