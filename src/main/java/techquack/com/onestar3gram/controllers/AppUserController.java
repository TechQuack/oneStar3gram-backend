package techquack.com.onestar3gram.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.exceptions.*;
import techquack.com.onestar3gram.services.AppUserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService service;
    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Integer id) throws UserNotFoundException {
        AppUser user = service.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) throws UsernameNotFoundException {
        AppUser user = service.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    @GetMapping("/self")
    public ResponseEntity<AppUser> getSelf(@AuthenticationPrincipal OAuth2User principal) throws NotLoggedInException {
        AppUser user = service.getUser(principal);
        if (user == null) {
            throw new NotLoggedInException();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<AppUser> updateUser(@AuthenticationPrincipal OAuth2User principal,
                                               AppUser newUser) throws NotLoggedInException, EmailAlreadyTakenException, UsernameAlreadyTakenException {
        AppUser user = service.getUser(principal);
        if (user == null) {
            throw new NotLoggedInException();
        }
        if(!service.canChangeEmail(user, newUser)) {
            throw new EmailAlreadyTakenException(newUser.getEmail());
        }

        if(!service.canChangeUsername(user, newUser)) {
            throw new UsernameAlreadyTakenException(newUser.getUsername());
        }
        service.updateUser(user, newUser);
        return ResponseEntity.ok(newUser);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<AppUser> deleteUser (@PathVariable int id) throws UserNotFoundException {
        AppUser user = service.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        service.deleteUser(user);
        return ResponseEntity.ok(user);
    }
}
