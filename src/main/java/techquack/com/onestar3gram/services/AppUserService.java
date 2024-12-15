package techquack.com.onestar3gram.services;

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
 }
