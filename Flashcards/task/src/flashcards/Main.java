package flashcards;

import java.util.*;
import java.io.*;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private static boolean validateTerm(String term, Map<String, String> cards) {
        if (cards.containsKey(term)) {
            System.out.println("The card " + "\"" + term + "\"" + " already exists.\n");
            return false;
        }
        return true;
    }

    private static boolean validateDefinition(String definition, Map<String, String> cards) {
        if (cards.containsValue(definition)) {
            System.out.println("The definition " + "\"" + definition + "\"" + " already exists.\n");
            return false;
        }
        return true;
    }

    private static void addCard(Map<String, String> cards) {
        System.out.println("The card:");
        SCANNER.nextLine();
        String term = SCANNER.nextLine();

        if (validateTerm(term, cards)) {
            System.out.println("The definition of the card:");
            String definition = SCANNER.next();
            if (validateDefinition(definition, cards)) {
                cards.put(term, definition);
                System.out.println("The pair (\"" + term + "\":\"" + definition + "\") has been added.\n");
            }
        }
    }

    private static void removeCard(Map<String, String> cards) {
        System.out.println("The card:");
        SCANNER.nextLine();
        String term = SCANNER.nextLine();

        if (cards.containsKey(term)) {
            cards.remove(term);
            System.out.println("The card has been removed.\n");
        } else {
            System.out.println("Can't remove \"" + term + "\": there is no such card.\n");
        }
    }

    private static void validateAnswer(String key, String answer, Map<String, String> cards) {
        String tempKey = "";
        if (cards.get(key).equals(answer)) {
            System.out.println("Correct answer.");
            return;
        }
        for (String k : cards.keySet()) {
            if (answer.equals(cards.get(k))) {
                tempKey = k;
            }
        }
        if (!tempKey.isEmpty()) {
            System.out.println("Wrong answer. The correct one is \"" + cards.get(key) + "\", you've just written the definition of \"" + tempKey + "\".");
        } else {
            System.out.println("Wrong answer. The correct one is \"" + cards.get(key) + "\".");
        }
    }

    private static void askForCard(Map<String, String> cards) {
        if (cards.size() == 0) {
            return;
        }

        Set<String> tempTerms = cards.keySet();
        String[] terms = tempTerms.toArray(new String[0]);

        System.out.println("How many times to ask?");
        int asks = SCANNER.nextInt();
        SCANNER.nextLine();

        for (int i = 0; i < asks; i++) {
            String term = terms[RANDOM.nextInt(terms.length)];
            System.out.println("Print the definition of \"" + term + "\":");
            String answer = SCANNER.nextLine();
            validateAnswer(term, answer, cards);
        }
    }

    private static void saveCards(Map<String, String> cards) {
        System.out.println("File name:");
        String filename = SCANNER.next();
        File file = new File(filename);

        try (PrintWriter writer = new PrintWriter(file)) {
            for (Map.Entry<String, String> entry : cards.entrySet()) {
                writer.println(entry.getKey() + "/" + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("An exception occurs " + e.getMessage());
        }
        System.out.println(cards.size() + " cards have been saved.\n");
    }

    private static void loadCards(Map<String, String> cards) {
        System.out.println("File name:");
        String filename = SCANNER.next();
        File file = new File(filename);
        List<String> tempCards = new ArrayList<>();
        String term = "";
        String definition = "";
        int cnt = 0;

        try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    tempCards = Arrays.asList(scanner.nextLine().split("/"));
                    term = tempCards.get(0).replace("/", "");
                    definition = tempCards.get(1);
                    cards.put(term, definition);
                    cnt++;
                }
                System.out.println(cnt + " cards have been loaded.\n");
        } catch (Exception e) {
            System.out.println("File not found.\n");
        }
    }

    public static void main(String[] args) {
        Map<String, String> cards = new HashMap<>();

        while (true) {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            switch (SCANNER.next()) {
                case "add":
                    addCard(cards);
                    break;
                case "remove":
                    removeCard(cards);
                    break;
                case "ask":
                    askForCard(cards);
                    break;
                case "export":
                    saveCards(cards);
                    break;
                case "import":
                    loadCards(cards);
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    System.exit(0);
            }
        }
    }
}