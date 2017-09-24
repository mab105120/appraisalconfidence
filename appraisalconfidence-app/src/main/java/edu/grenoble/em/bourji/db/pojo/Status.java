package edu.grenoble.em.bourji.db.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Moe on 9/23/2017.
 */
@Entity
@Table(name = "STATUS")
public class Status implements Serializable {

    @Id
    @Column(name = "ID", length = 64, nullable = false)
    private String user;
    @Id
    @Column(name = "STATUS", length = 64, nullable = false)
    private String status;
    @Id
    @Column(name = "TIME")
    private Timestamp time;

    public Status() {
        // no-arg default constructor for hibernate
    }

    public Status(String user, String status) {
        this.user = user;
        this.status = status;
        this.time = Timestamp.valueOf(LocalDateTime.now());
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Status)) return false;
        Status that = (Status) o;
        return this.getUser().equals(that.getUser()) &&
                this.getStatus().equals(that.getStatus()) &&
                this.getTime().equals(that.getTime());
    }

    @Override
    public int hashCode() {
        int hashCode = 5;
        return 37 * hashCode + (
                this.user.hashCode() + this.status.hashCode() + this.time.hashCode()
        );
    }
}
