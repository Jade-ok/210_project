package ui;

import model.Course;
import model.TimeBlock;
import model.TimeTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;

// A GUI for managing a course timetable. 
// Users can add, remove, view, save, and load courses in a structured table.
public class CourseFlowGUI extends JFrame {
    private CourseFlowApp courseFlowApp;
    private JTable timetableTable;
    private DefaultTableModel tableModel;
    private static final String[] DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
    private static final String[] TIME_SLOTS = generateTimeSlots(); // 08:00 ~ 19:00

    // MODIFIES: this
    // EFFECTS: Initializes and displays the CourseFlowGUI window.
    //          Sets up the timetable table, buttons, and listeners.  
    //          user can add, view, and delete their course schedule on the time-table
    //          print eventlog when app is closed.
    @SuppressWarnings("methodlength")
    public CourseFlowGUI() {
        courseFlowApp = new CourseFlowApp();

        setTitle("Course Flow Manager");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // title
        JLabel titleLabel = new JLabel("Course TimeTable", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = new String[DAYS.length + 1]; 
        columnNames[0] = "Time";
        System.arraycopy(DAYS, 0, columnNames, 1, DAYS.length);

        tableModel = new DefaultTableModel(columnNames, 0);
        timetableTable = new JTable(tableModel);

        timetableTable.setDefaultRenderer(Object.class, new CustomCellRenderer());

        for (String time : TIME_SLOTS) {
            tableModel.addRow(new Object[] { time, "", "", "", "", "" });
        }

        add(new JScrollPane(timetableTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Course");
        JButton deleteButton = new JButton("Delete Course");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // add button
        addButton.addActionListener(e -> addCourseGUI());
        deleteButton.addActionListener(e -> deleteCourse());

        saveButton.addActionListener(e -> {
            courseFlowApp.saveTimeTable();
            ImageIcon saveIcon = ImageLoader.loadIcon("saveicon.png", 80, 80);
            JOptionPane.showMessageDialog(null, "Timetable saved successfully!", "Save Complete",
                    JOptionPane.INFORMATION_MESSAGE, saveIcon);
        });

        loadButton.addActionListener(e -> {
            courseFlowApp.loadTimeTable();
            updateTimetableTable();
            ImageIcon loadIcon = ImageLoader.loadIcon("loadicon.png", 80, 80);
            JOptionPane.showMessageDialog(null, "Timetable loaded successfully!", "Load Complete",
                    JOptionPane.INFORMATION_MESSAGE, loadIcon);
        });

        updateTimetableTable();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                courseFlowApp.getTimeTable().printEventLog();
            }
        });

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Update timetable to JTable
    private void updateTimetableTable() {
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            for (int j = 1; j <= DAYS.length; j++) {
                tableModel.setValueAt("", i, j);
            }
        }

        TimeTable timeTable = courseFlowApp.getTimeTable();
        for (Course course : timeTable.getAllCourses()) {
            for (TimeBlock block : course.getLectureTimes()) {
                addTimeBlockToTable(block, course.getCourseCode() + " (Lecture)");
            }
            if (course.getLabTime() != null) {
                addTimeBlockToTable(course.getLabTime(), course.getCourseCode() + " (Lab)");
            }
            if (course.getDiscussionTime() != null) {
                addTimeBlockToTable(course.getDiscussionTime(), course.getCourseCode() + " (Discussion)");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds a course's time block to the timetable based on its day and time slot
    private void addTimeBlockToTable(TimeBlock block, String courseInfo) {
        int column = getDayColumn(block.getDay());
        if (column == -1) {
            return;
        }

        int startRow = getTimeRow(block.getStartTime());
        int endRow = getTimeRow(block.getEndTime());

        for (int i = startRow; i < endRow; i++) {
            tableModel.setValueAt(courseInfo, i, column);
        }
    }

    // EFFECTS: Converts a day string into the corresponding column index in the timetable
    private int getDayColumn(String day) {
        for (int i = 0; i < DAYS.length; i++) {
            if (DAYS[i].equalsIgnoreCase(day)) {
                return i + 1;
            }
        }
        return -1;
    }

    // EFFECTS: Converts a time into the corresponding row index in the timetable
    private int getTimeRow(LocalTime time) {
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(time.toString())) {
                return i;
            }
        }
        return TIME_SLOTS.length - 1;
    }

    // EFFECTS: Generates time slots from 08:00 to 19:00 (30minute intervals) and return it in HH:mm format
    private static String[] generateTimeSlots() {
        String[] slots = new String[23]; // 08:00 ~ 19:00 (30min gap)
        LocalTime time = LocalTime.of(8, 0);
        for (int i = 0; i < slots.length; i++) {
            slots[i] = time.toString();
            time = time.plusMinutes(30);
        }
        return slots;
    }

    // MODIFIES: this
    // EFFECTS: Displays a dialog for the user to input a course code and the number of lectures.
    //          Prompts the user to enter lecture times and add lab and discussion times.
    //          Creates a Course object and adds it to the timetable if successful.
    //          Updates the timetable display and shows a success message.
    @SuppressWarnings("methodlength")
    private void addCourseGUI() {
        ImageIcon courseIcon = ImageLoader.loadIcon("studyLamp.png", 80, 80);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField courseCodeField = new JTextField(5);
        JTextField numLecturesField = new JTextField(5);

        courseCodeField.setPreferredSize(new Dimension(120, 15));
        numLecturesField.setPreferredSize(new Dimension(60, 15));

        panel.add(new JLabel("Course Code:"));
        panel.add(courseCodeField);
        panel.add(new JLabel("Number of Lectures:"));
        panel.add(numLecturesField);

        if (JOptionPane.showConfirmDialog(null, panel, "Add Course", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, courseIcon) != JOptionPane.OK_OPTION) {
            return;
        }

        Course newCourse = new Course(courseCodeField.getText().trim());
        int numLectures = Integer.parseInt(numLecturesField.getText().trim());

        for (int i = 0; i < numLectures; i++) {
            newCourse.addLectureTime(getTimeBlockInput("Lecture"));
        }

        if (confirmAction("Add a Lab?", "labicon.png")) {
            newCourse.setLabTime(getTimeBlockInput("Lab"));
        }

        if (confirmAction("Add a Discussion?", "discussionicon.png")) {
            newCourse.setDiscussionTime(getTimeBlockInput("Discussion"));
        }

        if (courseFlowApp.getTimeTable().addCourse(newCourse)) {
            updateTimetableTable();
            JOptionPane.showMessageDialog(null, "Course Added", "Success", JOptionPane.INFORMATION_MESSAGE, courseIcon);
        }
    }

    // EFFECTS: Shows a window where the user enters a day, start time, and end time.
    //          If the input is valid, returns a TimeBlock. If not, shows an error and returns null.
    private TimeBlock getTimeBlockInput(String type) {
        JPanel panel = createTimeBlockPanel(type);
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter " + type + " Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                return parseTimeBlock(panel);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid time format! Use HH:mm.");
            }
        }
        return null;
    }
    
    // EFFECTS: Creates a window with fields for selecting a day and entering a time range.
    private JPanel createTimeBlockPanel(String type) {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel(type + " Day:"));
        panel.add(new JComboBox<>(DAYS));
        panel.add(new JLabel("Start Time (HH:mm):"));
        panel.add(new JTextField());
        panel.add(new JLabel("End Time (HH:mm):"));
        panel.add(new JTextField());
        return panel;
    }  
    
    // EFFECTS: Gets user input from the panel and creates a TimeBlock.
    //          Returns a new TimeBlock if the input is valid.
    //          Shows an error message if the input is invalid.
    private TimeBlock parseTimeBlock(JPanel panel) {
        JComboBox<?> dayBox = (JComboBox<?>) panel.getComponent(1);
        JTextField startTimeField = (JTextField) panel.getComponent(3);
        JTextField endTimeField = (JTextField) panel.getComponent(5);
    
        return new TimeBlock(dayBox.getSelectedItem().toString().toUpperCase(),
                LocalTime.parse(startTimeField.getText()), LocalTime.parse(endTimeField.getText()));
    }
    

    // EFFECTS: Displays a confirmation dialog with a Yes/No option.
    //          Returns true if the user selects "Yes"
    private boolean confirmAction(String message, String iconFile) {
        ImageIcon icon = (iconFile != null) ? ImageLoader.loadIcon(iconFile, 80, 80) : null; 

        int result = JOptionPane.showConfirmDialog(null, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
        return result == JOptionPane.YES_OPTION;
    }

    // MODIFIES: this
    // EFFECTS: Opens a dialog for the user to input a course code to delete.
    //          If the course exists, removes it from the timetable and updates the display.
    //          If the course does not exist, displays an error message.
    private void deleteCourse() {
        ImageIcon deleteIcon = ImageLoader.loadIcon("deleteicon.png", 80, 80); 

        String courseCode = (String) JOptionPane.showInputDialog(
                this, "Enter Course Code to delete:", "Delete Course",
                JOptionPane.QUESTION_MESSAGE, deleteIcon, null, null);

        if (courseCode == null || courseCode.trim().isEmpty()) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete " + courseCode + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, deleteIcon);

        if (confirm == JOptionPane.YES_OPTION) {
            TimeTable timeTable = courseFlowApp.getTimeTable();
            Course courseToRemove = timeTable.getCourseByName(courseCode);

            if (courseToRemove != null) {
                timeTable.removeCourse(courseToRemove); 
                updateTimetableTable(); 
                JOptionPane.showMessageDialog(
                        this, "Course " + courseCode + " deleted", "Done", JOptionPane.INFORMATION_MESSAGE, deleteIcon);
            } else {
                JOptionPane.showMessageDialog(this, "Course not found!", "Error",
                        JOptionPane.ERROR_MESSAGE, deleteIcon);
            }
        }
    }

}
