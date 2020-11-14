/*
 * Author: Jordan Greenbaum
 * Instructor: Dr. Joseph Helsing
 * Course: CSI 111
 * Assignment: Project 2
 * Date: 30 October 2020
 * Description: A class to simulate a disease outbreak following an SIR model, using cellular automata with a Moore neighborhood configuration.
 */

import java.io.*; // For file I/O
import java.util.Scanner; // For reading files

/**
 * A class to simulate a disease outbreak following an SIR model.
 * Uses cellular automata with a Moore neighborhood configuration.
 * @author Jordan Greenbaum
 */
public class Simulation {
    private int THRESHOLD; // the required number of infectious people in a susceptible person’s neighborhood for the susceptible person to become infectious on the next day
    private int PERIOD; // the infectious period (in days) of the disease
    private Person[][] region; // all of the Person objects, stored in order
    private int day = 0; // the current day
    private int peakDay = 0; // the day during which there was the highest number of infectious Persons
    private int peakCount = 0; // the highest number of infectious Persons in a single day

    /**
     * Constructs a new Simulation object.
     */
    public Simulation() { }

    /**
     * Configures this Simulation based on the provided input file.
     * Then, displays the initial region configuration.
     * @param file the input file
     * @throws IOException if some error occurs while reading the file input
     */
    public void load(File file) throws IOException {
        File inputFile = file; // The input file from which the simulation's configuration is determined
        Scanner input = new Scanner(inputFile); // To gather data from the file

        // Load the threshold and period:
        THRESHOLD = Integer.parseInt(input.nextLine().split(":")[1]); // load the threshold
        PERIOD = Integer.parseInt(input.nextLine().split(":")[1]); // load the infectious period

        String line; // the current line of the file input
        String[] row; // Array of the comma-separated Strings in the line
        int currentRow = 0; // the current region row number
        char state; // the health state of each Person in the region configuration file

        // read and store the region information:
        while (input.hasNextLine()) {
            line = input.nextLine();
            row = line.split(",");

            // initialize the region with the proper size once the dimension has been found:
            if (currentRow == 0)
                region = new Person[row.length][row.length];

            // Store each line:
            for (int i = 0; i < row.length; i++) {
                // Grab the character determining the Person's state
                state = Character.toLowerCase(row[i].charAt(0));

                // Make sure the state is valid:
                switch (state) {
                    case 's': // susceptible
                    case 'i': // infectious
                    case 'r': // recovered
                    case 'v': // vaccinated
                        // if the state is valid, add the Person to the region
                        region[currentRow][i] = new Person(state);
                        break;
                    default:
                        // otherwise, throw an unchecked exception (formatted so that IntelliJ can create a hyperlink to the file in console)
                        throw new IllegalArgumentException("Invalid character '" + state + "' found in input file (" + file.getName() + ":" + (currentRow + 3) + ")");
                }
            }

            // Move to next row in region:
            currentRow++;
        }

        // Print the region on Day 0
        printRegion();
    }

    /**
     * Simulates the disease outbreak.
     * Each day, infectious Persons become recovered if they have been infectious for the entire infectious period.
     * Then, susceptible Persons become infectious if the number of infectious Persons in their neighborhood meets the threshold.
     * The entire region is printed at the end of each day.
     * Information about this Simulation's peak is updated as necessary, and the duration is counted.
     * The method will end when there are no infectious Persons left in the region.
     */
    public void run() {
        int currentInfectious; // The number of infectious Persons on each day of the simulation
        Person[][] nextRegion; // The configuration of region for the next day, so that daily susceptible to infectious changes can be made all at once rather than one Person at a time
        int up_left, up, up_right, // Whether or not each neighbor of the current Person exists and is infectious:
                left, right,                    // 1 if they are infectious
                down_left, down, down_right;    // 0 if they aren't infectious or don't exist

        // run the simulation:
        do {
            currentInfectious = 0; // Reset the infectious count each day
            nextRegion = new Person[region.length][region.length]; // Create a new two-dimensional Array to hold the next day's region
            day++; // Add 1 to the day count

            // Update infectious:
            for (int r = 0; r < region.length; r++) { // rows
                for (int c = 0; c < region[r].length; c++) { // columns
                    if (region[r][c].getState() == 'i') {

                        region[r][c].setInfectiousPeriod(region[r][c].getInfectiousPeriod() + 1); // Add 1 to each person's infectious period

                        // Change the Person to recovered if they were infectious for the entire infection period
                        if (region[r][c].getInfectiousPeriod() >= PERIOD) {
                            region[r][c].setState('r');
                        }
                        // Otherwise, add 1 to the daily infectious count
                        else {
                            currentInfectious++;
                        }
                    }
                }
            }

            // Clone region each day to make new susceptible -> infectious updates independently:
            for (int r = 0; r < region.length; r++) {
                for (int c = 0; c < region[r].length; c++) {
                    nextRegion[r][c] = new Person(region[r][c].getState(), region[r][c].getInfectiousPeriod());
                }
            }

            // Change susceptible to infectious:
            for (int r = 0; r < region.length; r++) { // rows
                for (int c = 0; c < region[r].length; c++) { // columns
                    if (nextRegion[r][c].getState() == 's') {

                        // Count infectious neighbors:
                        up_left = r > 0 && c > 0 && region[r - 1][c - 1].getState() == 'i' ? 1 : 0; // up-left
                        up = r > 0 && region[r - 1][c].getState() == 'i' ? 1 : 0; // up
                        up_right = r > 0 && c < region[r].length - 1 && region[r - 1][c + 1].getState() == 'i' ? 1 : 0; // up-right
                        left = c > 0 && region[r][c - 1].getState() == 'i' ? 1 : 0; // left
                        right = c < region[r].length - 1 && region[r][c + 1].getState() == 'i' ? 1 : 0; // right
                        down_left = r < region.length - 1 && c > 0 && region[r + 1][c - 1].getState() == 'i' ? 1 : 0; // down-left
                        down = r < region.length - 1 && region[r + 1][c].getState() == 'i' ? 1 : 0; // down
                        down_right = r < region.length - 1 && c < region[r].length - 1 && region[r + 1][c + 1].getState() == 'i' ? 1 : 0; // down-right

                        // Change status to infectious if infectious neighbor count meets threshold:
                        if (up_left + up + up_right + left + right + down_left + down + down_right >= THRESHOLD) {
                            nextRegion[r][c].setState('i');
                            currentInfectious++; // Add 1 to the daily infectious count
                        }
                    }
                }
            }

            // Update peak:
            if (currentInfectious > peakCount) {
                peakCount = currentInfectious;
                peakDay = day;
            }

            // Publish daily changes to the region:
            region = nextRegion;

            // Print the region:
            printRegion();
        } while (currentInfectious > 0);

        // Print the simulation summary:
        printSummary();
    }

    /**
     * Prints the entire region of this Simulation, as well as what day the Simulation is currently on.
     */
    private void printRegion() {
        // Print the region:
        System.out.println("Day: " + day);
        for (int r = 0; r < region.length; r++) { // rows
            for (int c = 0; c < region[r].length; c++) { // columns
                System.out.print(region[r][c].getState() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints a summary of this Simulation.
     * Displays the duration of the outbreak,
     * the day on which the number of infectious Persons peaked,
     * and the peak number of infectious Persons.
     */
    private void printSummary() {
        System.out.println("Outbreak Duration: " + day + " days");
        System.out.println("Peak Day: Day " + peakDay);
        System.out.println("Peak Infectious Count: " + peakCount + " people");
    }
}
