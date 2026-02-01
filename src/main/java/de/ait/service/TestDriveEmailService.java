package de.ait.service;

import de.ait.dto.TestDriveConfirmationEmailRequest;
import de.ait.model.Car;
import de.ait.repository.CarRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class TestDriveEmailService {

    private final CarRepository carRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(TestDriveEmailService.class);

    public void sendConfirmationEmail(TestDriveConfirmationEmailRequest request) {
        sendEmail(request, "test-drive-confirmation.html", "Подтверждение тест-драйва");
    }

    public void sendReminderEmail(TestDriveConfirmationEmailRequest request) {
        sendEmail(request, "test-drive-reminder.html", "Напоминание о тест-драйве");
    }

    private void sendEmail(TestDriveConfirmationEmailRequest request, String templateName, String subject) {
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        Context context = new Context();
        context.setVariable("clientName", request.getClientName());
        context.setVariable("car", car);
        context.setVariable("testDriveDateTime", request.getTestDriveDateTime());
        context.setVariable("dealerAddress", request.getDealerAddress());
        context.setVariable("dealerPhone", request.getDealerPhone());

        String htmlContent = templateEngine.process(templateName, context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(request.getClientEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            logger.info("Test drive email sent to {} for carId={} at {}", request.getClientEmail(), request.getCarId(), request.getTestDriveDateTime());
        } catch (MessagingException e) {
            logger.error("Failed to send test drive email", e);
            throw new RuntimeException("Email send error");
        }
    }
}
