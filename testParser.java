import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class testParser {
    public static void main(String[] args) {
        // file should be read through passed argument
        if(args.length != 1){
            System.err.println("Need to have 1 argument of test text file name");
            return;
        }
        //use passed argument to read file
        File inputFile = new File(args[0]);
        StringBuilder sourceString = new StringBuilder();

        try (Scanner fileScanner = new Scanner(inputFile)) {
            while (fileScanner.hasNextLine()) {
                //read source code from passed file
                sourceString.append(fileScanner.nextLine() + "\n");
            }

            // create lexicalAnalyzer object using the String built by scanner.
            LexicalAnalyzer analyzer = new LexicalAnalyzer(sourceString.toString());

            // create the parser, passing in the analyzer
            Parser parser = new Parser(analyzer);

            // start parsing and return the output of the evaluation.
            System.out.println("Starting Parse using grammar rules...");
            parser.parse().execute();
            System.out.print("Done!");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + args[0]);
        } catch (Exception e) {
            // this will catch syntax errors thrown by the Parser
            System.err.println( e.getMessage());
        }
    }
}