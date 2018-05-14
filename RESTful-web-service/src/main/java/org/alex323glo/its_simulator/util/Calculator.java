package org.alex323glo.its_simulator.util;

/**
 * TODO add doc.
 *
 * @author Alexey_O
 * @version 0.1
 */
public class Calculator {

    /**
     * TODO add doc.
     *
     * @param exp
     * @return
     */
    public static int calculateUserLevel(Long exp) {
        long cutExperience = (exp - (exp % 1000)) / 1000;

        if (cutExperience < 1) {
            return 0;
        }

        int buf = 1;
        int counter = 1;

        while (buf < cutExperience) {
            buf *= 2;
            counter++;
        }

        return counter;
    }

}
