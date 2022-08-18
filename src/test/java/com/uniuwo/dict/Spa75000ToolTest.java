package com.uniuwo.dict;

import com.uniuwo.dict.model.DictEntry;
import com.uniuwo.dict.util.LatinUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Spa75000ToolTest {

//    @Test
    void processTest() throws Exception {
        Path src = Paths.get("F:\\0Dict\\Spanish\\English - Spanish dictionary Diccionario ingles espanol (75.000 entries)_spa_eng_todo.txt");
        Path output = Paths.get("output").resolve("spa_75000_todo.txt");

//        <div class="word"><span style="color:#0080c0;"><strong>valdepeñas: </strong></span>Valdepeñas wine</div>
//                == <span style="color:#0080c0;">valdré: </span>valer ==

        // 1.
        int count = 0;
        String mark = "== <span style=\"color:#0080c0;\">";
        List<String> lines = Files.readAllLines(src);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if(!line.startsWith(mark)) continue;

            int pos1 = mark.length();
            int pos2 = line.indexOf("</span>", pos1);
            String word = line.substring(pos1, pos2);
            String content = line.substring(pos2 + "</span>".length());
            if(content.endsWith("==")){
                content = content.substring(0, content.length() - 2);
            }

            String lineNew = "<div class=\"word\"><span style=\"color:#0080c0;\"><strong>" + word + "</strong></span>" + content + "</div>";

            lines.set(i, lineNew);
            count++;
        }

        if(count > 0){
            Files.write(output, lines);
            System.out.println("Processed lines " + count);
        }

    }

    @Test
    void processValidateTest() throws Exception {
        Path src = Paths.get("output").resolve("spa_75000_todo.txt");

//        <div class="word"><span class="lema"><strong>aburrido: </strong></span>boring; bored; fed up; bore</div>

        // 2.
        int count = 0;
        String mark = "<div class=\"word\"><span class=\"lema\"><strong>";
        List<String> lines = Files.readAllLines(src);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!line.startsWith(mark)) continue;

            int pos1 = mark.length();
            int pos2 = line.indexOf("</strong></span>", pos1);
            if(pos1 > pos2){
                System.err.println("lem end not found at line " + (i+1));
                continue;
            }

            String word = line.substring(pos1, pos2);
            String content = line.substring(pos2 + "</strong></span>".length());
            if (content.endsWith("</div>")) {
                content = content.substring(0, content.length() - "</div>".length());
            }

            word = word.trim();
            if (word.endsWith(",")) {
                System.err.println(" comma(,) found at line " + (i + 1));
                count++;
            }
        }


        System.out.println("Processed lines " + count);

    }

//    @Test
    void initDbTest() throws Exception {
        Path dbPath = Paths.get("output").resolve("spa_75000_test_init.db");
        DictDbPopulate.InitDb(dbPath);
    }

//    @Test
    void initDbIndexTest() throws Exception {
        Path dbPath = Paths.get("output").resolve("spa_75000_test_init.db");
        DictDbPopulate.InitDbIndex(dbPath, true);
    }

//    @Test
    void processDbTest() throws Exception {
        Path src = Paths.get("output").resolve("spa_75000_todo.txt");

        Path dbPath = Paths.get("output").resolve("spa_75000_test_init.db");

//        <div class="word"><span class="lema"><strong>aburrido: </strong></span>boring; bored; fed up; bore</div>

        // 3.
        List<DictEntry> result = new ArrayList<>();

        String mark = "<div class=\"word\"><span class=\"lema\"><strong>";
        List<String> lines = Files.readAllLines(src);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!line.startsWith(mark)) continue;

            int pos1 = mark.length();
            int pos2 = line.indexOf("</strong></span>", pos1);
            if(pos1 > pos2){
                System.err.println("lem end not found at line " + (i+1));
                continue;
            }

            String word = line.substring(pos1, pos2);
            String content = line.substring(pos2 + "</strong></span>".length());
            if (content.endsWith("</div>")) {
                content = content.substring(0, content.length() - "</div>".length());
            }

            word = word.trim();
            if(word.endsWith(":")){
                word = word.substring(0, word.length() - 1).trim();
            }

            String word1="";
            String word2="";
            if (word.contains(",")) {
                String[] splits = word.replace(" ", "").split(",");
                if(splits.length >= 2){
                    // if no need to concatenate suffix:
                    word1 = LatinUtil.removeAccents(splits[0].toLowerCase());
                    word2 = LatinUtil.removeAccents(splits[1].toLowerCase());
                }
                if(splits.length > 2){
                    System.err.println(word + " at line " + (i+1));
                }
            } else {
                word1 = LatinUtil.removeAccents(word.toLowerCase());
            }

            result.add(new DictEntry(word, content, word1, word2));
        }

        // save to db
        DictDbPopulate.SaveDb(dbPath, result);
        System.out.println("Processed lines " + result.size());

    }
}