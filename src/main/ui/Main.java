package ui;

import javax.swing.SwingUtilities;

// Launches the CourseFlow GUI
public class Main {

    // Effects: Initializes and launches the CourseFlow GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CourseFlowGUI());
    }

}
