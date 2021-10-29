import java.util.ArrayList;
import java.util.Arrays;

public class Readability {

    public static final String FOLDER_PREFIX = "./data/Texts/AllTexts/";

    public static void main(String[] args) {

        ArrayList<DataInfo> docs = TextLib.readDocInfo( "./data/Texts/allfeatures-ose-final.csv");
        double totalError = 0;
        int fileNotFoundCount = 0;

        for(DataInfo doc : docs) {
            String filename = doc.getFileName();

            String text = TextLib.readFileAsString(FOLDER_PREFIX + fileNameManipulation(filename));

            ArrayList<String> sentences = TextLib.splitIntoSentences(text);
            System.out.println(sentences.size());

            if (sentences.size() != 0) {
                double prediction = FKReadability(sentences);
                System.out.println(doc.toString() + ":" + prediction);
                double error = (prediction - doc.getFleschScore());
                totalError += Math.abs(error);
            } else {
                fileNotFoundCount++;
            }
        }

        System.out.println("Average error is: " + totalError/ (docs.size() - fileNotFoundCount) );



        // TODO:  Break each sentence into words.
        // TODO:  Force to lower-case and strip out all puctuation for doing syllable counts.
    }

    private static void testSyllableMethod() {
        ArrayList<Word> words = TextLib.readSyllablesFile("data/syllables.txt");

        double right = 0;
        for (Word w : words) {
            String word = w.getWord();
            int prediction = syllablesFor(word);

            if (prediction == w.getSyllables()) right++;
        }

        System.out.println("You got " + (right/words.size()) + " right");
    }

    private static int syllablesFor(String testWord) {
        boolean inVowelChain = false;
        int boundaries = 0;

        for (int i = 0; i < testWord.length(); i++) {
            String letter = testWord.substring(i, i+1);
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

    private static boolean isVowel(String letter) {
        return "aeiouy".contains(letter);
    }
    private static double FKReadability(ArrayList<String> sentences){
        double totalSyllables = 0;
        ArrayList<String[]> words = new ArrayList<>();
        double totalSentences = sentences.size();
        for (String sentence : sentences) {
            //System.out.println(sentence.length() + ": " + sentence);
        }
        double totalWords = 0;
        for(int i = 0; i < sentences.size(); i++){
            String[] currWords = sentences.get(i).split(" ");
            totalWords += currWords.length;
            words.add(currWords);
        }
        for(int i = 0; i < words.size(); i++){
            for (int j = 0; j < words.get(i).length; j++) {
                String currentWord = words.get(i)[j];
                totalSyllables +=  syllablesFor(currentWord);
            }
        }
        double score = 206.835 - 1.015*(totalWords / totalSentences) - 84.6*(totalSyllables / totalWords);
        return score;
    }

    private static String fileNameManipulation(String filename){
        String ending = filename.substring((filename.length()-8));
        filename = filename.substring(0, filename.length()-8);
            filename = filename.replace("-"," ");
            filename += ending;
            return filename;
    }


}
