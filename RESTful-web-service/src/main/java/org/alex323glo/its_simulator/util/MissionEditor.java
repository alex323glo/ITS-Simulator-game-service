package org.alex323glo.its_simulator.util;

import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.model.game.SpaceShip;

import java.time.LocalTime;

/**
 * Additional util class, which helps to
 * safely modify Mission object data (without change of non-changeable fields)
 *
 * @author Alexey_O
 * @version 0.1
 */
public class MissionEditor {
    
    private Mission tempMission;

    public MissionEditor() {
        tempMission = new Mission();
    }

    MissionEditor spaceShip(SpaceShip spaceShip) {
        tempMission.setSpaceShip(spaceShip);
        return this;
    }

    MissionEditor startingPoint(Planet startingPoint) {
        tempMission.setStartingPoint(startingPoint);
        return this;
    }

    MissionEditor destinationPoint(Planet destinationPoint) {
        tempMission.setStartingPoint(destinationPoint);
        return this;
    }

    MissionEditor payload(Double payload) {
        tempMission.setPayload(payload);
        return this;
    }

    MissionEditor duration(LocalTime duration) {
        tempMission.setDuration(duration);
        return this;
    }

    Mission merge(Mission mission) {
        if (tempMission.getSpaceShip() != null) {
            mission.setSpaceShip(tempMission.getSpaceShip());
        }
        if (tempMission.getStartingPoint() != null) {
            mission.setStartingPoint(tempMission.getStartingPoint());
        }
        if (tempMission.getDestinationPoint() != null) {
            mission.setDestinationPoint(tempMission.getDestinationPoint());
        }
        if (tempMission.getPayload() != null) {
            mission.setPayload(tempMission.getPayload());
        }
        if (tempMission.getDuration() != null) {
            mission.setDuration(tempMission.getDuration());
        }
        return mission;
    }

    public MissionEditor clearEditor() {
        tempMission = new Mission();
        return this;
    }
    
}
