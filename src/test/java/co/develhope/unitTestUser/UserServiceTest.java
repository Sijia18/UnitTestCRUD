package co.develhope.unitTestUser;

import co.develhope.unitTestUser.entity.User;
import co.develhope.unitTestUser.repository.UserRepo;
import co.develhope.unitTestUser.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createUserTest() throws Exception {
        User user = new User();
        user.setName("Aurora");

        String userJSON = objectMapper.writeValueAsString(user);
        //json è il parser da json-oggetto e viceversa
        MvcResult result = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
        User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertNotNull(userResponse.getName());
    }

    @Test
    void readUsersTest() {
        User user1 = new User();
        user1.setName("Aurora");
        user1.setSurname("Scalici");

        User user2 = new User();
        user2.setName("Alberto");
        user2.setSurname("Bu");

        userRepo.save(user1);
        userRepo.save(user2);

        List<User> userEntities = userService.read();
        assertEquals(2, userEntities.size());
    }

    @Test
    void updateUserTest() {
        User existingUser = new User();
        existingUser.setName("Rari");
        existingUser.setSurname("Scalicci");
        userRepo.save(existingUser);
        Long userId = existingUser.getId();

        User updatedUser = new User();
        updatedUser.setName("Aurora");
        updatedUser.setSurname("Scalici");
        userService.update(userId, updatedUser);

        Optional<User> retrievedUpdatedUser = userRepo.findById(userId);
        assertTrue(retrievedUpdatedUser.isPresent());
        User retrievedUser = retrievedUpdatedUser.get();
        assertEquals(userId, retrievedUser.getId());
        assertEquals("Aurora", retrievedUser.getName());
        assertEquals("Scalici", retrievedUser.getSurname());
    }

    @Test
    void deleteUserTest() {
        User user = new User();
        user.setName("Aurora");
        user.setSurname("Scalici");
        userRepo.save(user);
        Long userId = user.getId();
        userService.delete(userId);

        Optional<User> deletedUser = userRepo.findById(userId);
        assertFalse(deletedUser.isPresent());
    }
}