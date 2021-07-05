/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;


/**
 *
 * @author AHMOD
 */
public class TestHibernateMain<E> {
    
    HibernateMain<Object> hibernateMain;
    
    @BeforeAll
    public void beforeTest(){
        hibernateMain = new HibernateMain<>();
    }
    
    @Test
    public void testSelectSession(String selectStatement, Class entityObject){
        selectStatement = "SELECT  * FROM Coursxe";
        entityObject = Student.class;
        List<E> list = new ArrayList<>();
        Deque deque = new ArrayDeque();
        Assertions.assertEquals(deque, hibernateMain.selectSession(selectStatement, entityObject));
        
    }
    
    
}
