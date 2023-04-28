package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestDto {

    protected Long id;
    protected String name;
    @Email(message = "Неверный формат почты пользователя!")
    @NotNull(message = "Почта пользователя не должна быть NULL!")
    @NotBlank(message = "Почта пользователя не должна быть пустой!")
    protected String email;

}
