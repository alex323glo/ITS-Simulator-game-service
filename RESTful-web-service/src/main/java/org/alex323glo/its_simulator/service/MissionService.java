package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.model.game.SpaceShip;
import org.alex323glo.its_simulator.util.MissionEditor;

import java.util.List;

public interface MissionService {

    Mission createMission(UserGameProfile gameProfile, SpaceShip ship,
                              Planet startPoint, Planet destinationPoint) throws AppException;
    Mission findMission(Long missionId) throws AppException;

    Mission editMissionBeforePerform(Mission mission, MissionEditor editor) throws AppException;
    Mission deleteMissionBeforePerform(Mission mission) throws AppException;
    Mission performMission(Mission mission) throws AppException;
    Mission completeMission(Mission mission) throws AppException;
    Mission cancelMissionAfterPerform(Mission mission) throws AppException;

    List<Mission> findAllMissions(UserGameProfile gameProfile) throws AppException;
    List<Mission> findAllReadyMissions(UserGameProfile gameProfile) throws AppException;
    List<Mission> findAllActiveMissions(UserGameProfile gameProfile) throws AppException;

}
