package pl.lukasz.service.validators;

import org.glassfish.jersey.internal.Errors;
import org.springframework.validation.ValidationUtils;
import pl.lukasz.service.constans.ServiceConstans;
import pl.lukasz.service.user.User;
import pl.lukasz.service.utilities.ServiceUtils;

public class UserRegisterValidator {


    public boolean supports(Class<?> cls){
        return User.class.equals(cls);
    }


    public void validate(Object object, Errors errors){
        User user = (User) object;

        ValidationUtils.rejectIfEmpty((org.springframework.validation.Errors) errors, "name", "error.userName.empty");
        ValidationUtils.rejectIfEmpty((org.springframework.validation.Errors) errors,"lastName","error.userLastName.empty");
        ValidationUtils.rejectIfEmpty((org.springframework.validation.Errors) errors, "email", "error.userEmail.empty");
        ValidationUtils.rejectIfEmpty((org.springframework.validation.Errors) errors, "password", "error.userPassword.empty");

        if (!user.getEmail().equals(null)){
            boolean isMatch = ServiceUtils.checkEmailOrPassword(ServiceConstans.EMAIL_PATTERN, user.getEmail());
            if (!isMatch){
                ((org.springframework.validation.Errors) errors).rejectValue("email","error.userEmailIsNotMatch");
            }
        }

        if (!user.getPassword().equals(null)){
            boolean isMatch = ServiceUtils.checkEmailOrPassword(ServiceConstans.PASSWORD_PATTERN, user.getEmail());
            if (!isMatch){
                ((org.springframework.validation.Errors) errors).rejectValue("password","error.userPasswordIsNotMatch");
            }
        }
    }

}