import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AccuracyCalculator {

    public static void main(String[] args) {
        Map<Integer, Set<Integer>> relevanceJudgments = loadRelevanceJudgments("src/cranfield/cranqrel.txt");

        Map<Integer, List<Integer>> queryResults = retrieveTop10Results();


        Map<Integer, Double> accuracyMap = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : queryResults.entrySet()) {
            int queryId = entry.getKey();
            List<Integer> retrievedDocs = entry.getValue();
            Set<Integer> relevantDocs = relevanceJudgments.getOrDefault(queryId, new HashSet<>());
            double accuracy = calculateAccuracy(retrievedDocs, relevantDocs);
            accuracyMap.put(queryId, accuracy);
        }

        double totalAccuracy = 0.0;
        for (double accuracy : accuracyMap.values()) {
            totalAccuracy += accuracy;
        }
        double systemAccuracy = totalAccuracy / accuracyMap.size();

        // Print system accuracy
        System.out.println();
        System.out.println("System Accuracy: " + systemAccuracy + "");
        System.out.println();

//        for (Map.Entry<Integer, Double> entry : accuracyMap.entrySet()) {
//            System.out.println("Query " + entry.getKey() + " Accuracy: " + entry.getValue() + "");
//        }
    }

    private static Map<Integer, Set<Integer>> loadRelevanceJudgments(String filePath) {
        Map<Integer, Set<Integer>> relevanceJudgments = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                int queryId = Integer.parseInt(parts[0]);
                int docId = Integer.parseInt(parts[1]);
                relevanceJudgments.computeIfAbsent(queryId, k -> new HashSet<>()).add(docId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relevanceJudgments;
    }

    private static Map<Integer, List<Integer>> retrieveTop10Results() {
        Map<Integer, List<Integer>> queryResults = new HashMap<>();

        queryResults.put(1, Arrays.asList(487, 1269, 14, 52, 793, 15, 185, 13, 330, 173));

        queryResults.put(2, Arrays.asList(13, 793, 173, 15, 747, 1090, 37, 909, 365, 701));

        queryResults.put(3, Arrays.asList(6, 400, 829, 330, 345, 543, 827, 182, 145, 981));

        queryResults.put(4, Arrays.asList(167, 1062, 1190, 186, 1256, 489, 577, 330, 1249, 260));

        queryResults.put(5, Arrays.asList(1033, 626, 104, 829, 1297, 78, 1148, 29, 102, 402));

        queryResults.put(6, Arrays.asList(316, 258, 297, 345, 492, 330, 122, 1365, 1269, 793));

        queryResults.put(7, Arrays.asList(493, 125, 57, 58, 1041, 123, 435, 1232, 1105, 226));

        queryResults.put(8, Arrays.asList(434, 444, 417, 123, 477, 557, 689, 1323, 233, 754));

        queryResults.put(10, Arrays.asList(494, 303, 1011, 782, 1265, 525, 950, 1314, 1287, 1215));

        queryResults.put(11, Arrays.asList(496, 263, 1311, 1148, 111, 73, 26, 1357, 1239, 655));

        queryResults.put(12, Arrays.asList(625, 1233, 793, 544, 705, 811, 418, 918, 454, 799));

        queryResults.put(13, Arrays.asList(497, 521, 904, 1269, 1249, 39, 314, 416, 441, 798));

        queryResults.put(14, Arrays.asList(740, 47, 271, 389, 650, 876, 26, 65, 171, 193));

        queryResults.put(15, Arrays.asList(463, 464, 554, 762, 1066, 1098, 1099, 1118, 1280, 818));

        queryResults.put(18, Arrays.asList(226, 235, 499, 928, 198, 125, 57, 58, 233, 249));

        queryResults.put(57, Arrays.asList(753, 1187, 666, 148, 469, 803, 135, 1272, 308, 95));

        queryResults.put(62, Arrays.asList(275, 124, 976, 1269, 566, 960, 1223, 63, 45, 73));

        queryResults.put(66, Arrays.asList(187, 180, 284, 226, 166, 1353, 816, 1120, 928, 90));

        queryResults.put(69, Arrays.asList(537, 90, 1322, 1258, 18, 1269, 95, 316, 172, 1392));

        queryResults.put(74, Arrays.asList(904, 468, 470, 1249, 573, 504, 194, 917, 1388, 1071));




        return queryResults;
    }


    private static double calculateAccuracy(List<Integer> retrievedDocs, Set<Integer> relevantDocs) {
        int relevantRetrieved = 0;
        for (int docId : retrievedDocs) {
            if (relevantDocs.contains(docId)) {
                relevantRetrieved++;
            }
        }
        return (double) relevantRetrieved / relevantDocs.size() * 1;
    }
}
