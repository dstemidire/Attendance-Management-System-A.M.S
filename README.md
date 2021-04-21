# Attendance Management System (A.M.S)

AMS is a web application that manages student attendance.

#### Technologies
+ **Core Technology:** Java
    + Spring
    + JSF
    + Hibernate

#### Requirements (User Stories) 
+ As an AM, I want our students to be able to sign in using the combination of their NAME (first name, last name), COURSE (java, mms, mis, dtp), TIME IN and INITIAL. So as to, have their signing in to the centre record down.
 
+ As an AM, I want our students to be able to sign out using the combination of their TIME OUT and INITIAL. So as to, have their signing out of the centre record down.

+ As an AM, I want a daily generated report of the amount of students that signed in and didn't sign out at the end of the day. So as to, keep track of attendance record.

+ As an AM, I want the daily generated report to be indicated with two colors, and a summary added with them. So as to, get the information fast and easily/conveniently.
            
    + Color 1 - Red. Summary: Some students didn't sign out. Show: Number of student that signed in and numbers of students that didn't sign out.
    + Color 2 - Green. Summary: All students signed out. Show: Number of student that signed in.

+ As an AM, I want the functionality to export the attendance data for the day and daily generated report for the day as a .csv file. So as to, have an external version of the data I could share to someone or refer to on my local machine.

+ As an AM, I want the functionality to search through an attendance record (for a certain day) using the student name. So as to, check student sign in and sign out details when necessary.