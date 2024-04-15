package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.mytutor.demo.HelperClasses.SpringSessionController;

import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.DatabaseControllers.DatabaseDeleteController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;
import com.mytutor.demo.HelperClasses.FileController;
import com.mytutor.demo.object_files.Course;

import jakarta.servlet.http.HttpSession;

/**
 * Manages Creating, Editing, Viewing and Deleting courses
 */
@Controller
@RequestMapping("/admin")
public class ManageCoursesController {
    @Autowired
    private DatabaseCreateController dbCreateController;
    @Autowired
    private DatabaseUpdateController dbUpdateController;
    @Autowired
    private DatabaseQueryController dbQueryController;
    @Autowired
    private DatabaseDeleteController dbDeleteController;

    private final HttpSession httpSession;

    public ManageCoursesController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Models the attributes or items to be passed to ManageCourse View or template,
     * checks the saved attributes in the session to add additional attributes for
     * specific
     * values
     * 
     * @param model:       adds the attributes to be passed to the template
     * @param httpSession: holds the current course on the view or section being
     *                     viewed on the template
     * @return redirect to ManageCourses View
     * @throws SQLException
     */
    @GetMapping("/manage/course")
    public String showForm(Model model, HttpSession httpSession) throws SQLException {
        String creatorID = SpringSessionController.getLoggedUser();
        List<String> adminCourseSites = dbQueryController.getCreatorCourses(creatorID);
        model.addAttribute("adminCourseSites", adminCourseSites);

        int courseID = 0;
        Course course = new Course();
        if (httpSession.getAttribute("course") != null) {
            courseID = (int) httpSession.getAttribute("course");
            course = dbQueryController.getCourse(courseID);
            if (course == null) {
                course = new Course();
            }
        }

        model.addAttribute("tutorlist", httpSession.getAttribute("tutorlist"));
        httpSession.removeAttribute("tutorlist");

        model.addAttribute("CourseForm", course);
        return "ManageCourse";
    }

    /**
     * Receives a request to switch to create a new course in the template then
     * assign the handler
     * to show that section in the form
     * 
     * @return redirect to model mapping for the template
     * @throws Exception
     */
    @GetMapping("/requestCreate")
    public String displayCreateForm() throws Exception {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        httpSession.setAttribute("coursehandler", "create");
        return "redirect:/admin/manage/course";
    }

    /**
     * Receives a request to switch to course update or edit in the template then
     * assign the handler
     * to show that section in the form
     * 
     * @return redirect to model mapping for the template
     * @throws Exception
     */
    @GetMapping("/requestUpdate")
    public String displayUpdateForm() throws Exception {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        httpSession.setAttribute("coursehandler", "update");
        return "redirect:/admin/manage/course";
    }

    /**
     * Receives a request to switch to course display or view in the template then
     * assign the handler
     * to show that section in the form
     * 
     * @return redirect to model mapping for the template
     * @throws Exception
     */
    @GetMapping("/requestDisplay")
    public String displayForm() throws Exception {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        httpSession.setAttribute("coursehandler", "view");
        return "redirect:/admin/manage/course";
    }

    /**
     * Receives object for the New Course details and checks if course already
     * exists or not,
     * adds the course if it doesn't exist and set its creator as this logged user
     * (admin)
     * 
     * @param CourseForm: Course object of the new course to be added to the
     *                    database
     * @return redirect to modelling mapping for the ManageCourses View
     * @throws SQLException
     */
    @PostMapping("/add/course")
    public String submitForm(@ModelAttribute("CourseForm") Course CourseForm) throws SQLException {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        Course course = CourseForm;
        String creatorID = SpringSessionController.getLoggedUser();
        course.setCreatorID(creatorID);
        String resultMessage = "";
        if (dbQueryController.courseExists(course.getCourseCode(), course.getCourseYear())) {
            resultMessage = "Course already exists";
        } else {
            int courseID = dbCreateController.addCourse(course);
            String courseName = CourseForm.getCourseCode() + "," + CourseForm.getCourseYear();
            resultMessage = "Course has been created Successfully";

            httpSession.setAttribute("managingcourse", courseName);
            httpSession.setAttribute("course", courseID);
            httpSession.setAttribute("coursehandler", "update");
        }
        httpSession.setAttribute("resultMessage", resultMessage);

        return "redirect:/admin/manage/course";
    }

    /**
     * Gets the courseID of the selected or entered course to view or edit
     * 
     * @param selectedSite: entered course to be edited or viewed
     * @param httpSession:  used to store the courseID and course name of the
     *                      selected site
     * @return redirect to modelling mapping for the ManageCourses View
     */
    @PostMapping("/selectCourse")
    public String processForm(@RequestParam("selectedSite") String selectedSite, HttpSession httpSession) {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        try {
            String courseName[] = selectedSite.split(",");
            String courseCode = courseName[0];
            int courseYear = Integer.parseInt(courseName[1]);
            int courseID = dbQueryController.getCourseID(courseCode, courseYear);

            if (courseID > 0) {
                httpSession.setAttribute("course", courseID);
                httpSession.setAttribute("managingcourse", selectedSite);
            } else {
                String result = "Could not find the course entered!\nCourse Name format: <course code>','<course year>.";
                httpSession.setAttribute("errorMessage", result);
            }
        } catch (Exception e) {
            String result = "Could not find the course entered!\nCourse Name format: <course code>','<course year>.";
            httpSession.setAttribute("errorMessage", result);
        }

        return "redirect:/admin/manage/course";
    }

