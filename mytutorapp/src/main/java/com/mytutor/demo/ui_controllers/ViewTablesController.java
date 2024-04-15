package com.mytutor.demo.ui_controllers;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;

/**
 * Allows viewing of database tables with their records on View Tables template
 * as administrator feature *
 */
@Controller
@RequestMapping("/admin")
public class ViewTablesController {

    @Autowired
    private DatabaseQueryController dbQueryController;

    /**
     * Loads the list of Database table names send them to the View and with
     * previously selected table records
     * @param tableName: the table that was selected to view its records
     * @param model: allow adding attributes to send to the View or template 
     * @return the view to ViewTable template with the modelled attributes
     */
    @PostMapping("/get/tableRecords")
    public String getTable(@RequestParam String tableName, Model model) {
        List<List<String>> tableData = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        try {
            tableData = dbQueryController.getTableDataWithColumns(tableName);
            tables = dbQueryController.showTables();

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        model.addAttribute("tableData", tableData);
        model.addAttribute("tables", tables);
        model.addAttribute("showTables", true);
        model.addAttribute("prevSelected", tableName);
        return "ViewTables"; // Thymeleaf template name
    }

    /**
     * Incoming mapping from other screens and loads in the names of the database tables 
     * @param model: adds the list of table names as model attributes and set showing table false in the view
     * @return a view to ViewTables template with the modelled attributes for the template
     */
    @GetMapping("/view/tables")
    public String tables(Model model) {
        List<String> tables = new ArrayList<>();
        try {
            tables = dbQueryController.showTables();
        } catch (Exception e) {
            System.err.println(e);
        }
        model.addAttribute("showTables", false);
        model.addAttribute("tables", tables);
        return "ViewTables";
    }
}
