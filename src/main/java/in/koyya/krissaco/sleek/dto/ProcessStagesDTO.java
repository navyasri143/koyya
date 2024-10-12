package in.koyya.krissaco.sleek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStagesDTO {

    // Updated DTO representing the stages with respective percentages
    private double boilingSteaming;
    private double grading;
    private double cutting;
    private double primaryShelling;
    private double secondaryShelling;
    private double bormaDrying;
    private double cooling;  // Renamed from chilling to cooling
    private double peeling;
    private double sweating;
    private double sorting;
    private double packaging;
}
