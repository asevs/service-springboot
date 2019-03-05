package pl.lukasz.service.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.lukasz.service.constans.ServiceConstans;
import pl.lukasz.service.user.User;
import pl.lukasz.service.utilities.ServiceUtils;

public class UserRegisterValidator implements Validator {


    public boolean supports(Class<?> cls){
        return User.class.equals(cls);
    }


    public void validate(Object object, Errors errors){
        User user = (User) object;

        ValidationUtils.rejectIfEmpty( errors, "name", "error.userName.empty");
        ValidationUtils.rejectIfEmpty(errors,"lastName","error.userLastName.empty");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.userEmail.empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "error.userPassword.empty");

        if (!user.getEmail().equals(null)){
            boolean isMatch = ServiceUtils.checkEmailOrPassword(ServiceConstans.EMAIL_PATTERN, user.getEmail());
            if (!isMatch){
                (errors).rejectValue("email","error.userEmailIsNotMatch");
            }
        }

        if (!user.getPassword().equals(null)){
            boolean isMatch = ServiceUtils.checkEmailOrPassword(ServiceConstans.PASSWORD_PATTERN, user.getPassword());
            if (!isMatch){
                (errors).rejectValue("password","error.userPasswordIsNotMatch");
            }
        }
    }
    public void validateEmailExist(User user, Errors errors) {
        if (user != null) {
            errors.rejectValue("email", "error.userEmailExist");
        }
    }

}