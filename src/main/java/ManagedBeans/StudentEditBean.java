/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBeans;

import DAO.Course;
import DAO.HibernateMain;
import DAO.Logger;
import DAO.Student;
import SpringIoc.Appconfig;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author AHMOD
 */
@Named
@SessionScoped
public class StudentEditBean implements Serializable {
    private Logger selectedLogger;
     private String name;
    private String course;
    private final AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext(Appconfig.class);
    
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
        System.out.print("autocomplete course name: " + this.getCourse());
        return courseNameList.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }
          //initializating name and course beans to know values to be updated
    public String initNameAndCourse() {
        name = getSelectedLogger().getName().getStudentName();
        course = getSelectedLogger().getCourse().getCourseName();
        return "edit";
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
      
      String initial;
      /**
     * generate initial from student name.
     * @return String
     *         The first letter of each word I.E
     *         returns SAM if name = Samuel Ajayi Mark.
     */
       public String createInitial() {
        System.out.println("START LISTENER");
        if (name != null) {
            StringBuilder stringInitial = new StringBuilder();
            String[] splittedStudentName = name.split("[ ]");
            for (String s : splittedStudentName) {
                stringInitial.append(s.charAt(0));
            }
            initial = stringInitial.toString();
        }
        System.out.println("Initial: " + initial);
        return initial;
    }
      ///edit an existing record in Logger table.
    public void editLogger() {
        System.out.println("Inside edit logger method");
        // object to access all neccessary client request information.
        FacesContext context = FacesContext.getCurrentInstance();
        getSelectedLogger().setName((Student)acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Student WHERE studentName = " + "'" + name +"'", Student.class).get(0));
        getSelectedLogger().setCourse((Course)acac.getBean(HibernateMain.class).selectSession("SELECT * FROM Course WHERE courseName = " + "'" + course +"'", Course.class).get(0));
        getSelectedLogger().setInitial(createInitial());
        System.out.println(getSelectedLogger().getName().getStudentName());
        System.out.println(getSelectedLogger().getCourse().getCourseName());
        System.out.println(name);
        System.out.println(course);
       
     
            boolean isEdit = acac.getBean(HibernateMain.class).updateSession(selectedLogger, Logger.class);
            context.addMessage(null, new FacesMessage(loginMessage(isEdit)));
        

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

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the course
     */
    public String getCourse() {
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(String course) {
        this.course = course;
    }
   
    
}
