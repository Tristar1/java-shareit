package ru.practicum.shareit.user.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    protected Integer id;
    protected String name;
    protected String email;
}
