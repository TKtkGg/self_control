package com.tktkgg.selfcontrol.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import com.tktkgg.selfcontrol.entity.Profile;
import com.tktkgg.selfcontrol.entity.Schedule;
import com.tktkgg.selfcontrol.entity.Setting;
import com.tktkgg.selfcontrol.entity.Task;
import com.tktkgg.selfcontrol.entity.User;
import com.tktkgg.selfcontrol.repository.ProfileRepository;
import com.tktkgg.selfcontrol.repository.ScheduleRepository;
import com.tktkgg.selfcontrol.repository.SettingRepository;
import com.tktkgg.selfcontrol.repository.TaskRepository;
import com.tktkgg.selfcontrol.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthenticationAuthorizationIntegrationTest {
    private static final String PASSWORD = "correct-password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void unauthenticatedUserReceives401FromProtectedApi() throws Exception {
        mockMvc.perform(get("/api/profile"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentTypeCompatibleWith(
                MediaType.valueOf("application/problem+json")
            ))
            .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
            .andExpect(jsonPath("$.instance").value("/api/profile"));
    }

    @Test
    void authenticatedUserCanReadOwnPrivateProfile() throws Exception {
        User user = createUser();
        createProfileAndSetting(user, false);
        MockHttpSession session = loginAs(user);

        mockMvc.perform(
            get("/api/profile")
                .session(session)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(user.getId().toString()))
            .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void successfulLoginRotatesSessionId() throws Exception {
        User user = createUser();
        MockHttpSession anonymousSession = new MockHttpSession();
        String anonymousSessionId = anonymousSession.getId();

        MvcResult result = mockMvc.perform(
            post("/api/auth/login")
                .session(anonymousSession)
                .with(csrf())
                .with(remoteAddress())
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson(user.getEmail(), PASSWORD))
        )
            .andExpect(status().isOk())
            .andReturn();

        MockHttpSession authenticatedSession =
            (MockHttpSession) result.getRequest().getSession(false);

        assertNotNull(authenticatedSession);
        assertNotEquals(anonymousSessionId, authenticatedSession.getId());
    }

    @Test
    void csrfTokenIsRequiredForProfileMutation() throws Exception {
        User user = createUser();
        createProfileAndSetting(user, true);
        MockHttpSession session = loginAs(user);

        mockMvc.perform(
            patch("/api/profile")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateProfileJson("updated-user"))
        )
            .andExpect(status().isForbidden())
            .andExpect(content().contentTypeCompatibleWith(
                MediaType.valueOf("application/problem+json")
            ))
            .andExpect(jsonPath("$.code").value("FORBIDDEN"))
            .andExpect(jsonPath("$.instance").value("/api/profile"));
    }

    @Test
    void anotherUserCannotReadPrivateProfileOrSchedule() throws Exception {
        User owner = createUser();
        createProfileAndSetting(owner, false);
        createSchedule(owner);

        User viewer = createUser();
        createProfileAndSetting(viewer, true);
        MockHttpSession viewerSession = loginAs(viewer);

        mockMvc.perform(
            get("/api/users/{userId}/profile", owner.getId())
                .session(viewerSession)
        )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value("USER_FORBIDDEN"));

        mockMvc.perform(
            get("/api/users/{userId}/schedule", owner.getId())
                .session(viewerSession)
        )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value("USER_FORBIDDEN"));
    }

    @Test
    void anotherUserCannotUpdateOrDeleteTask() throws Exception {
        User owner = createUser();
        Task task = createTask(owner);

        User attacker = createUser();
        MockHttpSession attackerSession = loginAs(attacker);

        mockMvc.perform(
            patch("/api/tasks/{taskId}", task.getId())
                .session(attackerSession)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateTaskJson("changed-by-attacker"))
        )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value("TASK_ACCESS_DENIED"));

        mockMvc.perform(
            delete("/api/tasks/{taskId}", task.getId())
                .session(attackerSession)
                .with(csrf())
        )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value("TASK_ACCESS_DENIED"));

        assertTrue(taskRepository.findById(task.getId()).isPresent());
        assertEquals("Original task", taskRepository.findById(task.getId()).orElseThrow().getName());
    }

    @Test
    void authenticatedUserCanUpdateOwnTask() throws Exception {
        User owner = createUser();
        Task task = createTask(owner);
        MockHttpSession ownerSession = loginAs(owner);

        mockMvc.perform(
            patch("/api/tasks/{taskId}", task.getId())
                .session(ownerSession)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateTaskJson("updated-task"))
        )
            .andExpect(status().isOk());

        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertEquals("updated-task", updatedTask.getName());
        assertEquals(LocalTime.of(10, 0), updatedTask.getStartTime());
        assertEquals(LocalTime.of(11, 0), updatedTask.getEndTime());
    }

    @Test
    void logoutInvalidatesSessionAndProtectedApiReturns401() throws Exception {
        User user = createUser();
        createProfileAndSetting(user, true);
        MockHttpSession session = loginAs(user);

        mockMvc.perform(
            post("/api/auth/logout")
                .session(session)
                .with(csrf())
        )
            .andExpect(status().isOk());

        mockMvc.perform(
            get("/api/profile")
                .session(session)
        )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }

    private User createUser() {
        User user = new User();
        user.setUsername("auth-test-user");
        user.setEmail(uniqueEmail());
        user.setPasswordHash(passwordEncoder.encode(PASSWORD));
        return userRepository.saveAndFlush(user);
    }

    private void createProfileAndSetting(User user, boolean isPublic) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setIcon(null);
        profile.setSelfIntroduce("Test profile");
        profileRepository.saveAndFlush(profile);

        Setting setting = new Setting();
        setting.setUser(user);
        setting.setIsPublic(isPublic);
        setting.setIsAuthorizeNotification(true);
        settingRepository.saveAndFlush(setting);
    }

    private Schedule createSchedule(User user) {
        Schedule schedule = new Schedule();
        schedule.setUser(user);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setTitle("Monday schedule");
        return scheduleRepository.saveAndFlush(schedule);
    }

    private Task createTask(User user) {
        Schedule schedule = createSchedule(user);

        Task task = new Task();
        task.setSchedule(schedule);
        task.setStartTime(LocalTime.of(9, 0));
        task.setEndTime(LocalTime.of(10, 0));
        task.setName("Original task");
        return taskRepository.saveAndFlush(task);
    }

    private MockHttpSession loginAs(User user) throws Exception {
        MvcResult result = mockMvc.perform(
            post("/api/auth/login")
                .with(csrf())
                .with(remoteAddress())
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson(user.getEmail(), PASSWORD))
        )
            .andExpect(status().isOk())
            .andReturn();

        MockHttpSession session =
            (MockHttpSession) result.getRequest().getSession(false);

        assertNotNull(session);
        return session;
    }

    private String loginJson(String email, String password) {
        return "{\"email\":\"" + email
            + "\",\"password\":\"" + password + "\"}";
    }

    private String updateProfileJson(String username) {
        return "{\"username\":\"" + username
            + "\",\"icon\":null,\"selfIntroduce\":\"Updated profile\"}";
    }

    private String updateTaskJson(String name) {
        return "{\"startHour\":10,\"startMinute\":0"
            + ",\"endHour\":11,\"endMinute\":0"
            + ",\"name\":\"" + name + "\"}";
    }

    private RequestPostProcessor remoteAddress() {
        return request -> {
            request.setRemoteAddr("auth-test-ip-" + UUID.randomUUID());
            return request;
        };
    }

    private String uniqueEmail() {
        return "auth-test-" + UUID.randomUUID() + "@example.com";
    }
}
