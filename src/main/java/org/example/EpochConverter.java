package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EpochConverter {
    public static void main(String[] args) {
        String fileName = "src/main/resources/restarts.txt";

        List<Long> epochTimes = readEpochTimesFromFile(fileName);

        // Print epoch times
//        epochTimes.stream().sorted().forEach(System.out::println);


        // Convert epoch times to human-readable format in GMT
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // Set time zone to GMT
        System.out.println("\nHuman Readable Times in GMT:");
        epochTimes.stream().sorted().forEach(epochTime -> System.out.println(sdf.format(new Date(epochTime * 1000))));
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
}
