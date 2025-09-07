import java.util.*;

public class wordcounter {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WordCounter counter = new WordCounter();
        
        System.out.println("=== Word Counter ===");
        System.out.println("Enter your text (type 'END' on a new line to finish):");
        
        StringBuilder text = new StringBuilder();
        String line;
        
        // Read multiple lines of input
        while (!(line = scanner.nextLine()).equals("END")) {
            text.append(line).append(" ");
        }
        
        String inputText = text.toString().trim();
        
        if (inputText.isEmpty()) {
            System.out.println("No text entered!");
            return;
        }
        
        // Analyze the text
        counter.analyzeText(inputText);
        counter.displayResults();
        
        scanner.close();
    }
    
    static class WordCounter {
        private Map<String, Integer> wordFrequency;
        private int totalWords;
        private int uniqueWords;
        
        public WordCounter() {
            this.wordFrequency = new HashMap<>();
            this.totalWords = 0;
            this.uniqueWords = 0;
        }
        
        public void analyzeText(String text) {
            // Clear previous analysis
            wordFrequency.clear();
            totalWords = 0;
            uniqueWords = 0;
            
            // Split text into words and clean them
            String[] words = text.toLowerCase()
                                .replaceAll("[^a-zA-Z0-9\\s]", "")
                                .split("\\s+");
            
            for (String word : words) {
                if (!word.trim().isEmpty()) {
                    totalWords++;
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                }
            }
            
            uniqueWords = wordFrequency.size();
        }
        
        public void displayResults() {
            System.out.println("\n=== ANALYSIS RESULTS ===");
            System.out.println("Total words: " + totalWords);
            System.out.println("Unique words: " + uniqueWords);
            System.out.println();
            
            // Display word frequencies sorted by frequency (descending)
            System.out.println("Word Frequency Analysis:");
            System.out.println("------------------------");
            
            wordFrequency.entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .forEach(entry -> {
                            String word = entry.getKey();
                            int frequency = entry.getValue();
                            double percentage = (frequency * 100.0) / totalWords;
                            System.out.printf("%-15s: %3d times (%.1f%%)\n", 
                                            word, frequency, percentage);
                        });
            
            // Additional statistics
            System.out.println("\n=== ADDITIONAL STATISTICS ===");
            displayTopWords(5);
            displayWordLengthStats();
        }
        
        private void displayTopWords(int topN) {
            System.out.println("Top " + topN + " most frequent words:");
            wordFrequency.entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(topN)
                        .forEach(entry -> 
                            System.out.println("  " + entry.getKey() + " (" + entry.getValue() + " times)")
                        );
            System.out.println();
        }
        
        private void displayWordLengthStats() {
            Map<Integer, Integer> lengthFreq = new HashMap<>();
            double totalLength = 0;
            
            for (String word : wordFrequency.keySet()) {
                int length = word.length();
                int frequency = wordFrequency.get(word);
                lengthFreq.put(length, lengthFreq.getOrDefault(length, 0) + frequency);
                totalLength += length * frequency;
            }
            
            double averageLength = totalLength / totalWords;
            
            System.out.println("Word Length Statistics:");
            System.out.printf("Average word length: %.2f characters\n", averageLength);
            
            System.out.println("Words by length:");
            lengthFreq.entrySet()
                     .stream()
                     .sorted(Map.Entry.comparingByKey())
                     .forEach(entry -> 
                         System.out.printf("  %d characters: %d words\n", 
                                         entry.getKey(), entry.getValue())
                     );
        }
        
        // Getter methods for accessing results programmatically
        public int getTotalWords() { return totalWords; }
        public int getUniqueWords() { return uniqueWords; }
        public Map<String, Integer> getWordFrequency() { return new HashMap<>(wordFrequency); }
        
        public int getWordCount(String word) {
            return wordFrequency.getOrDefault(word.toLowerCase(), 0);
        }
        
        public List<String> getMostFrequentWords(int limit) {
            return wordFrequency.entrySet()
                               .stream()
                               .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                               .limit(limit)
                               .map(Map.Entry::getKey)
                               .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
}