    /**
     * Updates the Info of the course with the new provided ones such as Course
     * Description, creator,
     * department but not the course code or year
     * 
     * @param CourseForm: contains the new updated course details
     * @return redirect to modelling mapping for the ManageCourses View
     * @throws SQLException
     */
    @PostMapping("/update/courseInfo")
    public String updateForm(@ModelAttribute("CourseForm") Course CourseForm) throws SQLException {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        int courseID = (int) httpSession.getAttribute("course");
        Course course = dbQueryController.getCourse(courseID);
        course.updateCourse(CourseForm);
        courseID = dbUpdateController.updateCourse(course);

        String resulString = "Course Info has been updated.";
        httpSession.setAttribute("resultMessage", resulString);
        return "redirect:/admin/manage/course";
    }

    /**
     * Adds or updates the Convenor for the course to the course details
     * 
     * @param CourseForm: contains attribute with convenor username
     * @return redirect to modelling mapping for the ManageCourses View
     * @throws SQLException
     */
    @PostMapping("/update/courseConvenor")
    public String processConvenor(@ModelAttribute("CourseForm") Course CourseForm) throws SQLException {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        int courseID = (int) httpSession.getAttribute("course");
        dbUpdateController.updateConvenor(CourseForm.getConvenor(), courseID);
        String resulString = "Convenor has been updated for the course.";
        httpSession.setAttribute("resultMessage", resulString);
        return "redirect:/admin/manage/course";
    }

    /**
     * Adds or update the Teaching Assistant for the course to the course details
     * 
     * @param CourseForm: contains attribute with teaching assistant username
     * @return redirect to modelling mapping for the ManageCourses View
     * @throws SQLException
     */
    @PostMapping("/update/courseTA")
    public String processTeachingAssistant(@ModelAttribute("CourseForm") Course CourseForm) throws SQLException {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        int courseID = (int) httpSession.getAttribute("course");
        dbUpdateController.updateTeachingAssistant(CourseForm.getTeachingAssistant(), courseID);
        String resulString = "TA has been updated for the course.";
        httpSession.setAttribute("resultMessage", resulString);
        return "redirect:/admin/manage/course";
    }

    /**
     * Adds the entered lecturer username from the form to the course details for
     * the course
     * 
     * @param lecturer: username of the lecturer to be added
     * @return redirect to modelling mapping for the ManageCourses View
     * @throws SQLException
     */
    @PostMapping("/add/courseLecturer")
    public String addLecturer(@RequestParam("lecturer") String lecturer) throws SQLException {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;

        int courseID = (int) httpSession.getAttribute("course");
        dbCreateController.addCourseLecturer(courseID, lecturer);

        String resulString = "Lecturer " + lecturer + " has been added.";
        httpSession.setAttribute("resultMessage", resulString);
        return "redirect:/admin/manage/course";
    }

    /**
     * Receives a MultipartFile containing the lists of tutor usernames to be added
     * to the course,
     * opens the file and reads the lines with tutor user names to a List object and
     * then sends them
     * to be displayed on the form for user to confirm before adding them
     * 
     * @param file:        MultipartFile such as textfile with list of tutor
     *                     usernames
     * @param httpSession: used to put the list of tutor usernames to display on the
     *                     View form
     * @return redirect to modelling mapping for the ManageCourses View
     */
    @PostMapping("/listfile")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        List<String> tutorList = FileController.readFile(file);
        String tutorString = "";
        if (tutorList != null) {
            tutorString = FileController.listToString(tutorList);
        } else {
            httpSession.setAttribute("errorMessage", "Couldn't open the file provided");
        }
        httpSession.setAttribute("tutorlist", tutorString);

        return "redirect:/admin/manage/course";
    }

    /**
     * Receives form submission with the list of tutors as a string list to be added
     * to the course,
     * converts them to a List of string objects with usernames as elements of the
     * object
     * then adds those to tutors list to the course and also in the database
     * 
     * @param tutorlist: list of tutor usernames in as String of usernames separated
     *                   by line feed
     * @return redirect to view or modelling mapping for managing course
     * @throws SQLException
     */
    @PostMapping("/add/courseTutors")
    public String processTutor(@RequestParam("tutorlist") String tutorlist) throws SQLException {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        int courseID = (int) httpSession.getAttribute("course");
        Course course = dbQueryController.getCourse(courseID);

        List<String> tutors = Arrays.asList(tutorlist.split("\n"));
        course.setTutors(tutors);
        int numRecords = dbCreateController.addTutorsToCourse(course, courseID);

        String resulString = numRecords + " record(s) have added to database";

        httpSession.setAttribute("resultMessage", resulString);
        httpSession.removeAttribute("tutorlist");
        return "redirect:/admin/manage/course";
    }

    /**
     * Recieves a request to delete the current course being viewed on the Template
     * View,
     * deletes that course and their relevant details including foreign classes,
     * the removes the current viewed details of the course from the session
     * 
     * @return
     * @throws SQLException
     */
    @PostMapping("/delete/course")
    public String deleteCourse() throws SQLException {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;

        String courseName = (String) httpSession.getAttribute("managingcourse");
        dbDeleteController.deleteCourse(courseName);
        String resulString = courseName + " course has been successfully deleted.";
        httpSession.setAttribute("resultMessage", resulString);
        httpSession.removeAttribute("managingcourse");
        httpSession.removeAttribute("course");
        return "redirect:/admin/manage/course";
    }

}