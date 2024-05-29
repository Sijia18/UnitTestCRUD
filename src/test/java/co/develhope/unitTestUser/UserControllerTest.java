package co.develhope.unitTestUser;

import co.develhope.unitTestUser.entity.User;
import co.develhope.unitTestUser.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createUserWithNameTest() throws Exception {
        User user = new User();
        user.setName("Aurora");

        String userJSON = objectMapper.writeValueAsString(user);
        MvcResult result = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertNotNull(userResponse.getName());
        assertEquals("Aurora", userResponse.getName());
    }

    @Test
    void readUserListTest() throws Exception {
        User user = new User();
        user.setName("Aurora");
        userService.create(user);

        MvcResult result = this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<User> userList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<User>>() {
        });

        assertNotNull(userList);
        assertFalse(userList.isEmpty());
    }

    @Test
    void readASingleUserTest() throws Exception {
        User user = new User();
        user.setName("Aurora");
        user.setSurname("Scalici");
        User savedUser = userService.create(user);

        this.mockMvc.perform(get("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aurora"))
                .andExpect(jsonPath("$.surname").value("Scalici"));
    }

    @Test
    void updateUserTest() throws Exception {
        User existingUser = new User();
        existingUser.setName("Rari");
        existingUser.setSurname("Scalicci");
        User savedUser = userService.create(existingUser);

        User updatedUser = new User();
        updatedUser.setName("Aurora");
        updatedUser.setSurname("Scalici");

        String updatedUserJson = objectMapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(put("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aurora"))
                .andExpect(jsonPath("$.surname").value("Scalici"));
    }

    @Test
    void deleteUserTest() throws Exception {
        User user = new User();
        user.setName("Aurora");
        user.setSurname("Scalici");
        User savedUser = userService.create(user);

        this.mockMvc.perform(delete("/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<User> deletedUser = userService.readOne(savedUser.getId());
        assertTrue(deletedUser.isEmpty());
    }
}