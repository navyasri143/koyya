package in.koyya.krissaco.sleek.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import in.koyya.krissaco.sleek.dto.SubscriptionRequest;
import in.koyya.krissaco.sleek.entity.Subscription;
import in.koyya.krissaco.sleek.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // In-memory invalidated tokens (for production, use Redis or a DB)
    private final Set<String> invalidatedTokens = new HashSet<>();

    
    // Method to register a new subscription
    public Subscription registerSubscription(SubscriptionRequest request) {
        LOGGER.info("Attempting to register a new subscription with email: '{}'", request.getEmail());

        if (subscriptionRepository.findById(request.getSleekId()).isPresent()) {
            LOGGER.error("Subscription ID '{}' already exists.", request.getSleekId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Subscription ID already exists.");
        }

        if (subscriptionRepository.findById(request.getEmail()).isPresent()) {
            LOGGER.error("Email '{}' is already registered.", request.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered.");
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        LOGGER.info("Password encrypted successfully for email: '{}'", request.getEmail());

        Subscription subscription = new Subscription();
        subscription.setWorkspaceName(request.getWorkspaceName());
        subscription.setEmail(request.getEmail());
        subscription.setSleekId(request.getSleekId());
        subscription.setPassword(encryptedPassword);
        subscription.setActivationDeadline(LocalDateTime.now().plusHours(24));
        subscription.setToken(generateToken(request.getSleekId()));

        subscription = subscriptionRepository.save(subscription);
        LOGGER.info("Subscription registered successfully for email: '{}'", request.getEmail());

        sendActivationEmail(subscription);

        return subscription;
    }

    // Method to send the activation email
    private void sendActivationEmail(Subscription subscription) {
        String activationToken = subscription.getToken();
        String activationLink = "http://localhost:8080/subscriptions/" + subscription.getSleekId() + "/activate?token=" + activationToken + "&email="
                + subscription.getEmail();
        String emailBody = "Please click the following link to activate your subscription: " + activationLink;

        emailService.sendActivationEmail(subscription.getEmail(), "Activate Your Krissaco Sleek Subscription",
                emailBody);
        LOGGER.info("Activation email sent to: '{}'", subscription.getEmail());
    }

    // Activate subscription
    @Transactional
    public void activateSubscription(String sleekId, String email, String token) {
        LOGGER.info("Attempting to activate subscription for email: '{}'", email);
        
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(sleekId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();

            if (LocalDateTime.now().isAfter(subscription.getActivationDeadline())) {
                LOGGER.warn("Activation link for email '{}' has expired.", email);
                subscription.setToken(null);
                subscriptionRepository.save(subscription);
                throw new ResponseStatusException(HttpStatus.GONE, "Activation link has expired.");
            }

            if (subscription.isActive()) {
                LOGGER.warn("Subscription for email '{}' is already active.", email);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Subscription is already active.");
            }

            LOGGER.info("Received token: " + token);
            LOGGER.info("Expected token: " + subscription.getToken());

            if (!token.equals(subscription.getToken())) {
                LOGGER.warn("Invalid token for email '{}'", email);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid token");
            }

            LocalDateTime currentDateTime = LocalDateTime.now();
            subscription.setActive(true);
            subscription.setActivationDate(currentDateTime);
            subscription.setSubscriptionStartDate(currentDateTime);
            subscription.setToken(null); // Clear token after activation
            subscriptionRepository.save(subscription);
            LOGGER.info("Subscription activated successfully for email: '{}'", email);
        } else {
            LOGGER.error("Subscription with email '{}' not found.", email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email.");
        }
    }

    // Find subscription by SleekId
    public Subscription findBysleekId(String sleekId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(sleekId);
        return optionalSubscription.orElse(null);
    }

    // Check if the user is new (i.e., not activated yet)
    public boolean isNewUser(String sleekId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(sleekId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            // Define a new user as someone who has not activated their subscription yet
            return !subscription.isActive() && subscription.getActivationDate() == null;
        }
        return false;
    }

    // Existing updateSubscription method
    public boolean updateSubscription(String sleekId, String workspaceName, String email, String password,
            byte[] profileImage, String profileImageContentType) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(sleekId);

        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();

            if (workspaceName != null && !workspaceName.isEmpty()) {
                subscription.setWorkspaceName(workspaceName);
            }

            if (email != null && !email.isEmpty()) {
                if (!email.equals(subscription.getEmail()) && subscriptionRepository.findById(email).isPresent()) {
                    LOGGER.error("Email '{}' is already registered.", email);
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered.");
                }
                subscription.setEmail(email);
            }

            if (password != null && !password.isEmpty()) {
                String encryptedPassword = passwordEncoder.encode(password);
                subscription.setPassword(encryptedPassword);
                LOGGER.info("Password updated and encrypted for subscription ID: '{}'", sleekId);
            }

            if (profileImage != null && profileImageContentType != null) {
                subscription.setProfileImage(profileImage);
                subscription.setProfileImageContentType(profileImageContentType);
                LOGGER.info("Profile image updated for subscription ID: '{}'", sleekId);
            }

            subscriptionRepository.save(subscription);
            return true;
        } else {
            return false;
        }
    }

    // Overloaded method to update using a Subscription object
    public boolean updateSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription); // Simply save the subscription object
        LOGGER.info("Subscription updated successfully for sleekId: '{}'", subscription.getSleekId());
        return true;
    }

    // Generate a unique token
    public String generateToken(String sleekId) {
        return UUID.randomUUID().toString();
    }

    // Invalidate the token (adds it to the invalidated list)
    public void invalidateToken(String token) {
        invalidatedTokens.add(token); // Store the invalidated token
        LOGGER.info("Token invalidated successfully.");
    }

    // Check if the token is invalid
    public boolean isTokenInvalid(String token) {
        return invalidatedTokens.contains(token); // Verify token against the invalidation list
    }

    // Check if the provided token is valid for the subscription
    public boolean isValidToken(String sleekId, String token) {
        Optional<Subscription> optional = subscriptionRepository.findById(sleekId);
        if (optional.isPresent()) {
            Subscription subscription = optional.get();
            return token.equals(subscription.getToken()) && !isTokenInvalid(token);
        }
        return false;
    }
}
