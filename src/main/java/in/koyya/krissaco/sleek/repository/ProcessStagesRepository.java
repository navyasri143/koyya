package in.koyya.krissaco.sleek.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.koyya.krissaco.sleek.entity.ProcessStages;

public interface ProcessStagesRepository extends JpaRepository<ProcessStages, Long> {

    // Find process stages by sleekId
    Optional<ProcessStages> findBySleekId(String sleekId);
}
