package org.alex323glo.its_simulator.model.game;

/**
 * Mission's status model.
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see Mission
 */
public enum MissionStatus {

    CREATED,
    STARTED,
    COMPLETED,
    CANCELED;

    /**
     * Returns next (by business logic) state of Mission.
     *
     * Possible logical moves' chains:
     *      CREATED  -> STARTED -> COMPLETED
     *      COMPLETED -> COMPLETED
     *      CANCELED -> CANCELED
     *
     * @return next status in logical chain.
     */
    public MissionStatus getNextStatus() {
        switch (this) {
            case CREATED:
                return STARTED;
            case STARTED:
                return COMPLETED;

            default:
                return this;
        }
    }

}
