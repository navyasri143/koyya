package in.koyya.krissaco.sleek.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @Column(nullable = false, unique = true)
    private String sleekId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String workspaceName;

    @Column(nullable = false)
    private String password;  // Stores the encrypted password

    @Column(nullable = false)
    private boolean isActive = false;

    private LocalDateTime subscriptionStartDate;

    private LocalDateTime activationDate; // When the user activates the link

    @Column(nullable = false)
    private LocalDateTime activationDeadline; // Expiration date of the activation link

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] profileImage; // Field to store image data

    @Column(nullable = true)
    private String profileImageContentType; // Stores the content type (e.g., image/jpeg, image/png)

    @Column(nullable = true)
    private String token;
}
