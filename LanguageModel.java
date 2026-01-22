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
        String window = "";
        char c;
        List list = null;

        for(int i = 0; i < this.windowLength; i++){
            window += input.readChar();
        }

        while(!input.isEmpty()){
            c = input.readChar();
            if(this.CharDataMap.containsKey(window)){
                list = this.CharDataMap.get(window);
                list.update(c);
            }else{
                list = new List();
                list.addFirst(c);
                this.CharDataMap.put(window, list);
            }
            window = window.substring(1) + c;
        }
        
        for (List probs : this.CharDataMap.values())
            this.calculateProbabilities(probs);

	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {				
        int total = 0;
        double cp = 0;
        for(int i = 0; i < probs.getSize(); i++){
            total += probs.get(i).count;
        }

        for(int i = 0; i < probs.getSize(); i++){
            CharData data = probs.get(i);
            data.p =  ((double) data.count) / total;
            cp += data.p;
            data.cp = cp;
        }
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for(int i = 0; i < probs.getSize(); i++){
            CharData data = probs.get(i);
            if(data.cp >= r) return data.chr;
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

        if(initialText.length() < this.windowLength) {
            // System.out.println("Initial text shorter than window length, quitting.");
            return initialText;
        }
        
        // System.out.println("Creating string builder.");
        StringBuilder builder = new StringBuilder(initialText);
        String window = initialText.substring(initialText.length() - this.windowLength);
        // System.out.println("Initial window: " + window);
        while(builder.length() < textLength){
            // System.out.println("Trying to find CharData list");
            List value = this.CharDataMap.get(window);
            if(value == null) {
                return builder.toString();
            }
            
            char c = this.getRandomChar(value);
            // System.out.println("Found list! Appended letter: '" + c + "'");
            builder.append(c);
            window = window.substring(1) + c;
        }
        return builder.toString();
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
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];

        LanguageModel lm;
        if (randomGeneration)
            lm = new LanguageModel(windowLength);
        else
            lm = new LanguageModel(windowLength, 20);

        lm.train(fileName);
        System.out.println(lm.generate(initialText, generatedTextLength));
        }
}
