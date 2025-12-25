package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"})
@Builder(toBuilder = true)
public class User {
    private final long id;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @ и быть валидной")
    private final String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private final String login;

    @Getter(AccessLevel.NONE)
    private final String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private final LocalDate birthday;

    //@Builder.Default
    //private final Set<Long> friends = new HashSet<>();

    public String getName() {
        return name != null && !name.trim().isEmpty() ? name : login;
    }

    /*public User addFriend(Long friendId) {
        Set<Long> currentFriends = this.friends != null ? this.friends : new HashSet<>();
       2 Set<Long> updatedFriends = new HashSet<>(currentFriends);
        updatedFriends.add(friendId);

        return this.toBuilder()
                .friends(updatedFriends)
                .build();
    }

    public User removeFriend(Long friendId) {
        Set<Long> currentFriends = this.friends != null ? this.friends : new HashSet<>();
        Set<Long> updatedFriends = new HashSet<>(currentFriends);
        updatedFriends.remove(friendId);

        return this.toBuilder()
                .friends(updatedFriends)
                .build();
    }*/
}