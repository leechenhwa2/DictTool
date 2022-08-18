package com.uniuwo.dict;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookletBuilderTest {

    @Test
    void processTest() throws Exception {
        List<String> dbs = Arrays.asList(Paths.get("output").resolve("spa_75000_test_init.db").toAbsolutePath().toString());

        var wordPath = Paths.get("output").resolve("spa_wordcount_lowercase_selection1_list.txt");
        List<String> words = Files.readAllLines(wordPath);

        try (BookletBuilder builder = new BookletBuilder()) {
            builder.setSqliteDatabases(dbs);
            builder.setContentSeparator(" ⇒ ");

            List<String> result = builder.process(words);
            Files.write(Paths.get("output").resolve("booklet_multi_spa_selection1_list.txt"), result);

        }

        System.out.println("Done.");
    }

    @Test
    void processHtmlTest() throws Exception {
        List<String> dbs = Arrays.asList(Paths.get("output").resolve("spa_75000_test_init.db").toAbsolutePath().toString());

        var wordPath = Paths.get("output").resolve("spa_wordcount_lowercase_selection1_list.txt");
        List<String> words = Files.readAllLines(wordPath);

        try (BookletBuilder builder = new BookletBuilder()) {
            builder.setSqliteDatabases(dbs);
            builder.setContentSeparator(" ⇒ ");

            List<String> result = builder.process(words, true);

            //demo html style
            result.add(0, "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Document</title>\n" +
                    "    <link rel=\"stylesheet\" href=\"dict.css\">\n" +
                    "</head>\n" +
                    "<body>");
            result.add("</body>\n" +
                    "</html> ");

            Files.write(Paths.get("output").resolve("booklet_multi_spa_selection1_list.html"), result);

        }

        System.out.println("Done.");
    }

}