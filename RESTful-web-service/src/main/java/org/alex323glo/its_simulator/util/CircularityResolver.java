package org.alex323glo.its_simulator.util;

import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;

/**
 * Custom util - solution to problem of circular references.
 *
 * Especially, it's important for JSON serialization/deserialization, assertions (Unit tests)
 * and output formation (logs, debugger output, etc.).
 *
 * @author Alexey_O
 * @version 0.1
 */
public class CircularityResolver {

    /**
     * Resolves circular references of target User instance.
     *
     * @param user target instance.
     * @return fixed target instance.
     */
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

    /**
     * Resolves circular references of target UserGameProfile instance.
     *
     * @param gameProfile target instance.
     * @return fixed target instance.
     */
    public static UserGameProfile resolveLazyGameProfile(UserGameProfile gameProfile) {
        if (gameProfile.getUser() != null) {
            gameProfile.getUser().setUserGameProfile(null);

            if (gameProfile.getUser().getUserExtension() != null) {
                gameProfile.getUser().getUserExtension().setUser(null);
            }
        }
        gameProfile.setMissions(null);
        gameProfile.setShips(null);
        return gameProfile;
    }

    /**
     * Resolves circular references of target UserExtension instance.
     *
     * @param userExtension target instance.
     * @return fixed target instance.
     */
    public static UserExtension resolveUserExtensionWithLazyGameProfile(UserExtension userExtension) {
        if (userExtension.getUser() != null) {
            userExtension.getUser().setUserExtension(null);

            if (userExtension.getUser().getUserGameProfile() != null) {
                userExtension.getUser().getUserGameProfile().setUser(null);
                userExtension.getUser().getUserGameProfile().setShips(null);
                userExtension.getUser().getUserGameProfile().setMissions(null);
            }
        }
        return userExtension;
    }

}
