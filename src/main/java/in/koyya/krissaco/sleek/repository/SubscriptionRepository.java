package in.koyya.krissaco.sleek.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.koyya.krissaco.sleek.entity.Subscription;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    //Optional<Subscription> findBySubscriptionId(String subscriptionId);
    Optional<Subscription> findById(String sleekId);
}
