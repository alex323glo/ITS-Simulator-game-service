package org.alex323glo.its_simulator.model.game;

/**
 * Contract's status model.
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see Contract
 */
public enum ContractStatus {

    CREATED,
    READY,
    PERFORMED,
    COMPLETED,
    CANCELED;

    /**
     * Returns next (by business logic) state of Contract.
     *
     * Possible logical moves' chains:
     *      CREATED -> READY -> PERFORMED -> COMPLETED
     *      COMPLETED -> COMPLETED
     *      CANCELED -> CANCELED
     *
     * @return next status in logical chain.
     */
    public ContractStatus getNextStatus() {
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
