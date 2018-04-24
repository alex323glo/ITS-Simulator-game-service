package org.alex323glo.its_simulator.model.game;

/**
 * Space ship's status model.
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see SpaceShip
 */
public enum SpaceShipStatus {

    FREE,
    BUSY,
    INACTIVE;

    /**
     * Returns next (by business logic) state of space ship.
     *
     * Possible logical moves' chains:
     *      FREE -> BUSY
     *      BUSY -> FREE
     *      INACTIVE -> INACTIVE
     *
     * @return next status in logical chain.
     */
    public SpaceShipStatus getNextStatus() {
        switch (this) {
            case FREE:
                return BUSY;
            case BUSY:
                return FREE;

            default:
                return this;
        }
    }

}
