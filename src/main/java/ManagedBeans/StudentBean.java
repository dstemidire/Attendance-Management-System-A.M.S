package ManagedBeans;

import javax.faces.application.FacesMessage;
import DAO.Logger;
import java.time.LocalDate;
import javax.faces.context.FacesContext;
import DAO.Course;
import java.util.stream.Collectors;
import DAO.Student;
import DAO.HibernateMain;
import java.util.ArrayList;
import java.util.List;
import SpringIoc.Appconfig;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.time.LocalDateTime;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.faces.context.ExternalContext;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * This class represents student managed bean connected to jsf UI.
 *
 * @author AHMOD
 */
@Named("studentB")
@RequestScoped
public class StudentBean implements Serializable {

    // student name
    private String name;
    // student course name
    private String course;
    // student initial
    private String initial;
    // student time in
    private LocalDateTime timeIn;
    // student time out
    private LocalDateTime timeOut;
    // student log status
    private boolean logStatus;
    // object to acheive IOC (invertion of control)
    private final AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext(Appconfig.class);
    StringBuilder stringInitial;
    // loggers query result
    private List<Logger> loggersDetails;
    // use for identifying student signing out.
    private Logger selectedLogger;

    public StudentBean() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return this.course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getInitial() {
        return this.initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public LocalDateTime getTimeIn() {
        return this.timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isLogStatus() {
        return this.logStatus;
    }

    public void setLogStatus(boolean logStatus) {
        this.logStatus = logStatus;
    }

    /**
     * auto complete student name when typing.
     *
     * @param  query
     *         string typed in UI
     * @return generic list object.
     *         The result of data queried based on input string.
     */
    public List<String> autocompleteStudentName(String query) {
        String queryLowerCase = query.toLowerCase();
        List<String> studentNameList = new ArrayList<>();
        List<Student> studentsObjectList = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Student", Student.class);
        studentsObjectList.stream().map(eachStudentObjectList -> {
            studentNameList.add(eachStudentObjectList.getStudentName());
            return eachStudentObjectList;
        }).forEachOrdered(eachStudentObjectList -> System.out.println(eachStudentObjectList.getStudentName()));
        return studentNameList.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }

     /**
     * auto complete student name when typing.
     *
     * @param  query
     *         string typed in UI
     * @return generic list object.
     *         The result of data queried based on input string.
     */
    public List<String> autocompleteCourseName(String query) {
        String queryLowerCase = query.toLowerCase();
        List<String> courseNameList = new ArrayList<>();
        List<Course> courseObjectList = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Course", Course.class);
        courseObjectList.stream().map(eachStudentObjectList -> {
            courseNameList.add(eachStudentObjectList.getCourseName());
            return eachStudentObjectList;
        }).forEachOrdered(eachStudentObjectList -> System.out.println(eachStudentObjectList.getCourseName()));
        System.out.print("autocomplete course name: " + this.course);
        return courseNameList.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }

    /**
     * generate initial from student name.
     * @return String
     *         The first letter of each word I.E
     *         returns SAM if name = Samuel Ajayi Mark.
     */
    public String createInitial() {
        System.out.println("START LISTENER");
        if (name != null) {
            stringInitial = new StringBuilder();
            String[] splittedStudentName = name.split("[ ]");
            for (String s : splittedStudentName) {
                stringInitial.append(s.charAt(0));
            }
            initial = stringInitial.toString();
        }
        System.out.println("Initial: " + initial);
        return initial;
    }

    // sign in proccess for student
    public void logIn() {
        // object to access all neccessary client request information.
        FacesContext context = FacesContext.getCurrentInstance();
        if (name != null && course != null) {
            System.out.print("In if block inside the logIn method");
            List<Student> studentList = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Student where studentName = '" + name + "'", Student.class);
            List<Course> courseList = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Course where courseName = '" + course + "'", Course.class);
            List<Logger> loggerList = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Logger where name_id = " + studentList.get(0).getId() + "AND course_id = " + courseList.get(0).getId() + "AND today = '" + LocalDate.now().toString() + "'", Logger.class);
            if (!loggerList.isEmpty()) {
                System.out.println("inside check loggerlist");
                logStatus = false;
                context.addMessage((String) null, new FacesMessage(loginMessage(logStatus) + " You've signed in already"));
            } else {
                System.out.println("outside check loggerlist");
                Logger loggerObject = acac.getBean(Logger.class);
                loggerObject.setName((Student) studentList.get(0));
                loggerObject.setCourse((Course) courseList.get(0));
                loggerObject.setTimeIn(LocalDateTime.now().toString());
                loggerObject.setInitial(this.createInitial());
                loggerObject.setToday(LocalDate.now().toString());
                setLogStatus(acac.getBean(HibernateMain.class).insertSession(loggerObject, Logger.class));
                context.addMessage(null, new FacesMessage(loginMessage(logStatus)));
            }
        }
    }

    // sign out proccess for student
    public void logOut() {
         // object to access all neccessary client request information.
        FacesContext context = FacesContext.getCurrentInstance();
        if (selectedLogger.getName().getStudentName() != null && selectedLogger.getCourse().getCourseName() != null) {
            System.out.print("In if block inside the logout method");
            List<Student> studentList = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Student where studentName = '" + selectedLogger.getName().getStudentName() + "'", Student.class);
            List<Course> courseList = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Course where courseName = '" + selectedLogger.getCourse().getCourseName() + "'", Course.class);
            List<Logger> loggerList = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Logger where name_id = " + studentList.get(0).getId() + "AND course_id = " + courseList.get(0).getId() + "AND today = '" + LocalDate.now().toString() + "'", Logger.class);
            if (loggerList.get(0).getTimeOut() != null) {
                System.out.println("inside check loggerlist");
                System.out.println("loggerlist: " + loggerList.get(0).getTimeOut());
                logStatus = false;
                context.addMessage(null, new FacesMessage(loginMessage(logStatus) + " You've signed out"));
            } else {
                Logger loggerUpdate = loggerList.get(0);
                loggerUpdate.setTimeOut(LocalDateTime.now().toString());
                setLogStatus(acac.getBean(HibernateMain.class).updateSession(loggerUpdate, Logger.class));
                context.addMessage(null, new FacesMessage(loginMessage(logStatus)));
            }
        }
    }

    /**
     * Login message
     * @param logStatusObject 
     * @return String
     *         login message based on logStatusObject
     */
    public String loginMessage(boolean logStatusObject) {
        if (logStatusObject) {
            return "Successfull";
        }
        return "Unsuccessful";
    }

    /**
     * @return loggersDetails
     *         result for all records in the Logger's table.
     */
    public List<Logger> getLoggersDetails() {
        loggersDetails = acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Logger WHERE today = " + "'" + LocalDate.now() + "'", Logger.class);
        return loggersDetails;

    }

    /**
     * @param loggersDetails the loggersDetails to set
     */
    public void setLoggersDetails(List<Logger> loggersDetails) {
        this.loggersDetails = loggersDetails;
    }

    /**
     * @return the selectedLogger
     */
    public Logger getSelectedLogger() {
        return selectedLogger;
    }

    /**
     * @param selectedLogger the selectedLogger to set
     */
    public void setSelectedLogger(Logger selectedLogger) {
        this.selectedLogger = selectedLogger;
    }
    
    // generate a csv file of all record in the Logger's table.
    public void generateCsv() throws IOException {
        System.out.println("generate csv method");
        Path path = Paths.get("C:\\Users\\AHMOD\\Documents\\Apache Netbean Project\\AMSMOCK\\src\\main\\webapp\\CSV_Folder" + LocalDate.now().toString() + ".csv");
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE);
        try (CSVPrinter csvPrinter = CSVFormat.DEFAULT
                .print(bufferedWriter)) {
            for (Logger loggersDetail : loggersDetails) {
                csvPrinter.printRecord(loggersDetail.getId(), loggersDetail.getName().getStudentName(),
                        loggersDetail.getCourse().getCourseName(), loggersDetail.getInitial(),
                        loggersDetail.getTimeIn(), loggersDetail.getTimeOut());
            }
            // object to access all neccessary client request information.
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext ec= facesContext.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("text/csv");
            ec.setResponseHeader("Content-Disposition", "attachment;filename=CSV_Folder"+LocalDate.now()+".csv");
            OutputStream out = ec.getResponseOutputStream();
            try (InputStream input = Files.newInputStream(path, StandardOpenOption.READ)) {
                int a;
                while ((a = input.read()) != -1) {
                    out.write(a);
                    out.flush();
                }
                facesContext.responseComplete();
            }
        }
    }
}