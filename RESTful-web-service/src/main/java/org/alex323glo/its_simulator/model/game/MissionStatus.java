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
    READY,
    PERFORMED,
    COMPLETED,
    CANCELED;

    /**
     * Returns next (by business logic) state of Mission.
     *
     * Possible logical moves' chains:
     *      CREATED -> READY -> PERFORMED -> COMPLETED
     *      COMPLETED -> COMPLETED
     *      CANCELED -> CANCELED
     *
     * @return next status in logical chain.
     */
    public MissionStatus getNextStatus() {
        switch (this) {
            case CREATED:
                return READY;
            case READY:
                return PERFORMED;
            case PERFORMED:
                return COMPLETED;

            default:
                return this;
        }
    }

}
