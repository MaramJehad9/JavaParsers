package org.example.Training;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.TokenRange;
import com.github.javaparser.utils.SourceRoot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

public class JavaTokenization {
    public static void main_(String[] args) throws IOException {
        // Set up JavaParser with the appropriate configuration
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        JavaParser javaParser = new JavaParser(parserConfiguration);

        // Parse the Java source code files
        SourceRoot sourceRoot = new SourceRoot(Paths.get("C:\\Users\\MaramJehad\\Downloads\\PhD\\jhotdraw-develop\\jhotdraw-develop\\jhotdraw-app\\src\\main\\java\\org\\jhotdraw\\app\\AbstractView.java"));
        sourceRoot.tryToParse();

        // Tokenize the code and extract tokens
        sourceRoot.getCompilationUnits().forEach(compilationUnit -> {
            TokenRange tokenRange = null;
            try {
                tokenRange = compilationUnit.getTokenRange().orElseThrow(() -> new FileNotFoundException("Failed to parse the Java source file."));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            tokenRange.forEach(token -> {
                System.out.println("Token: " + token.getText());
            });

        });
        AstAnalysis ast = new AstAnalysis();
    }
}
