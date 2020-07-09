package flashcards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        FlashCard flashCard = new FlashCard(scanner);
        List<String> argsList = new ArrayList<>(Arrays.asList(args));

        if (argsList.contains("-import")) {
            int pathIndex = argsList.indexOf("-import") + 1;
            flashCard.importFromFile(argsList.get(pathIndex));
        }

        while (!exit) {
            flashCard.addLogAndPrint("Input the action (add, remove, import, export, ask, exit):");
            String action = scanner.nextLine();
            flashCard.addLog(action);
            switch (action) {
                case "add": {
                    flashCard.add();
                    break;
                }
                case "remove": {
                    flashCard.remove();
                    break;
                }
                case "import": {
                    flashCard.addLogAndPrint("File name:");
                    flashCard.importFromFile(scanner.nextLine());
                    break;
                }
                case "export": {
                    flashCard.addLogAndPrint("File name:");
                    flashCard.exportToFile(scanner.nextLine());
                    break;
                }
                case "ask": {
                    flashCard.ask();
                    break;
                }
                case "exit": {
                    exit = true;

                    System.out.println("Bye bye!");
                    if (argsList.contains("-export")) {
                        int pathIndex = argsList.indexOf("-export") + 1;
                        flashCard.exportToFile(argsList.get(pathIndex));
                    }
                    break;
                }
                case "log": {
                    flashCard.log();
                    break;
                }
                case "hardest card": {
                    flashCard.hardestCard();
                    break;
                }
                case "reset stats": {
                    flashCard.resetStats();
                    break;
                }

            }
        }

    }

}