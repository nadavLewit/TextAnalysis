import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Document {
    private String text;
    private int totalWords;
    private int totalSentences;
    private double readability;
    private int totalSyllables;
    private int vocabularySize;
    private double averageWordsPerSentence;
    private double averageCharPerWord;
    private int rating;
    private double helpfulness;
    private String reviewerName;
    private ArrayList<String> words;
    private ArrayList<String> uniqueWords;
    private boolean isReal;
    private ArrayList<String> possesiveBin = new ArrayList<String>(Arrays.asList("my", "mine", "his", "hers", "their", "theirs", "your", "yours"));
    private Dictionary dict = new Dictionary();


    public static Document getDocumentFrom(String filepath, boolean isReal) {
        String text = readFileAsString(filepath);
        Document document = new Document(filepath, isReal);
        return document;
    }

    public Document(String text, boolean isReal) {
        this.text = text;
        this.totalSentences = splitIntoSentences().size();
        this.totalWords = splitIntoWords().size();
//        this.readability = calculateReadability();
        this.totalSyllables = calculateTotalSyllables();
        this.words = splitIntoWords();
        this.uniqueWords = UniqueWords();
        this.vocabularySize = uniqueWords.size();
        this.averageWordsPerSentence = calculateAverageWordsPerSentence();
        this.averageCharPerWord = calculateAverageCharPerWord();
        this.rating = getRating();
        this.helpfulness = getHelpfulness();
        this.isReal = isReal;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public ArrayList<String> getUniqueWords() {
        return uniqueWords;
    }

    public int getNumSentences() {
        return totalSentences;
    }

    public int getNumWords() {
        return totalWords;
    }

    public double getReadability() {
        return readability;
    }

    public int getTotalSyllables() {
        return totalSyllables;
    }

    public int getVocabularySize() {
        return vocabularySize;
    }

    public double getAverageWordsPerSentence() {
        return averageWordsPerSentence;
    }

    public double getAverageCharPerWord() {
        return averageCharPerWord;
    }

    public boolean isReal() {
        int counter = 0;
//        if (phraseRepetition()) return true;
//        counter++;
//        if (!areDictionaryWords()) {
//            counter--;
//        } else {
//            counter++;
//        }
        if (!containsPossesiveWords()) {
            counter--;
        } else {
            counter++;
        }
        if(totalWords < 8) {
            counter--;
        } else {
            counter++;
        }
//        if(isExtremeReview()) {
//            counter--;
//        } else {
//            counter++;
//        }
//        if(isHelpful()) {
//            counter++;
//        } else {
//            counter--;
//        }
        if(counter > 0) return true;
        return false;

    }

    private ArrayList<String> splitIntoSentences() {
        ArrayList<String> output = new ArrayList<>();

        Locale locale = Locale.US;
        BreakIterator breakIterator = BreakIterator.getSentenceInstance(locale);
        breakIterator.setText(text);

        int prevIndex = 0;
        int boundaryIndex = breakIterator.first();
        while (boundaryIndex != BreakIterator.DONE) {
            String sentence = text.substring(prevIndex, boundaryIndex).trim();
            if (sentence.length() > 0)
                output.add(sentence);
            prevIndex = boundaryIndex;
            boundaryIndex = breakIterator.next();
        }

        String sentence = text.substring(prevIndex).trim();
        if (sentence.length() > 0)
            output.add(sentence);

        return output;

    }

    public ArrayList<String> splitIntoWords() {
        ArrayList<String> words = new ArrayList<String>();
        for (String sentence : splitIntoSentences()) {
            String[] wordsWithPunctuation = sentence.split(" ");
            for (String word : wordsWithPunctuation) {
                word.toLowerCase();
                words.add(removePunctuation(word));
            }
        }
        return words;
    }

    private double calculateReadability() {
        return 206 - (1.015 * (totalWords / totalSentences) - (84.6 * (getTotalSyllables() / totalWords)));
    }

    private int calculateTotalSyllables() {
        int totalSyllables = 0;
        for (String word : splitIntoWords()) {
            totalSyllables += syllablesFor(word);
        }
        return totalSyllables;
    }

    private ArrayList<String> UniqueWords() {
        ArrayList<String> uniqueWords = this.words;
        for (int i = 0; i < uniqueWords.size(); i++) {
            for (int j = i; j < uniqueWords.size(); j++) {
                if (uniqueWords.get(i) == uniqueWords.get(j)) {
                    uniqueWords.remove(j);
                }
            }
        }
        return uniqueWords;
    }

    private double calculateAverageWordsPerSentence() {
        return totalWords / totalSentences;
    }

    public static String removePunctuation(String word) {
        String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
        String newWord = "";
        for (int i = 0; i < word.length(); i++) {
            String letter = word.substring(i, i + 1);
            if (ALPHABET.contains(letter)) {
                newWord += letter;
            }
        }
        return newWord;
    }

    private double calculateAverageCharPerWord() {
        double totalChar = 0;
        for (String word : splitIntoWords()) {
            totalChar += word.length();
        }
        return totalChar / totalWords;
    }

    public static String readFileAsString(String filename) {
        Scanner scanner;
        StringBuilder output = new StringBuilder();

        try {
            scanner = new Scanner(new FileInputStream(filename), "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                output.append(line.trim() + "\n");
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
        }

        return output.toString();
    }

    private static int syllablesFor(String testWord) {
        boolean inVowelChain = false;
        int boundaries = 0;

        for (int i = 0; i < testWord.length(); i++) {
            String letter = testWord.substring(i, i + 1);
            if (isVowel(letter)) {
                if (!inVowelChain) {
                    inVowelChain = true;
                    boundaries++;
                }
            } else {
                inVowelChain = false;
            }
        }

        return boundaries;
    }

    private boolean containsPossesiveWords() {
        for (String word : words) {
            if (possesiveWord(word)) return true;
        }
        return false;
    }

    private boolean possesiveWord(String word) {
        if (word.contains("'s")) return true;
        return containsWords(possesiveBin, word);
    }

    private boolean containsWords(ArrayList<String> wordBin, String word) {
        for (String wordBinWord : wordBin) {
            if (word.equalsIgnoreCase(wordBinWord)) return true;
        }
        return false;
    }

    private boolean areDictionaryWords() {
        for (String word : words) {
            if (!isDictionaryWord(word) && !beginsWithCapital(word)) return false;
        }
        return true;
    }

    private boolean beginsWithCapital(String word) {
        return ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(word.substring(0, 1)));
    }

    private boolean isDictionaryWord(String word) {
        return dict.isWord(word);
    }

    private int getRating() {
        return rating;
    }
    private boolean isExtremeReview() {
        return (rating > 4 || rating < 2);
    }
    private double getHelpfulness() {
        return helpfulness;
    }
    private boolean isHelpful() {
        return (helpfulness > 0.7);
    }

    private boolean phraseRepetition() {
        String phrase1 = "";
        String phrase2 = "";
        for (int a = 3; a < 6; a++) {
            for (int i = 0; i < words.size(); i++) {
                for (int n = 1; n < a; n++) {
                    phrase1 += words.get(i + n);
                }
                for (int j = i; j < words.size(); j++) {
                    for (int n = 1; n < a; n++) {
                        phrase2 += words.get(i);
                    }
                    if (phrase1.equalsIgnoreCase(phrase2)) return true;
                    {
                    }
                    ;
                    phrase2 = "";
                }
                phrase1 = "";
            }
        }
        return false;
    }
    public boolean getIsReal() {
        return isReal;
    }


    private static boolean isVowel(String letter) {
        return "aeiouy".contains(letter);
    }
}