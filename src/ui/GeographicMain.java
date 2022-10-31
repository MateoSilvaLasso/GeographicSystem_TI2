package ui;

import model.GeographicController;

import java.util.Scanner;

import exception.*;

public class GeographicMain {

    private GeographicController map;

    private Scanner read;

    public GeographicMain() {
        map = new GeographicController();
        read = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GeographicMain op = new GeographicMain();
        int option = 0;
        do {
            option = op.menu();
            op.executeMenu(option);
        } while (option != 0);
    }

    public int menu() {
        System.out.println("************************************\n" + "Welcome to the best Geographic app\n" + "************************************");
        System.out.println("1: INSERT COMMAND\n" +
                "2: IMPORT FROM .SQL FILE\n" +
                "0: EXIT");

        int option = Integer.parseInt(read.nextLine());

        return option;
    }

    public void executeMenu(int option) {
        int option1;
        String ans;
        switch (option) {
            case 1:
                System.out.println("Type the command");
                String command = read.nextLine();
                try {
                    if (command.contains("INSERT")) {
                        if (command.contains("countries")) {
                            map.insertInto(1, command);
                        } else {
                            map.insertInto(2, command);
                        }
                    } else if (command.contains("SELECT * FROM") && !command.contains("ORDER")) {

                        if (command.contains("=")) {
                            map.selectFrom(1, command);
                        } else if (command.contains(">")) {
                            map.selectFrom(2, command);
                        } else if (command.contains("<")) {
                            map.selectFrom(3, command);
                        } else if (!command.contains("WHERE")) {
                            map.selectFrom(4, command);
                        }

                    } else if (command.contains("ORDER")) {
                        if (command.contains(">")) {
                            map.orderBy(1, command);
                        } else if (command.contains("name")) {
                            map.orderBy(2, command);
                        }
                    } else if (command.contains("DELETE")) {
                        if (command.contains("=")) {
                            map.delete(1, command);
                        } else {
                            map.delete(2, command);
                        }
                    } else {
                        throw new InexistentCommandException(command);
                    }
                } catch (InexistentCommandException ex) {
                    ex.printStackTrace();
                }
                break;
            case 2:
                System.out.println("Please type the name of the .txt file (not including the \".txt\"");

                ans = read.nextLine();

                map.importSQL(ans, option);
                map.autoSave();

                break;

            case 3:
                System.out.println("BYE");
                break;
            default:
                System.out.println("invalid option");
                break;
        }
    }
}
