package org.alex323glo.its_simulator.util;

import org.alex323glo.its_simulator.exception.ValidationException;
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
    private Validator validator;

    public UserExtensionEditor(Validator validator) {
        this.validator = validator;
        tempUserExtension = new UserExtension();
    }

    public UserExtensionEditor email(String email) {
        try {
            validator.validateEmail(email);
            tempUserExtension.setEmail(email);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return this;
    }

    // TODO add other UserExtension's properties ...

    public UserExtension merge(UserExtension userExtension) {
        if (tempUserExtension.getEmail() != null) {
            userExtension.setEmail(tempUserExtension.getEmail());
        }

        // TODO add other UserExtension's properties ...

        return userExtension;
    }

    public UserExtensionEditor clearEditor() {
        tempUserExtension = new UserExtension();
        return this;
    }
}
