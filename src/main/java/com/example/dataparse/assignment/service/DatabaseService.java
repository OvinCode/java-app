package com.example.dataparse.assignment.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.*;
import java.util.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class DatabaseService {

    private static final String DATA_DIRECTORY = "data/";
    static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    @Autowired
    private RedisService redisService;


    public void createTable(String sql) throws IOException {
        try {
            Map<String, Map<String, String>> parsedTable = SqlParser.parseCreateTable(sql);
            String tableName = parsedTable.keySet().iterator().next();
            Map<String, String> tableSchema = parsedTable.get(tableName);

            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String schemaFileName = DATA_DIRECTORY + tableName + "_schema.txt";
            String dataFileName = DATA_DIRECTORY + tableName + "_data.txt";

            File schemaFile = new File(schemaFileName);
            schemaFile.createNewFile();

            // Write schema
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(schemaFileName))) {
                for (Map.Entry<String, String> entry : tableSchema.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue());
                    writer.newLine();
                }
            }

            // Create empty data file
            new File(dataFileName).createNewFile();

            logger.info("Table {} created successfully", tableName);
            redisService.incrementSuccessCounter();
        } catch (Exception e) {
            logger.error("Error creating table: {}", e.getMessage(), e);
            redisService.incrementFailureCounter();
            throw e;
        }
    }

    public void insertData(String sql) throws IOException {
    	
    	try {
        Map<String, List<String>> insertData = SqlParser.parseInsert(sql);
        String tableName = insertData.keySet().iterator().next();
        List<String> values = insertData.get(tableName);

        String dataFileName = DATA_DIRECTORY + tableName + "_data.txt";

        // Append data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFileName, true))) {
            writer.write(String.join(",", values));
            writer.newLine();
        	}
        
        	logger.info("Data inserted successfully into table {}", tableName);
        	redisService.incrementSuccessCounter();
        
    		} catch (Exception e) {
    			
    			logger.error("Error inserting data", e);
                redisService.incrementFailureCounter();
                throw e;
            }
        }
    	

    public List<String> getTableSchema(String tableName) throws IOException {
        String schemaFileName = DATA_DIRECTORY + tableName + "_schema.txt";

        logger.info("This is an getTableSchema window message {}", tableName);
        List<String> schema = new ArrayList<>();

        logger.info("Retrieving schema for table: {}", tableName);

        try (BufferedReader reader = new BufferedReader(new FileReader(schemaFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                schema.add(line);
            }
        } catch (FileNotFoundException e) {
            // Log specific error for file not found
            logger.error("Schema file not found for table: {}", tableName, e);
            throw new IOException("Schema file not found for table: " + tableName, e);
        } catch (IOException e) {
            // Log general IO exception
            logger.error("Error reading schema file for table: {}", tableName, e);
            throw e;
        }

        return schema;
    }

    public List<String> getTableData(String tableName) throws IOException {
        String dataFileName = DATA_DIRECTORY + tableName + "_data.txt";
        logger.info("This is an getTableData message");
        List<String> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        }

        return data;
    }
}