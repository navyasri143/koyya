package in.koyya.krissaco.sleek.controller;
import in.koyya.krissaco.sleek.dto.EmailRequest;
import in.koyya.krissaco.sleek.service.RepoEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/{sleekId}/email")
public class EmailController {

    @Autowired
    private RepoEmailService repoEmailService;

    // Send an email
    @PostMapping("/send")
    public String sendEmail(
            @PathVariable String sleekId,
            @RequestBody EmailRequest emailRequest,  // Expecting a JSON payload
            HttpServletRequest request) {  // To access headers like Authorization
        
        // Extract the Authorization token if needed for authentication
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return "Authorization header missing or invalid!";
        }
        
        String token = authorizationHeader.substring(7); // Extract token part after "Bearer "

        // You can add logic here to validate the token if necessary
        
        // Call the email service
        repoEmailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
        
        return "Email sent successfully to: " + emailRequest.getTo();
    }
}