package com.mytutor.demo.HelperClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

/**
 * Manages files, reading, downloading or saving them to the Server storage for this application.
 * Includes methods for manipulating strings to list (virsa-versa)
 */
@Controller
@RequestMapping("/user/file")
public class FileController {
    /**
     * Writes a line to the file on the system storage
     * @param line: line that is added to the file
     * @param fileDir: directory to which the file is stored or to be stored after writing the line
     * @param filename: name of the file to be write the line to
     * @param append: boolean condition to state whether it should add line to end of the file or erase existing content
     * @return the directory or path to which the file is saved on the system storage
     */
    public static String writeLine(String line, String fileDir, String filename, boolean append) {
        String savedToPath = null;
        try {
            String filePath = fileDir + filename;
            FileWriter filewWriter = new FileWriter(filePath, append);
            filewWriter.write(line + "\n");

            filewWriter.close();
            savedToPath = filePath;
        } catch (FileNotFoundException e) {
            System.out.println("The file '" + filename + "' does not exist");

        } catch (Exception e) {
            System.err.println("Error occured while saving file");
        }
        return savedToPath;
    }

    /**
     * Writes list of lines to the file and save to system storage
     * @param lstLines: list of lines to be written on to the file
     * @param fileDir: directory to which the file should be stored or is stored
     * @param filename: name of the file to write lines to
     * @param append: boolean condition to append the file if exists or erase content first
     * @return path to where the file is saved on the system
     */
    public static String writeFileLines(List<String> lstLines, String fileDir, String filename, boolean append) {
        String savedToPath = null;
        try {
            String filePath = fileDir + filename;
            FileWriter filewWriter = new FileWriter(filePath, append);
            for (String line : lstLines) {
                filewWriter.write(line + "\n");
            }

            filewWriter.close();
            savedToPath = filePath;
        } catch (Exception e) {
            System.err.println("Error occured while saving file");
        }
        return savedToPath;

    }

    /**
     * Reads the lines of the file into a list of strings with those lines
     * @param fileDir: directory of where the file is stored
     * @param filename: name of the file to read lines of
     * @return list of lines(String) that were read from the file
     */
    public static List<String> readFile(String fileDir, String filename) {
        List<String> lstContent = new ArrayList<String>();

        String filepath = fileDir + filename;
        Scanner fScanner = null;
        try {
            File file = new File(filepath);
            fScanner = new Scanner(file);
            while (fScanner.hasNextLine()) {
                String line = fScanner.nextLine();
                line = line.strip();
                lstContent.add(line);
            }
            fScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file '" + filename + "' does not exist");

        } catch (Exception e) {
            System.err.println(e);
        }

        return lstContent;
    }

    /**
     * Writes contents of a MultipartFile to a file that is to be stored on the system 
     * @param file: MultipartFile to read from
     * @param storagePath: storage path or directory for where the file will be stored on the system
     * @return true if file was successful read and saved, else false
     */
    public static boolean writeFile(MultipartFile file, String storagePath) {

        if (!file.isEmpty()) {
            // Get the filename from the uploaded file
            String fileName = file.getOriginalFilename();

            // Create the target folder if it doesn't exist
            File folder = new File(storagePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Construct the path to save the file
            String filePath = storagePath + "/" + fileName;

            // Save the file to the specified path
            try (InputStream inputStream = file.getInputStream();
                    OutputStream outputStream = new FileOutputStream(new File(filePath))) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IllegalStateException e) {
                System.err.println(e);
                return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }

            // File uploaded successfully
            return true;
        } else {
            // Handle the case where no file was selected for upload
            return false;
        }
    }

