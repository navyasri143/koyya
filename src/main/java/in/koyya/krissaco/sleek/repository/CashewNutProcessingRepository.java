package in.koyya.krissaco.sleek.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.koyya.krissaco.sleek.entity.CashewNutProcessing;

@Repository
public interface CashewNutProcessingRepository extends JpaRepository<CashewNutProcessing, Long> {
    List<CashewNutProcessing> findBySleekId(String sleekId);
    List<CashewNutProcessing> findBySleekIdAndDate(String sleekId, Date date);
    List<CashewNutProcessing> findBySleekIdAndDateBetween(String sleekId, Date startDate, Date endDate);
}
