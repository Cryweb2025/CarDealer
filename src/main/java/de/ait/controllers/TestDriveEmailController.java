package de.ait.controllers;

import de.ait.dto.TestDriveConfirmationEmailRequest;
import de.ait.service.TestDriveEmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email/test-drive")
public class TestDriveEmailController {

    private final TestDriveEmailService service;
    private static final Logger logger = LoggerFactory.getLogger(TestDriveEmailController.class);

    @PostMapping("/confirmation")
    public ResponseEntity<?> sendConfirmation(@RequestBody @Valid TestDriveConfirmationEmailRequest request) {
        logger.info("Received test drive confirmation request: {}", request);
        service.sendConfirmationEmail(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reminder")
    public ResponseEntity<?> sendReminder(@RequestBody @Valid TestDriveConfirmationEmailRequest request) {
        logger.info("Received test drive reminder request: {}", request);
        service.sendReminderEmail(request);
        return ResponseEntity.accepted().build();
    }
}
