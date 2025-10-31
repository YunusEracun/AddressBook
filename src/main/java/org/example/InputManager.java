package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputManager {

    private final Scanner scanner;

    private InputManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public static InputManager getInstance(Scanner scanner) {
        return new InputManager(scanner);
    }

    public int getIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } else {
                System.out.println("Geçersiz giriş. Lütfen sadece bir sayı girin.");
                scanner.nextLine();
                System.out.print(prompt);
            }
        }
    }

    public String getStringInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Bu alan boş bırakılamaz. Lütfen bir değer girin.");
            }
        } while (input.isEmpty());
        return input;
    }
}