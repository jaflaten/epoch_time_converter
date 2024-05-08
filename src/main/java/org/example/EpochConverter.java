package org.example;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EpochConverter {
    public static void main(String[] args) throws IOException {
        String fileName = "src/main/resources/restarts.txt";

        List<Long> epochTimes = readEpochTimesFromFile(fileName);

        // Print epoch times
//        epochTimes.stream().sorted().forEach(System.out::println);


        // Convert epoch times to human-readable format in GMT
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // Set time zone to GMT
        System.out.println("\nHuman Readable Times in GMT:");
        epochTimes.stream().sorted().forEach(epochTime -> System.out.println(sdf.format(new Date(epochTime * 1000))));
        System.out.println("Number of restarts in total since start of monitoring: " + epochTimes.size());

        List<String> csv = new ArrayList<>();
        epochTimes.stream().sorted().forEach(epochTime -> csv.add(sdf.format(new Date(epochTime * 1000))));
        writeToCSV(csv, "restarts_readable.csv");
    }

    public static List<Long> readEpochTimesFromFile(String fileName) {
        List<Long> epochTimes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(",");
                for (String number : numbers) {
                    try {
                        long epochTime = Long.parseLong(number.trim());
                        epochTimes.add(epochTime);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format: " + number);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return epochTimes;
    }

    private static void writeToCSV(List<String> priorityDtos, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Writing header
            writer.write("Restart dates\n");

            // Writing data
            for (String priorityDto : priorityDtos) {
                writer.write(priorityDto + "\n");
            }
        }
    }
}
