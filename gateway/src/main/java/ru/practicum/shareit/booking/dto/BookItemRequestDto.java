package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	@NotNull(message = "Номер предмета бронирования должен быть заполнен!")
	private long itemId;
	@NotNull(message = "Время начала бронирования должно быть заполнено!")
	@FutureOrPresent(message = "Время начала бронирования должно быть текущим или буудущим!")
	private LocalDateTime start;
	@Future(message = "Время окончания бронирования должно быть буудущим!")
	@NotNull(message = "Время окончания бронирования должно быть заполнено!")
	private LocalDateTime end;
	private Long id;
	private Long bookerId;
	private String status;
}
