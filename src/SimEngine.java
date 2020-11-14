/*
 * Author: Jordan Greenbaum
 * Instructor: Dr. Joseph Helsing
 * Course: CSI 111
 * Assignment: Project 2
 * Date: 30 October 2020
 * Description: A manager class that gets an input file from the user to configure and run an instance of the Simulation class.
 */

import java.io.*; // for file I/O
import java.util.Scanner; // to take user input

/**
 * A manager class that gets an input file from the user to configure and run an instance of the Simulation class.
 * @author Jordan Greenbaum
 * @see Simulation
 */
public class SimEngine {
    /**
     * Gets an input file from the user, which is used to configure and run an instance of the Simulation class.
     * @param args commandline args, the first of which can be used to provide the input filename
     */
    public static void main(String[] args) {
        File file = args.length > 0 ? new File(args[0]) : new File(""); // The input file provided by the user
        Simulation sim = new Simulation(); // The simulation controlled by this SimEngine
        Scanner keyboard = new Scanner(System.in); // To take user input

        // Reprompt the user for their input file, if necessary
        while (!file.exists()) {
            System.out.println("That file does not exist! Please enter the filename of your input file: ");
            file = new File(keyboard.nextLine());
        }

        // Load the data from the input file into the Simulation
        try {
            sim.load(file);
        }
        catch (IOException e) {
            // End the program if the file was unable to load
            System.out.println("There was an error trying to load your file!");
            return;
        }

        // Run the Simulation
        sim.run();
    }
}
