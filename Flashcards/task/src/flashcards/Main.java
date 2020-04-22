package flashcards;

import java.util.*;
import java.io.*;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static final List<String> log = new ArrayList<>();

    private static boolean validateTerm(String term, Map<String, String> cards) {
        if (cards.containsKey(term)) {
            System.out.println("The card " + "\"" + term + "\"" + " already exists.\n");
            log.add("The card " + "\"" + term + "\"" + " already exists.\n");
            return false;
        }
        return true;
    }

    private static boolean validateDefinition(String definition, Map<String, String> cards) {
        if (cards.containsValue(definition)) {
            System.out.println("The definition " + "\"" + definition + "\"" + " already exists.\n");
            log.add("The definition " + "\"" + definition + "\"" + " already exists.\n");
            return false;
        }
        return true;
    }

    private static void addCard(Map<String, String> cards, Map<String, Integer> mistakes) {
        System.out.println("The card:");
        log.add("The card:");
        //SCANNER.nextLine();
        String term = SCANNER.nextLine();
        log.add(term);

        if (validateTerm(term, cards)) {
            System.out.println("The definition of the card:");
            log.add("The definition of the card:");
            String definition = SCANNER.next();
            log.add(definition);
            if (validateDefinition(definition, cards)) {
                cards.put(term, definition);
                mistakes.put(term, 0);
                System.out.println("The pair (\"" + term + "\":\"" + definition + "\") has been added.\n");
                log.add("The pair (\"" + term + "\":\"" + definition + "\") has been added.\n");
                SCANNER.nextLine();
            }
        }
    }

    private static void removeCard(Map<String, String> cards, Map<String, Integer> mistakes) {
        System.out.println("The card:");
        log.add("The card:");
        String term = SCANNER.nextLine();
        log.add(term);

        if (cards.containsKey(term)) {
            cards.remove(term);
            mistakes.remove(term);
            System.out.println("The card has been removed.\n");
            log.add("The card has been removed.\n");
        } else {
            System.out.println("Can't remove \"" + term + "\": there is no such card.\n");
            log.add("Can't remove \"" + term + "\": there is no such card.\n");
        }
    }

    private static void validateAnswer(String key, String answer, Map<String, String> cards, Map<String, Integer> mistakes) {
        String tempKey = "";
        if (cards.get(key).equals(answer)) {
            System.out.println("Correct answer.");
            log.add("Correct answer.");
            return;
        }
        for (String k : cards.keySet()) {
            if (answer.equals(cards.get(k))) {
                tempKey = k;
            }
        }
        if (!tempKey.isEmpty()) {
            System.out.println("Wrong answer. (The correct one is \"" + cards.get(key) + "\", you've just written the definition of \"" + tempKey + "\" card.)");
            log.add("Wrong answer. The correct one is \"" + cards.get(key) + "\", you've just written the definition of \"" + tempKey + "\".");
            mistakes.put(key, mistakes.get(key) + 1);
        } else {
            System.out.println("Wrong answer. The correct one is \"" + cards.get(key) + "\".");
            mistakes.put(key, mistakes.get(key) + 1);
            log.add("Wrong answer. The correct one is \"" + cards.get(key) + "\".");
        }
    }

    private static void askForCard(Map<String, String> cards, Map<String, Integer> mistakes) {
        if (cards.size() == 0) {
            return;
        }

        Set<String> tempTerms = cards.keySet();
        String[] terms = tempTerms.toArray(new String[0]);

        System.out.println("How many times to ask?");
        log.add("How many times to ask?");
        int asks = SCANNER.nextInt();
        log.add(String.valueOf(asks));
        SCANNER.nextLine();

        for (int i = 0; i < asks; i++) {
            String term = terms[RANDOM.nextInt(terms.length)];
            System.out.println("Print the definition of \"" + term + "\":");
            log.add("Print the definition of \"" + term + "\":");
            String answer = SCANNER.nextLine();
            log.add(answer);
            validateAnswer(term, answer, cards, mistakes);
        }
    }

    private static void saveCards(Map<String, String> cards, Map<String, Integer> mistakes) {
        System.out.println("File name:");
        log.add("File name:");
        String filename = SCANNER.next();
        log.add(filename);
        File file = new File(filename);

        try (PrintWriter writer = new PrintWriter(file)) {
            for (Map.Entry<String, String> entry : cards.entrySet()) {
                writer.println(entry.getKey() + "/" + entry.getValue() + "/" + mistakes.get(entry.getKey()));
            }
        } catch (IOException e) {
            System.out.println("An exception occurs " + e.getMessage());
            log.add("An exception occurs " + e.getMessage());
        }
        System.out.println(cards.size() + " cards have been saved.\n");
        log.add(cards.size() + " cards have been saved.\n");
        SCANNER.nextLine();
    }

    private static void loadCards(Map<String, String> cards, Map<String, Integer> mistakes) {
        System.out.println("File name:");
        log.add("File name:");
        String filename = SCANNER.next();
        log.add(filename);
        File file = new File(filename);
        List<String> tempCards;
        String term;
        String definition;
        int errors;
        int cnt = 0;

        try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    tempCards = Arrays.asList(scanner.nextLine().split("/"));
                    term = tempCards.get(0);
                    definition = tempCards.get(1);
                    errors = Integer.parseInt(tempCards.get(2));
                    cards.put(term, definition);
                    mistakes.put(term, errors);
                    cnt++;
                }
                System.out.println(cnt + " cards have been loaded.\n");
                log.add(cnt + " cards have been loaded.\n");
        } catch (Exception e) {
            System.out.println("File not found.\n");
            log.add("File not found.\n");
        }
        SCANNER.nextLine();
    }

    private static void saveLog() {
        System.out.println("File name:");
        log.add("File name:");
        String filename = SCANNER.next();
        log.add(filename);
        File file = new File(filename);

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String info : log) {
                writer.println(info);
            }
        } catch (IOException e) {
            System.out.println("An exception occurs " + e.getMessage());
            log.add("An exception occurs " + e.getMessage());
        }
        System.out.println("The log has been saved.\n");
        SCANNER.nextLine();
    }

    private static void showHardestCard(Map<String, Integer> mistakes) {
        List<String> hardestCards = new ArrayList<>();
        int maxErrors = 0;
        for (Integer errors : mistakes.values()) {
            if (errors > maxErrors) {
                maxErrors = errors;
            }
        }
        if (maxErrors == 0) {
            System.out.println("There are no cards with errors.\n");
            log.add("There are no cards with errors.\n");
        } else {
            for (Map.Entry<String, Integer> entry : mistakes.entrySet()) {
                if (maxErrors == entry.getValue()) {
                    hardestCards.add(entry.getKey());
                }
            }
            if (hardestCards.size() == 1) {
                System.out.println("The hardest card is \"" + hardestCards.get(0) + "\". You have " + maxErrors + " errors answering it.\n");
                log.add("The hardest card is \"" + hardestCards.get(0) + "\". You have " + maxErrors + " errors answering it.\n");
            } else {
                System.out.print("The hardest cards are ");
                log.add("The hardest cards are ");
                for (int i = 0; i < hardestCards.size(); i++) {
                    if (i < hardestCards.size() - 1) {
                        System.out.print("\"" + hardestCards.get(i) + "\", ");
                        log.add("\"" + hardestCards.get(i) + "\", ");
                    } else {
                        System.out.println("\"" + hardestCards.get(i) + "\". You have " + maxErrors + " errors answering it.\n");
                        log.add("\"" + hardestCards.get(i) + "\". You have " + maxErrors + " errors answering it.\n");
                    }
                }
            }
        }
    }

    private static void resetStats(Map<String, Integer> mistakes) {
        for (Map.Entry<String, Integer> entry : mistakes.entrySet()) {
            entry.setValue(0);
        }
        System.out.println("Card statistics has been reset.\n");
        log.add("Card statistics has been reset.\n");
    }

    public static void main(String[] args) {
        Map<String, String> cards = new HashMap<>();
        Map<String, Integer> mistakes = new HashMap<>();

        while (true) {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            log.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String choice = SCANNER.nextLine();
            log.add(choice);
            switch (choice) {
                case "add":
                    addCard(cards, mistakes);
                    break;
                case "remove":
                    removeCard(cards, mistakes);
                    break;
                case "ask":
                    askForCard(cards, mistakes);
                    break;
                case "export":
                    saveCards(cards, mistakes);
                    break;
                case "import":
                    loadCards(cards, mistakes);
                    break;
                case "log":
                    saveLog();
                    break;
                case "hardest card":
                    showHardestCard(mistakes);
                    break;
                case "reset stats":
                    resetStats(mistakes);
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    System.exit(0);
            }
        }
    }
}