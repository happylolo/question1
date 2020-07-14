import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/*
Given a log file for the home page visits of users for a month. Tab separated. Code with input validations and unit tests.
 productId    userId    timestamp (yyyy-mm-dd hh:mm)
 p1           u1         2019-01-02 02:12
 p3           u2         2019-01-02 04:13
 p1           u1         2019-01-02 03:12
 p12          u1         2019-01-02 02:20
 p1           u1         2019-01-02 06:12
 p89          u11        2019-01-03 02:20
 p1           u10        2019-01-03 03:20
 p1           u20        2019-01-03 02:19
 p1           u20        2019-01-03 02:20
 p11          u2         2019-01-05 02:21
 p11          u1         2019-01-06 02:22
 p12          u19        2019-01-08 02:20
1.) For each product how many unique users have visited the product ?
Output
p1:3
p3:1
p12:2 ... and so on
2.) Given a range of timestamp find the number of home page visits in that duration. Input will be in format:
(yyyy-mm-dd hh - yyyy-mm-dd hh)
e.g.
Input
2019-01-01 00 - 2019-01-03 00
Output
5
*/
public class ProblemOne {
    // Use LinkedHashMap to keep the input order
    private static final Map<String, Set<String>> productToUsers = new LinkedHashMap<>();

    // Use TreeMap to have the order of the timestamp, which is more efficient for range query.
    private static final SortedMap<String, Integer> minuteToFreq = new TreeMap<>();

    private static void preprocess(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader("./" + filename))) {
            // skip the first line since it would be the header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] splittedArr = line.split("\\s+");
                if (splittedArr.length != 4) {
                    throw new Exception("Input not valid!");
                }
                String productId = splittedArr[0];
                String userId = splittedArr[1];
                String timestamp = splittedArr[2] + " " + splittedArr[3];
                Set<String> users = productToUsers.getOrDefault(productId, new HashSet<>());
                users.add(userId);
                productToUsers.put(productId, users);
                int freq = minuteToFreq.getOrDefault(timestamp, 0);
                minuteToFreq.put(timestamp, freq + 1);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void printUniqueUserCountForAllProjects() {
        for (Map.Entry<String, Set<String>> entry : productToUsers.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().size());
        }
    }

    private static void printVisitsByTimestampRange(String input) {
        String[] range = input.split(" - ");
        String startTime = range[0] + ":00";
        String endTime = range[1] + ":00";
        int sum = minuteToFreq.subMap(startTime, endTime).values().stream().mapToInt(Integer::intValue).sum();
        System.out.println(sum);
    }

    public static void main(String[] args) {
        String filename = "problem_one.txt";
        preprocess(filename);
        printUniqueUserCountForAllProjects();
        printVisitsByTimestampRange("2019-01-01 00 - 2019-01-03 00");
    }
}
