package co.develhope.unitTestUser.controller;

import co.develhope.unitTestUser.entity.User;
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
    public User createUser (@RequestBody User user){
        return userService.create(user);
    }

    @GetMapping
    public List<User> showUsers (){
        return userService.read();
    }

    @GetMapping("/{id}")
    public Optional<User> showASingleUser (@PathVariable Long id){
        return userService.readOne(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}