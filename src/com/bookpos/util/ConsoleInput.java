package com.bookpos.util;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String text(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    public int integer(String label) {
        while (true) {
            try {
                return Integer.parseInt(text(label));
            } catch (NumberFormatException exception) {
                System.out.println("Input harus berupa angka bulat.");
            }
        }
    }

    public BigDecimal decimal(String label) {
        while (true) {
            try {
                return new BigDecimal(text(label));
            } catch (NumberFormatException exception) {
                System.out.println("Input harus berupa angka, contoh: 75000.");
            }
        }
    }
}
