# CourseFlow

## A scheduler for flexibly planning and managing university course timetables

**What will the application do?**
CourseFlow helps university students plan and manage their timetables efficiently.
Users can add courses to specific time blocks, group lectures, labs, and discussions
under one course, and rearrange schedules with drag-and-drop.
The app also supports multiple timetable versions and provides detailed course information,
including classrooms, professors, and credits.

**Who will use it?**
University students who need a flexible and intuitive way to organize their schedules,
especially those managing multiple course components.

**Why is this project of interest to me?**
Last semester, as I experienced North American university life for the first time,
I found it interesting that a single course is divided into lecture,
discussion, and lab sessions. However, this structure made it frustrating to create a timetable
using existing applications. Since there was no feature to group these components under a single course,
each time block had to be managed separately, as if they were different courses.
Additionally, I wished for a convenient drag-and-drop feature to easily move course blocks around.
These frustrations led me to come up with the idea for CourseFlow.

A _User Story_ list:

- As a user, I want to be able to add new courses to a specified time block in my titmetable
  and specify the course components(lecture, lab, or discussion...).
- As a user, I want to be able to remove the courses from the titmetable.
- As a user, I want to be able to view the list of courses on the titmetalbe,
  and courses grouped by specific programs.
- As a user, I want to be able to move a course to a different time block in my timetable.
- As a user, I want to be able to view detailed information about a specific course,
  including the classroom, professor, and credit information.  
- As a user, I want to be able to save my time-table to file.  
- As a user, I want to be able to load my time-table from file.  

## Instructions for End User
- You can generate the first required action related to the user story "adding multiple Xs to a Y" by clicking "Add Course", entering courses(lecture, lab, discussion).
- You can generate the second required action related to the user story "deleting Xs from a Y" by clicking "Delete Course", entering the course code.
- You can locate my visual component by viewing the color-coded timetable grid and custom icons in message windows.
- You can save the state of my application by clicking "Save", which stores the timetable to a file.
- You can reload the state of my application by "Load", which restores the last saved timetable.

## Phase 4: Task 2  
Wed Mar 26 02:17:13 PDT 2025  
Added course: CPSC110  
  
Wed Mar 26 02:17:44 PDT 2025  
Added course: MATH111  

Wed Mar 26 02:18:04 PDT 2025   
Removed course: CPSC110  

## Phase 4: Task 3  
I would like to refactor the course structure by making an abstract class called "CourseComponent", with Lecture, Lab, and Discussion as its subclasses. This would allow all components to be stored in a single list, making it easier to manage. And it could reduce duplicated code. For example, instead of checking for time conflicts separately, I could loop through all components in the same way using polymorphism. This design would improve flexibility and maintainability, especially if more component types were added in the future.
