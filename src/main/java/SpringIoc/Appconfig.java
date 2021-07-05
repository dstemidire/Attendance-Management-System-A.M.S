/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpringIoc;

import DAO.Course;
import DAO.HibernateMain;
import DAO.Logger;
import DAO.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *This class is being use to achieve IOC(invertion of control).
 * All object are already created.
 * @author AHMOD
 */
@Configuration
public class Appconfig {
    
    @Bean
    public Course getCourseBean(){
      return  new Course();
    }
    
     @Bean
    public Student getStudentBean(){
      return  new Student();
    }
    
    @Bean
    public Logger getLogBean(){
      return  new Logger();
    }
    
     @Bean
    public HibernateMain getHibernateMainBean(){
      return  new HibernateMain();
    }
    
}
