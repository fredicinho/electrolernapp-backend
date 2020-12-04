package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;
    @Autowired
    PasswordEncoder encoder;

    public void updateResetPasswordToken(String token, String email)  {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        System.out.println(user.toString());
        user.setResetPasswordToken(token);
        userRepository.save(user);

    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token).orElseThrow(() -> new RuntimeException("Error: Token is not found."));
    }

    public void updatePassword(User user, String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public User addUserToSchoolClass(String writeInCode, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        SchoolClass schoolClass = schoolClassRepository.findByWriteInCode(writeInCode).orElseThrow(() -> new RuntimeException("Error: SchoolClass is not found."));;
        user.getInSchoolClasses().add(schoolClass);
        return userRepository.save(user);
    }
}
