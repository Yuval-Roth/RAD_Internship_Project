package com.arealcompany.ms_api_gateway.business.security;

import com.arealcompany.ms_api_gateway.repository.UsersRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("UserManager")
public class UserManager implements UserDetailsManager {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ENC_PREFIX = "{bcrypt}";
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();


    public UserManager(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void createUser(UserDetails user) {
        String hashedPassword = passwordEncoder.encode(ENC_PREFIX+user.getPassword());
        User newUser = new User(user.getUsername(), hashedPassword);
        usersRepository.insert(newUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        String hashedPassword = passwordEncoder.encode(ENC_PREFIX+user.getPassword());
        User updatedUser = new User(user.getUsername(), hashedPassword);
        usersRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(String username) {
        usersRepository.deleteById(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            String username = currentUser.getName();
            User currentUserFromDB = usersRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if(passwordsMatch(oldPassword, currentUserFromDB.getPassword())) {
                String hashedPassword = passwordEncoder.encode(ENC_PREFIX+newPassword);
                User updatedUser = new User(username, hashedPassword);
                usersRepository.save(updatedUser);
            } else {
                throw new AccessDeniedException("Can't change password as old password is incorrect.");
            }
        }
    }

    @Override
    public boolean userExists(String username) {
        return usersRepository.existsById(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private boolean passwordsMatch(String toTest, String known) {
        return passwordEncoder.matches(toTest, known);
    }

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {
        if(!userExists("admin")) {
            createUser(new User("admin", "adminpass"));
        }
    }

    public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
        Assert.notNull(securityContextHolderStrategy, "securityContextHolderStrategy cannot be null");
        this.securityContextHolderStrategy = securityContextHolderStrategy;
    }
}
