package technikal.task.fishmarket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Table(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users;

    private String name;

    public Integer getId() {
        return this.id;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setAuthority(String name) {
        this.name = name;
    }
}