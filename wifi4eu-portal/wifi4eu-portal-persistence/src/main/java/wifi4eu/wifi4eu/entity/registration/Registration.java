package wifi4eu.wifi4eu.entity.registration;

import wifi4eu.wifi4eu.entity.thread.Thread;
import wifi4eu.wifi4eu.entity.user.User;
import wifi4eu.wifi4eu.entity.municipality.Municipality;

import javax.persistence.*;

@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @SequenceGenerator(name = "registration_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registration_seq")
    @Column(name = "id")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user")
    User user;

    @ManyToOne
    @JoinColumn(name = "municipality")
    Municipality municipality;

    @Column(name = "role")
    String role;

    @ManyToOne
    @JoinColumn(name = "thread")
    Thread thread;

    public Registration() {
    }

    public Registration(User user, Municipality municipality, String role, Thread thread) {
        this.user = user;
        this.municipality = municipality;
        this.role = role;
        this.thread = thread;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}