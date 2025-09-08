package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Course;
import model.TimeBlock;
import model.TimeTable;
import persistence.JsonReader;
import persistence.JsonWriter;

// console-based application that allows users to manage their course schedule,
// including adding, editing, viewing, and removing courses.
public class CourseFlowApp {
    private Scanner scanner;
    private TimeTable timeTable;

    private static final String JSON_STORE = "./data/timetable.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: construct a CourseFlowApp with a new Scanner for user input
    // and an empty TimeTable, then starts the application loop.
    public CourseFlowApp() {
        scanner = new Scanner(System.in);
        timeTable = new TimeTable();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        // runCourseFlow();
    }

    // MODIFIES: this
    // EFFECTS: run the menu and executes the action based on user input.
    @SuppressWarnings("methodlength")
    private void runCourseFlow() {
        boolean keepGoing = true;
        while (keepGoing) {
            menu();
            String command = scanner.nextLine().toLowerCase();
            switch (command) {
                case "1":
                    viewTimeTable();
                    break;
                case "2":
                    addCourse();
                    break;
                case "3":
                    editCourse();
                    break;
                case "4":
                    removeCourse();
                    break;
                case "5":
                    viewCourseDetails();
                    break;
                case "6":
                    editCourseDetails();
                    break;
                case "7":
                    saveTimeTable();
                    break;
                case "8":
                    loadTimeTable();
                    break;
                case "q":
                    keepGoing = false;
            }
        }
        System.out.println("\nGoodbye!");
    }

    // EFFECTS: Displays the main menu options
    private void menu() {
        System.out.println("\n•──────⋅☾ CourseFlow Menu ☽⋅──────•");
        System.out.println("\n1. View Time-Table");
        System.out.println("2. Add a Course");
        System.out.println("3. Edit a Course");
        System.out.println("4. Remove a Course");
        System.out.println("5. View Course Details");
        System.out.println("6. Edit Course Detail");
        System.out.println("7. Save TimeTable");
        System.out.println("8. Load TimeTable");
        System.out.println("Q. Quit");
        System.out.println("Enter your selection: \n");
    }

