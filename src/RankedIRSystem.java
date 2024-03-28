import java.io.*;
import java.util.*;

public class RankedIRSystem {

    private static Map<String, Integer> documentFrequencies = new HashMap<>();
    private static Map<Integer, Map<String, Double>> documentWeights = new HashMap<>();
    private static List<String> documents = new ArrayList<>();

    public static void main(String[] args) {
        loadDocuments();
        preprocessDocuments();

        retrieveTop10Results();
    }

    private static void loadDocuments() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/cranfield/Cran.all.txt"));
            StringBuilder documentBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(".I")) {
                    documents.add(documentBuilder.toString());
                    documentBuilder.setLength(0);
                }
                documentBuilder.append(line).append("\n");
            }
            documents.add(documentBuilder.toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void preprocessDocuments() {
        for (String document : documents) {
            Set<String> terms = new HashSet<>(Arrays.asList(document.toLowerCase().split("\\s+")));
            for (String term : terms) {
                documentFrequencies.put(term, documentFrequencies.getOrDefault(term, 0) + 1);
            }
        }

        int totalDocuments = documents.size();
        for (int i = 0; i < documents.size(); i++) {
            Map<String, Double> weights = new HashMap<>();
            String document = documents.get(i);
            String[] terms = document.toLowerCase().split("\\s+");
            for (String term : terms) {
                double idf = Math.log((double) totalDocuments / (documentFrequencies.get(term) + 1)) / Math.log(2);
                double tfIdf = 4 * idf; // Using 4-idf weighting
                weights.put(term, tfIdf);
            }
            documentWeights.put(i + 1, weights);
        }
    }


    public static Map<Integer, List<Map.Entry<Integer, Double>>> retrieveTop10Results() {
        Map<Integer, List<Map.Entry<Integer, Double>>> queryResults = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/cranfield/Cran.qry.txt"));
            StringBuilder queryBuilder = new StringBuilder();
            String line;

            int queryId = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(".I")) {
                    if (queryId != 0) {
                        List<Map.Entry<Integer, Double>> results = retrieveQueryResults(queryBuilder.toString());
                        queryResults.put(queryId, results);
                        printQueryResults(queryId, queryBuilder.toString(), results);
                    }
                    queryBuilder.setLength(0);
                    queryId = Integer.parseInt(line.split("\\s+")[1]);
                } else {
                    queryBuilder.append(line).append("\n");
                }
            }

            if (queryId != 0) {
                List<Map.Entry<Integer, Double>> results = retrieveQueryResults(queryBuilder.toString());
                queryResults.put(queryId, results);
                printQueryResults(queryId, queryBuilder.toString(), results);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return queryResults;
    }

    private static void printQueryResults(int queryId, String query, List<Map.Entry<Integer, Double>> results) {
        System.out.println("Query " + queryId + ": " + query.trim());

        System.out.println("Query " + queryId + " Results:");
        for (int i = 0; i < Math.min(10, results.size()); i++) {
            System.out.println("Document ID: " + results.get(i).getKey() + ", Score: " + results.get(i).getValue());
        }
        System.out.println();
    }

    private static List<Map.Entry<Integer, Double>> retrieveQueryResults(String query) {
        List<Map.Entry<Integer, Double>> results = new ArrayList<>();

        for (int i = 0; i < documents.size(); i++) {
            double score = calculateScore(query, documentWeights.get(i + 1));
            results.add(new AbstractMap.SimpleEntry<>(i + 1, score));
        }

        results.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        return results.subList(0, Math.min(10, results.size()));
    }





    private static double calculateScore(String query, Map<String, Double> documentWeights) {
        double score = 0;
        String[] queryTerms = query.split("\\s+");
        for (String term : queryTerms) {
            if (documentWeights.containsKey(term)) {
                score += documentWeights.get(term);
            }
        }
        return score;
    }
}
