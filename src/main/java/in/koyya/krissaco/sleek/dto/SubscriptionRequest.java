package in.koyya.krissaco.sleek.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class SubscriptionRequest {
    private String sleekId;
    private String workspaceName;
    private String email;
    private String password;
}
