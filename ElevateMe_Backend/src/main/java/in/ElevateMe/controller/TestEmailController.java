//package in.ElevateMe.controller;
//
//import in.ElevateMe.service.EmailService;
//import jakarta.mail.MessagingException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class TestEmailController {
//
//    private final EmailService emailService;
//
//    @GetMapping("/test-email")
//    public String sendTestEmail(@RequestParam String to) {
//        try {
//            String html = """
//                <div style="font-family:sans-serif;">
//                    <h2>Test Email from ElevateMe</h2>
//                    <p>This is a <strong>test email</strong> to verify Brevo SMTP setup.</p>
//                </div>
//                """;
//
//            emailService.sendHtmlEmail(to, "✅ Test Email from ElevateMe", html);
//            log.info("✅ Test email sent successfully to {}", to);
//            return "Test email sent successfully to: " + to;
//        } catch (MessagingException e) {
//            log.error("❌ Failed to send email: {}", e.getMessage(), e);
//            return "Failed to send email: " + e.getMessage();
//        }
//    }
//}