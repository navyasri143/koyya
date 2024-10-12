package in.koyya.krissaco.sleek.config;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.koyya.krissaco.sleek.entity.Subscription;
import in.koyya.krissaco.sleek.repository.SubscriptionRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SubscriptionRepository repo;
    @Override
    public UserDetails loadUserByUsername(String sleekId) throws UsernameNotFoundException {
        Optional<Subscription> optional =  repo.findById(sleekId);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        Subscription subscription = optional.get();
        return new org.springframework.security.core.userdetails.User(subscription.getSleekId(), subscription.getPassword(), new ArrayList<>());
    }
}
