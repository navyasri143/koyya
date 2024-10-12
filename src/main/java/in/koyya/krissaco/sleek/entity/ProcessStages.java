package in.koyya.krissaco.sleek.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sleekId;

    // Cashew nut processing stages percentages (default values)
    private double boilingSteaming = 5.0;
    private double grading = 2.0;
    private double cutting = 1.5; 
    private double primaryShelling = 1.5;
    private double secondaryShelling = 52.0;  // By-product
    private double bormaDrying = 5.0;
    private double cooling = 1.0;  // Renamed from chilling to cooling
    private double peeling = 3.0;
    private double sweating = 2.0;
    private double sorting = 2.0;
    private double packaging = 1.0;
}
