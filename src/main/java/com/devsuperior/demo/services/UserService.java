package com.devsuperior.demo.services;

import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.projections.UserDetailsProjection;
import com.devsuperior.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }
        User user = new User();
        userLoad(user, username, result);

        return user;
    }

    private void userLoad(User user, String username, List<UserDetailsProjection> projection) {
        user.setName(username);
        user.setEmail(username);
        user.setPassword(projection.get(0).getPassword());
        for (UserDetailsProjection u : projection) {
            user.addRole(new Role(u.getRoleId(), u.getAuthority()));
        }
    }
}
