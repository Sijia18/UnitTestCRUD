package co.develhope.unitTestUser;

import co.develhope.unitTestUser.entity.UserEntity;
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
class UserEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createUserWithNameTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Aurora");

        String userJSON = objectMapper.writeValueAsString(userEntity);
        MvcResult result = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        UserEntity userEntityResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);
        assertNotNull(userEntityResponse.getName());
        assertEquals("Aurora", userEntityResponse.getName());
    }

    @Test
    void readUserListTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Aurora");
        userService.create(userEntity);

        MvcResult result = this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<UserEntity> userEntityList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<UserEntity>>() {
        });

        assertNotNull(userEntityList);
        assertFalse(userEntityList.isEmpty());
    }

    @Test
    void readASingleUserTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Aurora");
        userEntity.setSurname("Scalici");
        UserEntity savedUserEntity = userService.create(userEntity);

        this.mockMvc.perform(get("/users/{id}", savedUserEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aurora"))
                .andExpect(jsonPath("$.surname").value("Scalici"));
    }

    @Test
    void updateUserTest() throws Exception {
        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setName("Rari");
        existingUserEntity.setSurname("Scalicci");
        UserEntity savedUserEntity = userService.create(existingUserEntity);

        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setName("Aurora");
        updatedUserEntity.setSurname("Scalici");

        String updatedUserJson = objectMapper.writeValueAsString(updatedUserEntity);
        this.mockMvc.perform(put("/users/{id}", savedUserEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aurora"))
                .andExpect(jsonPath("$.surname").value("Scalici"));
    }

    @Test
    void deleteUserTest() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Aurora");
        userEntity.setSurname("Scalici");
        UserEntity savedUserEntity = userService.create(userEntity);

        this.mockMvc.perform(delete("/users/{id}", savedUserEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<UserEntity> deletedUser = userService.readOne(savedUserEntity.getId());
        assertTrue(deletedUser.isEmpty());
    }
}