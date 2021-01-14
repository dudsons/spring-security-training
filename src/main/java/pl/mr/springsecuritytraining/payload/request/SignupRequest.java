package pl.mr.springsecuritytraining.payload.request;

import pl.mr.springsecuritytraining.models.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Set;

public class SignupRequest {

    @NotBlank
    @Size(min = 3,max = 20)
    private String username;

    @Email
    @NotBlank
    @Size(max = 50)
    private String email;

    private Set<String>role;

    @NotBlank
    @Size(min = 6, max=40)
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