    /**
     * Reads the lines of a MultipartFile to a list of string objects with those lines
     * @param file: MultipartFile to read lines from
     * @return list of string objects with the file lines
     */
    public static List<String> readFile(MultipartFile file) {
        List<String> lstContent = null;/// = new ArrayList<>();
        try {
            // Check if the uploaded file is not empty
            lstContent = new ArrayList<>();
            if (!file.isEmpty()) {
                // Create a BufferedReader to read the lines
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.strip();
                    lstContent.add(line);
                }

                reader.close();
                // errorMessage = "Successfully added all tutors";
            }
        } catch (IOException e) {
            // errorMessage = "Error reading the file: " + e.getMessage();
            System.err.println(e);
        }
        return lstContent;
    }

    
    /**
     * Get statuses list from a saved file in the system with the statuses
     * 
     * @return list of statuses
     */
    public static List<String> getStatuses() {
        String filename = "ApplicationStatuses.txt";
        String fileDir = "src/main/resources/static/textfiles/";
        List<String> statuses = readFile(fileDir, filename);

        return statuses;
    }

    /**
     * Adds a new status to list of status file saved in the system
     * 
     * @param status: the status to add
     */
    public static void addStatus(String status) {
        String filename = "ApplicationStatuses.txt";
        String fileDir = "src/main/resources/static/textfiles/";

        writeLine(status, fileDir, filename, true);
    }

    /**
     * Converts a list of string objects to a string where of those items as quoted strings separated by a comma
     * @param list: list of string objects to convert
     * @return String with the items in string quotes and separated by a comma
     */
    public static String listInString(List<String> list) {
        String lstString = "";

        int lstSize = list.size();
        for (int i = 0; i < lstSize - 1; i++) {
            lstString += "'" + list.get(i) + "'" + ",";
        }
        lstString += "'" + list.get(lstSize - 1) + "'";
        return lstString;
    }

    /**
     * Converts a String to a list a list of string objects from after spliting the substrings separated by comma
     * @param lstString: String object in format "'element1', 'element2', 'element3',..." each element are quoted
     * @return list of string objects of the substring elements without the quotations
     */
    public static List<String> stringInList(String lstString) {
        List<String> list = new ArrayList<String>();
        String[] arrList = lstString.split(",");

        for (String s: arrList){
            list.add(s.substring(1, s.length()-1));
        }
        return list;
    }

    /**
     * Converts a list of string objects to a list in String of the elements with new line feed to add element of each on a different line
     * @param list: list of string objects
     * @return string objects as paragraph of the extracted elements
     */
    public static String listToString(List<String> list) {
        String lstString = "";

        int lstSize = list.size();

        if (lstSize == 0) {
            return lstString;
        }

        for (int i = 0; i < lstSize - 1; i++) {
            lstString += list.get(i) + "\n";
        }
        lstString += list.get(lstSize - 1);
        return lstString;
    }

    /**
     * Deletes a file or document from the system storage
     * @param fileDir: directory of where the file is located on the system
     * @param filename: name of the file or document
     * @return true if the file was found and deleted, false if it wasn't
     */
    public static boolean deleteFile(String fileDir, String filename){
        boolean isDeleted = true;
        String filepath = fileDir + filename;

        File fileToDelete = new File(filepath);

        if (fileToDelete.delete()) {
            isDeleted = true;
            System.out.println("File deleted successfully.");
        } else {
            System.err.println("Failed to delete the file.");
            isDeleted = false;
        }

        return isDeleted;
    }

    /**
     * Gets mapping to open or download an applicant document saved to docs storage in the system
     * @param fileDir: sub-directory or type of the document for which the file is saved based on
     * @param filename: name of the file or applicant document
     * @return the resource of document requested as an a downloadable attachment
     * @throws Exception
     */
    @GetMapping("/download/{filedir}/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("filedir") String fileDir,
            @PathVariable("filename") String filename) throws Exception {
        String filePath = "src/main/resources/static/docs/" + fileDir + "/" + filename;
        Path path;
        Resource resource;

        File file = new File(filePath);
        if (file.exists()) {
            path = Paths.get(filePath);
            resource = new UrlResource(path.toUri());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Allows creation and sending of downloadable attachments of the file from system storage to the user
     * @param httpSession: user session with the fileDir and filename attributes
     * @return a downloadable attachment of the file requested
     * @throws Exception
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(HttpSession httpSession) throws Exception {
        String fileDir = (String) httpSession.getAttribute("fileDir");
        String filename = (String) httpSession.getAttribute("filename");

        String filePath = fileDir + filename;
        Path path = Paths.get(filePath);
        Resource resource = new UrlResource(path.toUri());

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
