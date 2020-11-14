/*
 * Author: Jordan Greenbaum
 * Instructor: Dr. Joseph Helsing
 * Course: CSI 111
 * Assignment: Project 2
 * Date: 30 October 2020
 * Description: A data storage class to be used in simulating a disease outbreak using an SIR model.
 */

/**
 * A data storage class to be used in simulating a disease outbreak using an SIR model.
 */
public class Person {
    /*=======================*
     * VARIABLE DECLARATIONS *
     *=======================*/

    private char state; // This Person's health state (either 's' for susceptible, 'i' for infectious, 'r' for recovered, or 'v' for vaccinated)
    private int infectiousPeriod = 0; // The number of days this person was infectious for

    /*==============*
     * CONSTRUCTORS *
     *==============*/

    /**
     * Constructs a new Person object with the specified health state.
     * @param state the Person's health state (either 's' for susceptible, 'i' for infectious, or 'r' for recovered)
     */
    public Person(char state) {
        this.state = state;
    }

    /**
     * Constructs a new Person object with the specified health state and infectiousPeriod.
     * @param state the Person's health state (either 's' for susceptible, 'i' for infectious, or 'r' for recovered)
     * @param infectiousPeriod the Person's infectious period
     */
    public Person(char state, int infectiousPeriod) {
        this.state = state;
        this.infectiousPeriod = infectiousPeriod;
    }

    /*=========*
     * GETTERS *
     *=========*/

    /**
     * Returns this Person's health state.
     * @return this Person's health state
     */
    public char getState() {
        return state;
    }

    /**
     * Returns this Person's infectious period.
     * @return this Person's infectious period
     */
    public int getInfectiousPeriod() {
        return infectiousPeriod;
    }

    /*=========*
     * SETTERS *
     *=========*/

    /**
     * Sets this Person's health state.
     * @param state the new health state of this Person
     */
    public void setState(char state) {
        this.state = state;
    }

    /**
     * Sets this Person's infectious period.
     * @param infectiousPeriod the new infectious period of this Person.
     */
    public void setInfectiousPeriod(int infectiousPeriod) {
        this.infectiousPeriod = infectiousPeriod;
    }
}
