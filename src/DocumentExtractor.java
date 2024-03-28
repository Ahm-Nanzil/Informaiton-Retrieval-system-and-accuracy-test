import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DocumentExtractor {
    public static void main(String[] args) {
        extractDocuments();
    }

    public static void extractDocuments() {
        File directory = new File("src/documents/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            Scanner scanner = new Scanner(new File("src/Cranfield/cran.all.txt"));
            StringBuilder document = new StringBuilder();
            int docId = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith(".I")) {
                    if (docId != 0) {
                        saveDocument(docId, document.toString());
                        document.setLength(0);
                    }
                    docId = Integer.parseInt(line.substring(3).trim());
                } else {
                    document.append(line).append("\n");
                }
            }
            if (docId != 0) {
                saveDocument(docId, document.toString());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveDocument(int docId, String content) {
        try {
            FileWriter writer = new FileWriter("src/documents/" + docId + ".txt");
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
