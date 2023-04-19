package ru.practicum.shareit.user;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Users")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column(nullable = false, unique = true)
    private String email;

}
