package com.example.dataparse.assignment.service;

import java.util.*;
import java.util.regex.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlParser {
		

    public static final String CREATE_TABLE_PATTERN = "CREATE TABLE (\\w+) \\((.+)\\)";
    public static final String INSERT_PATTERN = "INSERT INTO (\\w+) VALUES \\((.+)\\)";
    private static final Logger logger = LoggerFactory.getLogger(SqlParser.class);

    public static Map<String, Map<String, String>> parseCreateTable(String sql) {
        Pattern pattern = Pattern.compile("CREATE TABLE (\\w+) \\((.+)\\)");
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            String tableName = matcher.group(1);
            String columnsString = matcher.group(2);

            Map<String, String> tableSchema = new LinkedHashMap<>();
            String[] columns = columnsString.split(",");

            for (String column : columns) {
                String[] parts = column.trim().split("\\s+");
                if (parts.length == 2) {
                    String columnName = parts[0];
                    String columnType = parts[1];
                    tableSchema.put(columnName, columnType);
                }
            }

            Map<String, Map<String, String>> result = new HashMap<>();
            result.put(tableName, tableSchema);

            return result;
        }

        throw new IllegalArgumentException("Invalid CREATE TABLE statement");
    }

    public static Map<String, List<String>> parseInsert(String sql) {
        Pattern pattern = Pattern.compile(INSERT_PATTERN);
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            String tableName = matcher.group(1);
            String[] values = matcher.group(2).split(",");

            Map<String, List<String>> insertData = new HashMap<>();
            insertData.put(tableName, Arrays.asList(values));

            return insertData;
        }

        throw new IllegalArgumentException("Invalid INSERT statement");
    }
}