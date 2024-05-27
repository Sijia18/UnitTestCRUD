package co.develhope.unitTestUser.controller;

import co.develhope.unitTestUser.entity.UserEntity;
import co.develhope.unitTestUser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserEntity createUser (@RequestBody UserEntity userEntity){
        return userService.create(userEntity);
    }

    @GetMapping
    public List<UserEntity> showUsers (){
        return userService.read();
    }

    @GetMapping("/{id}")
    public Optional<UserEntity> showASingleUser (@PathVariable Long id){
        return userService.readOne(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity userEntity) {
        return ResponseEntity.ok(userService.update(id, userEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}