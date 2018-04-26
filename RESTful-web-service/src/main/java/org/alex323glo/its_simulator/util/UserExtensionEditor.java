package org.alex323glo.its_simulator.util;

import org.alex323glo.its_simulator.model.UserExtension;

/**
 * Additional util class, which helps to
 * safely modify UserExtension object data (without change of non-changeable fields)
 *
 * @author Alexey_O
 * @version 0.1
 */
public class UserExtensionEditor {

    private UserExtension tempUserExtension;

    public UserExtensionEditor() {
        tempUserExtension = new UserExtension();
    }

    public UserExtensionEditor email(String email) {
        tempUserExtension.setEmail(email);
        return this;
    }

    // ...

    public UserExtension merge(UserExtension userExtension) {
        if (tempUserExtension.getEmail() != null) {
            userExtension.setEmail(tempUserExtension.getEmail());
        }

        // ...

        return userExtension;
    }

    public UserExtensionEditor clearEditor() {
        tempUserExtension = new UserExtension();
        return this;
    }
}
