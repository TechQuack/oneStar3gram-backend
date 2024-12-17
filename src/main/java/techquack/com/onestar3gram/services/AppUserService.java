package techquack.com.onestar3gram.services;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.repositories.AppUserRepository;

import java.util.List;
import java.util.Optional;

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

    public AppUser getUser(OAuth2User oAuth2User) {
        return appUserRepository.findByEmail(oAuth2User.getAttribute(("email"))).orElse(null);
    }

    public boolean canChangeEmail(AppUser user, AppUser newUser) {
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(newUser.getEmail());
        return optionalAppUser.isEmpty() ||  optionalAppUser.get().getKeyCloakId().equals(user.getKeyCloakId());
    }

    public boolean canChangeUsername(AppUser user, AppUser newUser) {
        Optional<AppUser> optionalAppUser = appUserRepository.findByUsername(newUser.getUsername());
        return optionalAppUser.isEmpty() ||  optionalAppUser.get().getKeyCloakId().equals(user.getKeyCloakId());
    }


    public void createUser(OAuth2User user) {
        AppUser newUser = new AppUser();
        newUser.setEmail(user.getAttribute("email"));
        newUser.setUsername(user.getAttribute("preferred_username"));
        newUser.setKeyCloakId(user.getAttribute("sub"));
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


    public void updateUser(AppUser user, AppUser newUser) {
        user.setLastName(newUser.getLastName());
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setFirstName(newUser.getFirstName());
        appUserRepository.save(user);
    }

    public void deleteUser(AppUser user) {
        appUserRepository.delete(user);
    }
}
