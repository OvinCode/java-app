package com.example.dataparse.assignment.controller;

import com.example.dataparse.assignment.service.DatabaseService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
@Tag(name = "Database Operations", description = "Endpoints for managing the file-based database")
public class DatabaseController {

	@Autowired
    private DatabaseService databaseService;


    @PostMapping("/execute")
    @Operation(summary = "Execute SQL statement", description = "Execute CREATE TABLE or INSERT INTO SQL statements")
    @ApiResponse(responseCode = "200", description = "SQL statement executed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid SQL statement or execution error")
    public ResponseEntity<String> executeSql(
            @Parameter(description = "SQL statement to execute", required = true, example = "CREATE TABLE users (id INTEGER, name STRING)")
            @RequestBody String sql) {
        try {
            if (sql.toUpperCase().startsWith("CREATE TABLE")) {
                databaseService.createTable(sql);
                return ResponseEntity.ok("Table created successfully");
            } else if (sql.toUpperCase().startsWith("INSERT INTO")) {
                databaseService.insertData(sql);
                return ResponseEntity.ok("Data inserted successfully");
            } else {
                return ResponseEntity.badRequest().body("Unsupported SQL statement");
            }
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/schema/{tableName}")
    @Operation(summary = "Get table schema", description = "Retrieve the schema for a specific table")
    @ApiResponse(responseCode = "200", description = "Table schema retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Error retrieving table schema")
    public ResponseEntity<List<String>> getTableSchema(
            @Parameter(description = "Name of the table", required = true)
            @PathVariable String tableName) {
        try {
            List<String> schema = databaseService.getTableSchema(tableName);
            return ResponseEntity.ok(schema);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/data/{tableName}")
    @Operation(summary = "Get table data", description = "Retrieve the data for a specific table")
    @ApiResponse(responseCode = "200", description = "Table data retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Error retrieving table data")
    public ResponseEntity<List<String>> getTableData(
            @Parameter(description = "Name of the table", required = true)
            @PathVariable String tableName) {
        try {
            List<String> data = databaseService.getTableData(tableName);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}