    // EFFECTS: Displays the timetable, showing all courses scheduled for each week
    @SuppressWarnings("methodlength")
    private void viewTimeTable() {
        List<String> days = new ArrayList<>(List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"));
        System.out.println("\n•──────⋅☾ CourseFlow ☽⋅──────•\n");

        for (String day : days) {
            System.out.println(" " + day + ": ");
            boolean hasCourse = false;

            for (Course c : timeTable.getAllCourses()) {
                for (TimeBlock t : c.getLectureTimes()) {
                    if (t.getDay().equals(day)) {
                        System.out.println("  [" + c.getCourseCode() + "] " + t.getStartTime()
                                + "-" + t.getEndTime() + "_Lecture \n");
                        hasCourse = true;
                    }
                }

                if (c.getLabTime() != null && c.getLabTime().getDay().equals(day)) {
                    System.out.println("  [" + c.getCourseCode() + "] " + c.getLabTime().getStartTime() + "-"
                            + c.getLabTime().getEndTime() + "_Lab \n");
                    hasCourse = true;
                }

                if (c.getDiscussionTime() != null && c.getDiscussionTime().getDay().equals(day)) {
                    System.out.println("  [" + c.getCourseCode() + "] " + c.getDiscussionTime().getStartTime() + "-"
                            + c.getDiscussionTime().getEndTime() + "_Discussion \n");
                    hasCourse = true;
                }
            }

            if (!hasCourse) {
                System.out.println("  No courses ㋡\n");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to enter a new course, including lecture times
    // and optional components (Lecture/Lab/Discussion), then adds it to the
    // timetable.
    // if cousse is conflict with existing courses, print "conflict" messege
    private void addCourse() {
        System.out.print("\nEnter Course Code: ");
        Course course = new Course(scanner.nextLine());

        addLectures(course);
        addComponent(course, "Lab");
        addComponent(course, "Discussion");

        // timeTable.addCourse(course);
        // System.out.println("\nCourse Added!");
        if (timeTable.addCourse(course)) {
            System.out.println("\nCourse Added!");
        } else {
            System.out.println("\nCourse not added due to time conflict.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to enter lecture details (day, start time, end
    // time)
    // and adds them to the course(make new timeblock)
    private void addLectures(Course course) {
        System.out.print("Enter number of Lectures: ");
        int numLectures = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numLectures; i++) {
            System.out.print("Enter Lecture Day: ");
            String day = scanner.nextLine().toUpperCase();
            System.out.print("Enter Start Time (HH:mm): ");
            LocalTime startTime = LocalTime.parse(scanner.nextLine());
            System.out.print("Enter End Time (HH:mm): ");
            LocalTime endTime = LocalTime.parse(scanner.nextLine());
            course.addLectureTime(new TimeBlock(day, startTime, endTime));
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to enter component details (day, start time, end
    // time)
    // and adds them to the course(make new timeblock)
    private void addComponent(Course course, String type) {
        System.out.print("Do you want to add a " + type + "? (yes/no): ");
        if (!scanner.nextLine().equalsIgnoreCase("yes")) {
            return;
        }

        System.out.print("Enter " + type + " Day: ");
        String day = scanner.nextLine().toUpperCase();
        System.out.print("Enter Start Time (HH:mm): ");
        LocalTime startTime = LocalTime.parse(scanner.nextLine());
        System.out.print("Enter End Time (HH:mm): ");
        LocalTime endTime = LocalTime.parse(scanner.nextLine());

        if (type.equals("Lab")) {
            course.setLabTime(new TimeBlock(day, startTime, endTime));
        } else {
            course.setDiscussionTime(new TimeBlock(day, startTime, endTime));
        }
    }

    // MODIFIES: this
    // EFFECTS: User selects a lecture, lab, or discusstion to edit the day or time
    private void editCourse() {
        System.out.println("Enter Course Code to edit: ");
        String courseCode = scanner.nextLine();
        Course targetCourse = findCourse(courseCode);

        System.out.println("Please select one you want to edit");
        System.out.println("1. Edit Lecture time");
        System.out.println("2. Edit Lab time");
        System.out.println("3. Edit Discussion time");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                editLecture(targetCourse);
                break;
            case "2":
                editLab(targetCourse);
                break;
            case "3":
                editDiscussion(targetCourse);
                break;
            default:
                System.out.println("Invalid selection!");
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the selected Lecture time with new day and time.
    private void editLecture(Course course) {
        List<TimeBlock> lectures = course.getLectureTimes();
        if (lectures.isEmpty()) {
            System.out.println("No lectures found for this course.");
            return;
        }

        System.out.println("Select a lecture to edit:");
        for (int i = 0; i < lectures.size(); i++) {
            System.out.println((i + 1) + ". " + lectures.get(i).getDay() + " " + lectures.get(i).getStartTime() + "-"
                    + lectures.get(i).getEndTime());
        }

        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= lectures.size()) {
            System.out.println("Invalid selection!");
            return;
        }

        System.out.print("Enter new Day: ");
        String newDay = scanner.nextLine().toUpperCase();
        System.out.print("Enter new Start Time (HH:mm): ");
        LocalTime newStart = LocalTime.parse(scanner.nextLine());
        System.out.print("Enter new End Time (HH:mm): ");
        LocalTime newEnd = LocalTime.parse(scanner.nextLine());

        lectures.set(choice, new TimeBlock(newDay, newStart, newEnd));
        System.out.println("Lecture time updated!");
    }

    // MODIFIES: this
    // EFFECTS: Updates the Lab time with new day and time.
    private void editLab(Course course) {
        if (course.getLabTime() == null) {
            System.out.println("No Lab for this course.");
            return;
        }

        System.out.print("Enter new Lab Day: ");
        String newDay = scanner.nextLine().toUpperCase();
        System.out.print("Enter new Start Time (HH:mm): ");
        LocalTime newStart = LocalTime.parse(scanner.nextLine());
        System.out.print("Enter new End Time (HH:mm): ");
        LocalTime newEnd = LocalTime.parse(scanner.nextLine());

        course.setLabTime(new TimeBlock(newDay, newStart, newEnd));
        System.out.println("Lab time updated!");
    }

    // MODIFIES: this
    // EFFECTS: Updates the Discussion time with new day and time.
    private void editDiscussion(Course course) {
        if (course.getDiscussionTime() == null) {
            System.out.println("No Discussion for this course.");
            return;
        }

        System.out.print("Enter new Discussion Day: ");
        String newDay = scanner.nextLine().toUpperCase();
        System.out.print("Enter new Start Time (HH:mm): ");
        LocalTime newStart = LocalTime.parse(scanner.nextLine());
        System.out.print("Enter new End Time (HH:mm): ");
        LocalTime newEnd = LocalTime.parse(scanner.nextLine());

        course.setDiscussionTime(new TimeBlock(newDay, newStart, newEnd));
        System.out.println("Discussion time updated!");
    }

    // MODIFIES: this
    // EFFECTS: Removes the course with the given course code from the timetable.
    // If the course is not found, nothing happens.
    private void removeCourse() {
        System.out.println("\nEnter Course Code to remove: ");
        String courseCode = scanner.nextLine();
        Course target = findCourse(courseCode);

        if (target != null) {
            timeTable.removeCourse(target);
            System.out.println("Course removed!");
        } else {
            System.out.println("Course not found!");
        }
    }

    // EFFECTS: Displays details of the course with the given course code.
    // If the course is found, it prints the course's code, name, instructor,
    // credits, and location.
    // otherwise prints "Course not found."
    private void viewCourseDetails() {
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        Course courseToFind = findCourse(courseCode);

        if (courseToFind != null) {
            System.out.println("\n•──────⋅☾ Course Details ☽⋅──────•");
            System.out.println("Course Code: " + courseToFind.getCourseCode());
            System.out.println("Instructor: " + courseToFind.getInstructor());
            System.out.println("Credits: " + courseToFind.getCredits());
            System.out.println("Location: " + courseToFind.getLocation());
        } else {
            System.out.println("Course not found!");
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the details of the course with the given course code.
    // If the course is found, it allows the user to modify the instructor, credits,
    // and location.
    // otherwise "Course not found."
    private void editCourseDetails() {
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        Course c = findCourse(courseCode);

        if (c != null) {
            System.out.print("Enter Instructor: ");
            c.setInstructor(scanner.nextLine());
            System.out.print("Enter Course Credit: ");
            c.setCredits(Integer.parseInt(scanner.nextLine()));
            System.out.print("Enter Course Location: ");
            c.setLocation(scanner.nextLine());
            System.out.println("Course details updated!");
        } else {
            System.out.println("Course not found!");
        }
    }

    // EFFECTS: Finds and returns the course with the given course code.
    // if cant found the course, return null
    private Course findCourse(String courseCode) {
        for (Course c : timeTable.getAllCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(courseCode)) {
                return c;
            }
        }
        return null;
    }

    // EFFECTS: save the current timetable to JSON file.
    // If the file cannot be opened, prints an error message.
    public void saveTimeTable() {
        try {
            jsonWriter.open();
            jsonWriter.write(timeTable);
            jsonWriter.close();
            //System.out.println("TimeTable saved successfully!");
        } catch (FileNotFoundException e) {
            //System.out.println("Unable to save file.");
        }
    }

    // EFFECTS: load the timetable from a saved JSON file.
    // If there is no file, prints an error message.
    public void loadTimeTable() {
        try {
            timeTable = jsonReader.read();
            //System.out.println("TimeTable loaded successfully!");
        } catch (IOException e) {
            //System.out.println("No saved timetable found.");
        }
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }
}
