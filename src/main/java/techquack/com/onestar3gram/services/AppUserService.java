package techquack.com.onestar3gram.services;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.repositories.AppUserRepository;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser getUserById(Integer id) {
        return appUserRepository.findById(id).orElse(null);
    }

    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByUsername(username).orElse(null);
    }

    public List<AppUser> getUsers() {
        return appUserRepository.findBy();
    }

    public void createUser(OAuth2User user) {
        AppUser newUser = new AppUser();
        newUser.setEmail(user.getAttribute("email"));
        String username = user.getAttribute("preferred_username");
        if (!username.isEmpty()) {
            newUser.setUsername(username);
        }

        String firstName = user.getAttribute("given_name");
        if (!firstName.isEmpty()) {
            newUser.setFirstName(firstName);
        }

        String lastName = user.getAttribute("family_name");
        if (!lastName.isEmpty()) {
            newUser.setLastName(lastName);
        }
        appUserRepository.save(newUser);
    }
 }
