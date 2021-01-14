package pl.mr.springsecuritytraining.security.postgresql.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mr.springsecuritytraining.models.User;
import pl.mr.springsecuritytraining.repository.UserRepository;
import static pl.mr.springsecuritytraining.security.postgresql.security.services.UserDetailsImpl.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name).orElseThrow(()-> new UsernameNotFoundException("User with name " + name + " is not found"));
        return UserDetailsImpl.build(user);
    }
}
