package pl.lukasz.service.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.lukasz.service.constans.ServiceConstans;
import pl.lukasz.service.user.User;
import pl.lukasz.service.utilities.ServiceUtils;


public class ChangePasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return User.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        @SuppressWarnings("unused")
        User u = (User) obj;

        ValidationUtils.rejectIfEmpty(errors, "newPassword", "error.userPassword.empty");

    }

    public void checkPasswords(String newPassword, Errors errors) {

        if (!newPassword.equals(null)) {
            boolean isMatch = ServiceUtils.checkEmailOrPassword(ServiceConstans.PASSWORD_PATTERN, newPassword);
            if(!isMatch) {
                errors.rejectValue("newPassword", "error.userPasswordIsNotMatch");
            }
        }
    }
}