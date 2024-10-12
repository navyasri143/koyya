package in.koyya.krissaco.sleek.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.koyya.krissaco.sleek.config.CustomUserDetailsService;
import in.koyya.krissaco.sleek.config.JwtUtil;
import in.koyya.krissaco.sleek.dto.SubscriptionRequest;
import in.koyya.krissaco.sleek.entity.Subscription;
import in.koyya.krissaco.sleek.service.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
@CrossOrigin(origins = "http://localhost:3000")
public class SubscriptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerSubscription(@RequestBody SubscriptionRequest request) {
        LOGGER.info("Received subscription registration request for email: '{}'", request.getEmail());
        subscriptionService.registerSubscription(request);
        return new ResponseEntity<>(request.getSleekId(), HttpStatus.CREATED);
    }

    @GetMapping("/{sleekId}/activate")
    public ResponseEntity<String> activateSubscription(@PathVariable String sleekId, @RequestParam String email,
            @RequestParam String token) {
        LOGGER.info("Received activation request for email: '{}'", email);
        subscriptionService.activateSubscription(sleekId, email, token);
        return new ResponseEntity<>("Subscription activated successfully!", HttpStatus.OK);
    }

    @PostMapping("/{sleekId}/login")
    public ResponseEntity<String> login(@PathVariable String sleekId, @RequestBody Map<String, String> requestBody) {
        String password = requestBody.get("password");
        LOGGER.info("Login attempt for subscription ID: '{}'", sleekId);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sleekId, password));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(sleekId);
            LOGGER.info("Login successful for subscription ID: '{}'", sleekId);
            return new ResponseEntity<>(jwtUtil.generateToken(userDetails), HttpStatus.OK);
        } catch (AuthenticationException ae) {
            LOGGER.warn("Invalid login attempt for subscription ID: '{}'", sleekId);
            return new ResponseEntity<>("Invalid subscription ID or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{sleekId}/details")
    public ResponseEntity<Map<String, String>> getSubscriptionDetails(@PathVariable String sleekId) {
        LOGGER.info("Fetching subscription details for ID: '{}'", sleekId);
        Subscription subscription = subscriptionService.findBysleekId(sleekId);

        if (subscription != null) {
            Map<String, String> response = new HashMap<>();
            response.put("sleekId", subscription.getSleekId());
            response.put("workspaceName", subscription.getWorkspaceName());
            response.put("email", subscription.getEmail());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{sleekId}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String sleekId) {
        LOGGER.info("Fetching profile image for subscription ID: '{}'", sleekId);
        Subscription subscription = subscriptionService.findBysleekId(sleekId);
        if (subscription != null && subscription.getProfileImage() != null) {
            LOGGER.info("Profile image fetched successfully for subscription ID: '{}'", sleekId);
            return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType(subscription.getProfileImageContentType()))
            .body(subscription.getProfileImage());
        } else {
            LOGGER.warn("No profile image found for subscription ID: '{}'", sleekId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{sleekId}")
    public ResponseEntity<String> updateSubscription(@PathVariable String sleekId,
            @RequestParam(value = "workspaceName", required = false) String workspaceName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        LOGGER.info("Update request for subscription ID: '{}'", sleekId);

        String profileImageContentType = null;
        byte[] imageBytes = null;

        // Handle profile image upload if provided
        if (profileImage != null) {
            profileImageContentType = profileImage.getContentType();

            // Validate that the image is either JPG or PNG
            if (profileImageContentType == null ||
                    (!profileImageContentType.equals(MediaType.IMAGE_JPEG_VALUE)
                            && !profileImageContentType.equals(MediaType.IMAGE_PNG_VALUE))) {
                LOGGER.warn("Invalid image format for subscription ID: '{}'. Only JPG and PNG are allowed.", sleekId);
                return new ResponseEntity<>("Only JPG and PNG images are allowed.", HttpStatus.BAD_REQUEST);
            }

            try {
                imageBytes = profileImage.getBytes();
            } catch (IOException e) {
                LOGGER.error("Failed to read profile image data for subscription ID: '{}'", sleekId, e);
                return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        boolean isUpdated = subscriptionService.updateSubscription(sleekId, workspaceName, email, password, imageBytes,
                profileImageContentType);

        if (isUpdated) {
            LOGGER.info("Subscription ID: '{}' updated successfully.", sleekId);
            return new ResponseEntity<>("Subscription updated successfully!", HttpStatus.OK);
        } else {
            LOGGER.warn("Failed to update subscription ID: '{}'. Subscription not found.", sleekId);
            return new ResponseEntity<>("Subscription not found.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{sleekId}/logout")
    public ResponseEntity<String> logout(@PathVariable String sleekId, @RequestBody Map<String, String> requestBody) {
        LOGGER.info("Logout attempt for subscription ID: '{}'", sleekId);
        String token = requestBody.get("token");

        if (token != null) {
            subscriptionService.invalidateToken(token); // Invalidate token
            LOGGER.info("Logout successful for sleekId: '{}'", sleekId);
            return new ResponseEntity<>("Logout successful.", HttpStatus.OK);
        } else {
            LOGGER.warn("Logout failed: token missing for sleekId: '{}'", sleekId);
            return new ResponseEntity<>("Token required.", HttpStatus.BAD_REQUEST);
        }
    }
}
