import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * MarksAnalyzer - reads student marks from a CSV file and writes a report.
 * Usage: java -cp build MarksAnalyzer <input.csv> <output.txt>
 * CSV format: name,marks   (first line is a header)
 */
public class MarksAnalyzer {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java MarksAnalyzer <input.csv> <output.txt>");
            System.exit(1);
        }

        List<String> lines = Files.readAllLines(Paths.get(args[0]));
        int count = 0, passed = 0;
        double sum = 0, max = -1, min = 101;
        String topName = "", lowName = "";

        for (int i = 1; i < lines.size(); i++) {          // skip header
            String[] parts = lines.get(i).split(",");
            if (parts.length != 2) continue;
            String name = parts[0].trim();
            double marks = Double.parseDouble(parts[1].trim());
            count++;
            sum += marks;
            if (marks >= 50) passed++;
            if (marks > max) { max = marks; topName = name; }
            if (marks < min) { min = marks; lowName = name; }
        }

        if (count == 0) {
            System.err.println("No data found");
            System.exit(1);
        }

        String report = String.format(Locale.US,
            "Students: %d%nAverage: %.2f%nHighest: %.2f (%s)%nLowest: %.2f (%s)%nPassed: %d%nFailed: %d%n",
            count, sum / count, max, topName, min, lowName, passed, count - passed);

        Path out = Paths.get(args[1]);
        if (out.getParent() != null) Files.createDirectories(out.getParent());
        Files.writeString(out, report);
        System.out.println("Report written to " + args[1]);
    }
}
