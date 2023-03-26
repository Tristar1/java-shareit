package ru.practicum.shareit.user.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    protected String email;
    protected String name;
    protected Integer id;

}
