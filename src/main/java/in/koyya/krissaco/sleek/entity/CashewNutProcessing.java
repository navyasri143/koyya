package in.koyya.krissaco.sleek.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashewNutProcessing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sleekId;  

    private Date date;

    // Boiling/Steaming fields
    private double boilingSteamingInput;
    private String boilingSteamingInputUnit;
    private double boilingSteamingOutput;
    private String boilingSteamingOutputUnit;
    private boolean boilingSteamingInputOverwrite;
    private boolean boilingSteamingOutputOverwrite;

    // Grading fields
    private double gradingInput;
    private String gradingInputUnit;
    private double gradingOutput;
    private String gradingOutputUnit;
    private boolean gradingInputOverwrite;
    private boolean gradingOutputOverwrite;

    // Cutting fields (added as per the new order)
    private double cuttingInput;
    private String cuttingInputUnit;
    private double cuttingOutput;
    private String cuttingOutputUnit;
    private boolean cuttingInputOverwrite;
    private boolean cuttingOutputOverwrite;

    // Primary Shelling fields
    private double primaryShellingInput;
    private String primaryShellingInputUnit;
    private double primaryShellingOutput;
    private String primaryShellingOutputUnit;
    private boolean primaryShellingInputOverwrite;
    private boolean primaryShellingOutputOverwrite;

    // Secondary Shelling fields 
    private double secondaryShellingInput;
    private String secondaryShellingInputUnit;
    private double secondaryShellingOutput;
    private String secondaryShellingOutputUnit;
    private boolean secondaryShellingInputOverwrite;
    private boolean secondaryShellingOutputOverwrite;

    // Borma Drying fields
    private double bormaDryingInput;
    private String bormaDryingInputUnit;
    private double bormaDryingOutput;
    private String bormaDryingOutputUnit;
    private boolean bormaDryingInputOverwrite;
    private boolean bormaDryingOutputOverwrite;

    // Cooling fields (renamed from Chilling)
    private double coolingInput;
    private String coolingInputUnit;
    private double coolingOutput;
    private String coolingOutputUnit;
    private boolean coolingInputOverwrite;
    private boolean coolingOutputOverwrite;

    // Peeling fields
    private double peelingInput;
    private String peelingInputUnit;
    private double peelingOutput;
    private String peelingOutputUnit;
    private boolean peelingInputOverwrite;
    private boolean peelingOutputOverwrite;

    // Sweating fields
    private double sweatingInput;
    private String sweatingInputUnit;
    private double sweatingOutput;
    private String sweatingOutputUnit;
    private boolean sweatingInputOverwrite;
    private boolean sweatingOutputOverwrite;

    // Sorting fields
    private double sortingInput;
    private String sortingInputUnit;
    private double sortingOutput;
    private String sortingOutputUnit;
    private boolean sortingInputOverwrite;
    private boolean sortingOutputOverwrite;

    // Packaging fields
    private double packagingInput;
    private String packagingInputUnit;
    private double packagingOutput;
    private String packagingOutputUnit;
    private boolean packagingInputOverwrite;
    private boolean packagingOutputOverwrite;

    private boolean overwrite;
}
