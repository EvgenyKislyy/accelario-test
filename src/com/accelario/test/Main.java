package com.accelario.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final String CSV_EXTENSION = ".csv";
    private static final String ROWS_DELIMITER = ",";

    public static void main(String[] args) throws IOException {
        (new Main()).copyCardsData(args);
    }

    private void copyCardsData(String[] args) throws IOException {
        if (args.length < 2) {
            throw new IOException("Not enough folders");
        }
        String sourceFolderPath = args[0];
        String destinationFolderPath = args[1];

        File sourceFolder = new File(sourceFolderPath);
        File destinationFolder = new File(destinationFolderPath);

        if (sourceFolder.exists() && sourceFolder.isDirectory() && sourceFolder.list().length > 0  && destinationFolder.exists()
                && destinationFolder.isDirectory()) {

            System.out.println("Source document folder: " + sourceFolder);
            System.out.println("Destination document folder: " + destinationFolderPath);

            System.out.println("Start working");

            try (Stream<Path> filePathsStream = Files.walk(Paths.get(sourceFolderPath)).collect(Collectors.toList())
                    .parallelStream()) {
                filePathsStream.filter(Files::isRegularFile)
                        .filter(file -> file.getFileName().toString().toLowerCase().endsWith(CSV_EXTENSION))
                        .forEach(file -> copyCreditCardLines(destinationFolderPath, file));
            }
            System.out.println("Work has been finished");
        } else {
            throw new IOException("Something wrong with folders");
        }
    }

    private void copyCreditCardLines(String destinationFolderPath, Path filePath) {
    	System.out.println("Start new");
        try (Stream<String> stream = Files.lines(filePath);
             PrintWriter out = new PrintWriter(destinationFolderPath + filePath.getFileName())) {

            stream.forEach(csvLine -> {
                String[] cells = csvLine.split(ROWS_DELIMITER);
                for (String cell : cells) {
                    if (CreditCardValidator.checkCreditCardNumber(cell)) {
                        out.write(csvLine);
                        out.write(Character.LINE_SEPARATOR);
                        break;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Stop");
    }

}
