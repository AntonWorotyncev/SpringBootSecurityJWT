package com.schoolt1.springbootsecurityjwt.service;

import com.schoolt1.springbootsecurityjwt.model.Role;
import com.schoolt1.springbootsecurityjwt.model.User;
import com.schoolt1.springbootsecurityjwt.repository.UserRepository;
import com.schoolt1.springbootsecurityjwt.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
    }

    @Test
    public void testSave() {
        when(repository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("testUser", savedUser.getUsername());
    }

    @Test
    public void testCreate_NewUser() {
        when(repository.existsByUsername(user.getUsername())).thenReturn(false);
        when(repository.existsByEmail(user.getEmail())).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals("testUser", createdUser.getUsername());
        Mockito.verify(repository, Mockito.times(1)).save(user);
    }

    @Test
    public void testCreate_UserExistsByUsername() {
        when(repository.existsByUsername(user.getUsername())).thenReturn(true);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> userService.create(user));

        Assertions.assertEquals("Пользователь с таким именем уже существует", exception.getMessage());
    }

    @Test
    public void testCreate_UserExistsByEmail() {
        when(repository.existsByUsername(user.getUsername())).thenReturn(false);
        when(repository.existsByEmail(user.getEmail())).thenReturn(true);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> userService.create(user));

        Assertions.assertEquals("Пользователь с таким email уже существует", exception.getMessage());
    }


    @Test
    public void testGetCurrentUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findByUsername("testUser")).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrentUser();

        Assertions.assertNotNull(currentUser);
        Assertions.assertEquals("testUser", currentUser.getUsername());
    }

    @Test
    public void testGetAdmin() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);

        userService.getAdmin();

        Assertions.assertEquals(Role.ROLE_ADMIN, user.getRole());
        Mockito.verify(repository, Mockito.times(1)).save(user);
    }
}
