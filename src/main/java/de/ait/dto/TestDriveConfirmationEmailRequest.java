package de.ait.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDriveConfirmationEmailRequest {

    @NotBlank
    @Email
    private String clientEmail;

    @NotBlank
    private String clientName;

    @NotNull
    private Long carId;

    @NotBlank
    private String testDriveDateTime;

    @NotBlank
    private String dealerAddress;

    @NotBlank
    private String dealerPhone;
}
