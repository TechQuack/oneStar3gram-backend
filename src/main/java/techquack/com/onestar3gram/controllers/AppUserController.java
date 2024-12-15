package techquack.com.onestar3gram.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.exceptions.UserNotFoundException;
import techquack.com.onestar3gram.exceptions.UsernameNotFoundException;
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
    public ResponseEntity<AppUser> getUser(@PathVariable Integer id) {
        AppUser user = service.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) {
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
}
