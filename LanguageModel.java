import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        In input = new In(fileName);
        int index = 0;
        while(!input.isEmpty()){
            String line = input.readLine();
            while(index + this.windowLength + 1 <= line.length()){
                String window = line.substring(index, index + this.windowLength);
                char followingLetter = line.charAt(index + this.windowLength);
                List list = null;
                if(CharDataMap.containsKey(window)){
                    list = CharDataMap.get(window);
                    list.update(followingLetter);
                }else{
                    list = new List();
                    list.addFirst(followingLetter);
                    CharDataMap.put(window, list);
                }
                this.calculateProbabilities(list);
                index ++;
            }
        }
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {				
		// Your code goes here
        int total = 0;
        double cp = 0;
        for(int i = 0; i < probs.getSize(); i++){
            total += probs.get(i).count;
        }
        
        for(int i = 0; i < probs.getSize(); i++){
            CharData data = probs.get(i);
            data.p =  ((double) data.count) / total;
            data.cp += data.p + cp;
            cp += data.p;
        }
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for(int i = 0; i < probs.getSize(); i++){
            CharData data = probs.get(i);
            if(data.cp > r) return data.chr;
        }

		return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		// Your code goes here
        return "";
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
		// Your code goes here
    }
}
