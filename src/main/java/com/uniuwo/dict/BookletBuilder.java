package com.uniuwo.dict;

import com.uniuwo.dict.model.DictEntry;
import com.uniuwo.dict.util.LatinUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookletBuilder implements Closeable {

    private List<String> sqliteDatabases = new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();

    /**
     * Separator for concatenate line sections.  Default is empty string.
     */
    private String contentSeparator = "";

    public void setSqliteDatabases(List<String> sqliteDatabases) {
        assert sqliteDatabases.size() > 0;

        if (this.sqliteDatabases.size() > 0 && connections.size() > 0) {
            close();
        }
        this.sqliteDatabases = sqliteDatabases;
        buildConnections();
    }

    public String getContentSeparator() {
        if (contentSeparator == null) {
            contentSeparator = "";
        }
        return contentSeparator;
    }

    public void setContentSeparator(String contentSeparator) {
        this.contentSeparator = contentSeparator;
    }

    public List<String> process(List<String> words){
        return process(words, false);
    }

    public List<String> process(List<String> words, boolean outputHtml) {
        assert words.size() > 0;
        assert connections.size() > 0;

        List<String> result = new ArrayList<>();

        for (String word : words) {
            if (word.isBlank()) {
                result.add(word);
                continue;
            }

            String searchWord = LatinUtil.removeAccents(word);

            List<DictEntry> oneResult = new ArrayList<>();
            //query each connection for each word
            for (Connection connection : connections) {
                try {
                    var entries = findEntryList(connection, searchWord);
                    oneResult.addAll(entries);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (oneResult.size() > 0) {
                if(outputHtml){
                    String line = oneResult.stream()
                            .map(d -> d.toHtmlStringWithId(searchWord))
                            .collect(Collectors.joining(getContentSeparator()));
                    result.add("<div class=\"entry\">" + line + "</div>");
                } else {
                    String line = oneResult.stream()
                            .map(DictEntry::toString)
                            .collect(Collectors.joining(getContentSeparator()));
                    result.add(line);
                }
            } else {
                if(outputHtml){
                    result.add("<div class=\"entry\"><span class=\"word\"><span class=\"lema missing\">" + word + "</span></span></div>");
                } else {
                    result.add(word);
                }
            }
        }

        return result;
    }

    private List<DictEntry> findEntryList(Connection connection, String searchWord) throws SQLException {
        List<DictEntry> result = new ArrayList<>();

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        String sql = String.format("SELECT word, content from Dict where word1='%1$s' or word2='%1$s'", searchWord);
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                // read the result set

                String w = rs.getString(1);
                String c = rs.getString(2);
                if (w != null && !w.isBlank()) {
                    result.add(new DictEntry(w, c));
                }
            }
        }

        return result;
    }

    private void buildConnections() {
        // check if cleared
        assert connections.size() == 0;

        for (String sqliteDatabase : sqliteDatabases) {
            Path dbPath = Paths.get(sqliteDatabase);
            assert Files.exists(dbPath);
            URI dbUri = dbPath.toAbsolutePath().toUri();
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbUri);
                connections.add(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {
        if (connections.size() > 0) {
            for (Connection connection : connections) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

