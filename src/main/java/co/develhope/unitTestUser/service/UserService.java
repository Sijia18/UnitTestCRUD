package co.develhope.unitTestUser.service;

import co.develhope.unitTestUser.entity.User;
import co.develhope.unitTestUser.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User create (User user){
        return userRepo.save((user));
    }

    public List<User> read (){
        return userRepo.findAll();
    }

    public Optional<User> readOne (Long id){
        return userRepo.findById(id);
    }

    public User update(Long id, User userDetails) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id" + id + "not found"));
        user.setName(userDetails.getName());
        user.setSurname(userDetails.getSurname());
        return userRepo.save(user);
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}