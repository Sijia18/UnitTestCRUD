package co.develhope.unitTestUser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`user`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Column
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String name;
    @Column
    private String surname;
}
