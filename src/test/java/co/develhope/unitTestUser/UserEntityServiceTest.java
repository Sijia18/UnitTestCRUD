package co.develhope.unitTestUser;

import co.develhope.unitTestUser.entity.UserEntity;
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
class UserEntityServiceTest {

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
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Aurora");

        String userJSON = objectMapper.writeValueAsString(userEntity);
        //json è il parser da json-oggetto e viceversa
        MvcResult result = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
        UserEntity userEntityResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);
        assertNotNull(userEntityResponse.getName());
    }

    @Test
    void readUsersTest() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setName("Aurora");
        userEntity1.setSurname("Scalici");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setName("Alberto");
        userEntity2.setSurname("Bu");

        userRepo.save(userEntity1);
        userRepo.save(userEntity2);

        List<UserEntity> userEntities = userService.read();
        assertEquals(2, userEntities.size());
    }

    @Test
    void updateUserTest() {
        UserEntity existingUser = new UserEntity();
        existingUser.setName("Rari");
        existingUser.setSurname("Scalicci");
        userRepo.save(existingUser);
        Long userId = existingUser.getId();

        UserEntity updatedUser = new UserEntity();
        updatedUser.setName("Aurora");
        updatedUser.setSurname("Scalici");
        userService.update(userId, updatedUser);

        Optional<UserEntity> retrievedUpdatedUser = userRepo.findById(userId);
        assertTrue(retrievedUpdatedUser.isPresent());
        UserEntity retrievedUserEntity = retrievedUpdatedUser.get();
        assertEquals(userId, retrievedUserEntity.getId());
        assertEquals("Aurora", retrievedUserEntity.getName());
        assertEquals("Scalici", retrievedUserEntity.getSurname());
    }

    @Test
    void deleteUserTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Aurora");
        userEntity.setSurname("Scalici");
        userRepo.save(userEntity);
        Long userId = userEntity.getId();
        userService.delete(userId);

        Optional<UserEntity> deletedUser = userRepo.findById(userId);
        assertFalse(deletedUser.isPresent());
    }
}