package com.app.faculty.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {
    private Long id;
    @NotBlank
    @Pattern(regexp = "\\b[А-Яа-яІЇЄЖЭЁИЪA-Za-z]{2,15}\\b", message = "Not valid firstname, enter only letters")
    private String firstName;
    @NotBlank
    @Pattern(regexp = "\\b[А-Яа-яІЇЄЖЭЁИЪA-Za-z]{2,15}\\b", message = "Not valid lastname, enter only letters")
    private String lastName;
    @NotBlank
    @Pattern(regexp = "\\b[A-Za-z0-9._]+@[A-Za-z0-9.]+\\.[A-Za-z]{2,5}\\b", message = "Not valid email")
    private String email;
    @NotBlank
    @Pattern(regexp = "\\b[A-Za-z0-9._]{5,15}\\b", message = "Not valid username")
    private String username;
    @NotBlank
    @Pattern(regexp = "\\b[А-Яа-яІЇЄЭЁИA-Za-z0-9._!@#&$]{5,25}\\b", message = "Not valid password")
    private String password;
    @NotBlank
    @Pattern(regexp = "\\b[А-Яа-яІЇЄЭЁИA-Za-z0-9._!@#&$]{5,25}\\b", message = "Not valid password")
    private String passwordConfirm;
}
