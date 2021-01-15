package pl.mr.springsecuritytraining.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.mr.springsecuritytraining.models.ERole;
import pl.mr.springsecuritytraining.models.Role;
import pl.mr.springsecuritytraining.models.User;
import pl.mr.springsecuritytraining.payload.request.LoginRequest;
import pl.mr.springsecuritytraining.payload.request.SignupRequest;
import pl.mr.springsecuritytraining.payload.response.JwtResponse;
import pl.mr.springsecuritytraining.payload.response.MessageResponse;
import pl.mr.springsecuritytraining.repository.RoleRepository;
import pl.mr.springsecuritytraining.repository.UserRepository;
import pl.mr.springsecuritytraining.security.jwt.JwtUtils;
import pl.mr.springsecuritytraining.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> (item).getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwtToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.existsUserByName(signupRequest.getUsername())){
           return  ResponseEntity
                   .badRequest()
                   .body(new MessageResponse("Usie is existing, try different name for user"));
        }

        if(userRepository.existsUserByEmail(signupRequest.getEmail())){

            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Email is existing. Try different email"));
        }

        Set<String>strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        // Create new user's account
        User userToRegister = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        if(strRoles == null){
            Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(()-> new RuntimeException("Role not found"));
            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                switch (role){
                    case "admin":
                       roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(()-> new RuntimeException("Role is not found")));
                       break;
                    case "mod":
                        roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(()-> new RuntimeException("Role is not found")));
                        break;
                    default:
                        roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(()-> new RuntimeException("Role is not found")));
                }
            });
        }

        userToRegister.setRoles(roles);
        userRepository.save(userToRegister);

        return ResponseEntity.ok(new MessageResponse("User successfully added"));
    }
}

