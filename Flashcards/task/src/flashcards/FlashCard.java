package flashcards;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class FlashCard {

    private final Map<String, String> cards = new HashMap<>();
    private final Map<String, Integer> cardMistakes = new HashMap<>();
    private final List<String> logs = new ArrayList<>();
    private final Scanner scanner;

    public FlashCard(Scanner scanner) {
        this.scanner = scanner;
    }

    public void add() {
        String card, definition;

        addLogAndPrint("The card:");

        card = scanner.nextLine();
        logs.add(card);

        if (cards.containsKey(card)) {
            addLogAndPrint("The card \"" + card + "\" already exists.\n");
            return;
        }

        addLogAndPrint("The definition of the card:");

        definition = scanner.nextLine();
        logs.add(definition);

        if (cards.containsValue(definition)) {
            addLogAndPrint("The definition \"" + definition + "\" already exists.\n");
            return;
        }

        cards.put(card, definition);

        addLogAndPrint("The pair (\"" + card + "\":\"" + definition + "\") has been added.\n");
    }

    public void remove() {
        String card;

        addLogAndPrint("The card:");

        card = scanner.nextLine();
        logs.add(card);

        if (cards.containsKey(card)) {
            cards.remove(card);
            cardMistakes.remove(card);
            addLogAndPrint("The card has been removed.\n");
        } else {
            addLogAndPrint("Can't remove \"" + card + "\": there is no such card.\n");
        }

    }

    public void importFromFile(String fileName) {
        int count = 0;

        logs.add(fileName);

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNext()) {
                var card = scanner.nextLine();
                var definition = scanner.nextLine();
                var mistakesLine = scanner.nextLine();
                int mistakes;
                if (!"null".equals(mistakesLine)) {
                    mistakes = Integer.parseInt(mistakesLine);
                } else {
                    mistakes = 0;
                }
                cards.put(card, definition);
                cardMistakes.put(card, mistakes);
                count++;
            }

            addLogAndPrint(count + " cards have been loaded.\n");
        } catch (IOException e) {
            addLogAndPrint("File not found.\n");
        }
    }

    public void exportToFile(final String fileName) {
        int count = 0;

        logs.add(fileName);

        try (PrintWriter pw = new PrintWriter(new File(fileName))) {
            for (Map.Entry<String, String> entry : cards.entrySet()) {
                pw.println(entry.getKey());
                pw.println(entry.getValue());
                pw.println(cardMistakes.get(entry.getKey()));
                count++;
            }

            addLogAndPrint(count + " cards have been saved.\n");
        } catch (IOException e) {
            addLogAndPrint("Cards have not been saved.\n");
        }
    }

    public void ask() {
        int timeToAsk;
        String card;

        addLogAndPrint("How many times to ask?");

        timeToAsk = Integer.parseInt(scanner.nextLine());
        logs.add(Integer.toString(timeToAsk));

        for (int i = 0; i < timeToAsk; i++) {
            var cardsList = new ArrayList<>(cards.keySet());
            var random = new Random();
            var position = random.nextInt(cardsList.size());
            card = cardsList.get(position);

            addLogAndPrint("Print the definition of \"" + card + "\":");

            String answer = scanner.nextLine();
            logs.add(answer);

            if (cards.containsValue(answer)) {
                if (answer.equals(cards.get(card))) {
                    addLogAndPrint("Correct answer.");
                } else {
                    addLogAndPrint("Wrong answer. The correct one is \"" + cards.get(card)
                            + "\", you've just written the definition of \""
                            + cards.entrySet().stream()
                            .filter(e -> answer.equals(e.getValue()))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .get()
                            + "\"");
                }
            } else {
                addLogAndPrint("Wrong answer. The correct one is \"" + cards.get(card) + "\".");
                if (!cardMistakes.containsKey(card)) {
                    cardMistakes.put(card, 1);
                } else {
                    cardMistakes.put(card, cardMistakes.get(card) + 1);
                }

            }
        }
        System.out.println();
    }

    public void log() {
        String fileName;

        addLogAndPrint("File name:");

        fileName = scanner.nextLine();
        logs.add(fileName);

        try (PrintWriter pw = new PrintWriter(new File(fileName))) {
            for (String log: logs) {
                pw.println(log);
            }

            addLogAndPrint("The log has been saved.\n");
        } catch (IOException e) {
            addLogAndPrint("The log has not been saved.\n");
        }
    }

    public void hardestCard() {
        if (cardMistakes.isEmpty()) {
            addLogAndPrint("There are no cards with errors.\n");
        } else {
            int maxErrors = cardMistakes.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .get()
                    .getValue();

            List<String> hardestCards = cardMistakes.entrySet().stream()
                    .filter(e -> e.getValue() == maxErrors)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            String log;
            if (hardestCards.size() == 1) {
                log = ("The hardest card is \""
                        + hardestCards.get(0)
                        +"\". You have " + maxErrors + " errors answering it.\n");
            } else {
                StringBuilder mistakeCardsNames = new StringBuilder();

                for (int i = 0; i < hardestCards.size(); i++) {
                    if (i == hardestCards.size() - 1) {
                        mistakeCardsNames.append("\"").append(hardestCards.get(i)).append("\"");
                    } else {
                        mistakeCardsNames.append("\"").append(hardestCards.get(i)).append("\"").append(", ");
                    }
                }

                log = ("The hardest card are "
                        + mistakeCardsNames
                        +". You have " + maxErrors + " errors answering them.\n");
            }
            addLogAndPrint(log);
        }
    }

    public void resetStats() {
        cardMistakes.values().forEach(v -> v = 0);
        cardMistakes.clear();
        addLogAndPrint("Card statistics has been reset.\n");
    }

    public void addLogAndPrint(final String log) {
        System.out.println(log);
        logs.add(log);
    }

    public void addLog(final String log) {
        logs.add(log);
    }


}