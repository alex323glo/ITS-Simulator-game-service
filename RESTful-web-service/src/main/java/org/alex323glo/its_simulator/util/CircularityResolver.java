package org.alex323glo.its_simulator.util;

import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;

/**
 * TODO add doc.
 *
 * @author Alexey_O
 * @version 0.1
 */
public class CircularityResolver {

    public static User resolveUserWithLazyGameProfile(User user) {
        if (user.getUserExtension() != null) {
            user.getUserExtension().setUser(null);
        }
        if (user.getUserGameProfile() != null) {
            user.getUserGameProfile().setUser(null);
            user.getUserGameProfile().setShips(null);
            user.getUserGameProfile().setMissions(null);
        }
        return user;
    }

    public static UserGameProfile resolveLazyGameProfile(UserGameProfile gameProfile) {
        if (gameProfile.getUser() != null) {
            gameProfile.getUser().setUserGameProfile(null);
            gameProfile.getUser().getUserExtension().setUser(null);
        }
        gameProfile.setMissions(null);
        gameProfile.setShips(null);
        return gameProfile;
    }

}
