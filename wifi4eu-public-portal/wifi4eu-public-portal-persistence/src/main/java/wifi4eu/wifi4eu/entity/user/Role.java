package wifi4eu.wifi4eu.entity.user;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="roles")
public class Role {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role", length = 75)
    private String role;

    @Column(name="active", length = 20)
    private int active;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;


    public Role(){

    }

    public Role(String role, int active, Date createDate) {
        this.role = role;
        this.active = active;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Role{" +
                "role='" + role + '\'' +
                ", active=" + active +
                ", createDate=" + createDate +
                '}';
    }
}