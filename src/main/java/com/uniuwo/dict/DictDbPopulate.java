package com.uniuwo.dict;

import com.uniuwo.dict.model.DictEntry;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

public class DictDbPopulate {


    public static void SaveDb(Path dbPath, List<DictEntry> result) {

        int processed = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toAbsolutePath().toFile().toURI())) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            connection.setAutoCommit(false);

            for (DictEntry entry : result) {
                insertWord(connection, entry);
                processed++;
            }

            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());

            processed = 0;
        }
        System.out.println("Processed=" + processed);
    }

    private static void insertWord(Connection connection, DictEntry entry) throws SQLException {

        String updateSQL = "insert into Dict(word,content,word1,word2) values(?,?,?,?)";

        PreparedStatement statement = connection.prepareStatement(updateSQL);
        statement.setString(1, entry.getWord());
        statement.setString(2, entry.getContent());
        statement.setString(3, entry.getWord1());
        statement.setString(4, entry.getWord2());

        statement.executeUpdate();
    }


    public static void InitDb(Path dbPath) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toAbsolutePath().toFile().toURI())) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String sql = "CREATE TABLE IF NOT EXISTS Dict (\n" +
                    "\tid INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\tword TEXT,\n" +
                    "\tcontent TEXT,\n" +
                    "\tword1 TEXT, word2 TEXT);";
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Created.");
    }

    public static void InitDbIndex(Path dbPath, boolean extended) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath.toAbsolutePath().toFile().toURI())) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            String sql = "CREATE INDEX IF NOT EXISTS Dict_word_IDX ON Dict (word);";
            statement.execute(sql);

            if(extended){
                sql = "CREATE INDEX IF NOT EXISTS Dict_word1_IDX ON Dict (word1);";
                statement.execute(sql);

                sql = "CREATE INDEX IF NOT EXISTS Dict_word2_IDX ON Dict (word2);";
                statement.execute(sql);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Index Created.");
    }

}
