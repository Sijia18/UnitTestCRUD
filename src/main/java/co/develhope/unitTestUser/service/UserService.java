package co.develhope.unitTestUser.service;

import co.develhope.unitTestUser.entity.UserEntity;
import co.develhope.unitTestUser.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public UserEntity create (UserEntity userEntity){
        return userRepo.save((userEntity));
    }

    public List<UserEntity> read (){
        return userRepo.findAll();
    }

    public Optional<UserEntity> readOne (Long id){
        return userRepo.findById(id);
    }

    public UserEntity update(Long id, UserEntity userEntityDetails) {
        UserEntity userEntity = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id" + id + "not found"));
        userEntity.setName(userEntityDetails.getName());
        userEntity.setSurname(userEntityDetails.getSurname());
        return userRepo.save(userEntity);
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}