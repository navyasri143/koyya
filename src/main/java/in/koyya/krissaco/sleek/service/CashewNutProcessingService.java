package in.koyya.krissaco.sleek.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.koyya.krissaco.sleek.entity.CashewNutProcessing;
import in.koyya.krissaco.sleek.repository.CashewNutProcessingRepository;

@Service
public class CashewNutProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(CashewNutProcessingService.class);

    @Autowired
    private CashewNutProcessingRepository repository;

    // Method to create or update processing data
    public CashewNutProcessing createOrUpdateProcessingData(CashewNutProcessing processingData) {
        logger.info("Received data: {}", processingData);

        if (processingData.isOverwrite()) {
            // Find existing data by sleekId and date
            List<CashewNutProcessing> existingDataList = repository.findBySleekIdAndDate(
                processingData.getSleekId(), processingData.getDate());
            if (!existingDataList.isEmpty()) {
                CashewNutProcessing existingData = existingDataList.get(0);
                updateExistingData(existingData, processingData);
                return repository.save(existingData);
            }
        }
        return repository.save(processingData);
    }

    // Method to get all processing data by sleekId
    public List<CashewNutProcessing> getAllProcessingData(String sleekId) {
        return repository.findBySleekId(sleekId);
    }

    // Method to get processing data by a specific date and sleekId
    public List<CashewNutProcessing> getProcessingDataByDate(String sleekId, Date date) {
        return repository.findBySleekIdAndDate(sleekId, date);
    }

    // Method to get processing data in a range of dates by sleekId
    public List<CashewNutProcessing> getProcessingDataInRange(String sleekId, Date startDate, Date endDate) {
        return repository.findBySleekIdAndDateBetween(sleekId, startDate, endDate);
    }

    // Helper method to update existing data selectively based on overwrite flags
    private void updateExistingData(CashewNutProcessing existingData, CashewNutProcessing newData) {
        if (newData.isBoilingSteamingInputOverwrite()) {
            existingData.setBoilingSteamingInput(newData.getBoilingSteamingInput());
            existingData.setBoilingSteamingInputUnit(newData.getBoilingSteamingInputUnit());
        }
        if (newData.isBoilingSteamingOutputOverwrite()) {
            existingData.setBoilingSteamingOutput(newData.getBoilingSteamingOutput());
            existingData.setBoilingSteamingOutputUnit(newData.getBoilingSteamingOutputUnit());
        }

        if (newData.isGradingInputOverwrite()) {
            existingData.setGradingInput(newData.getGradingInput());
            existingData.setGradingInputUnit(newData.getGradingInputUnit());
        }
        if (newData.isGradingOutputOverwrite()) {
            existingData.setGradingOutput(newData.getGradingOutput());
            existingData.setGradingOutputUnit(newData.getGradingOutputUnit());
        }

        if (newData.isCuttingInputOverwrite()) {  // Added Cutting logic
            existingData.setCuttingInput(newData.getCuttingInput());
            existingData.setCuttingInputUnit(newData.getCuttingInputUnit());
        }
        if (newData.isCuttingOutputOverwrite()) {  // Added Cutting logic
            existingData.setCuttingOutput(newData.getCuttingOutput());
            existingData.setCuttingOutputUnit(newData.getCuttingOutputUnit());
        }

        if (newData.isPrimaryShellingInputOverwrite()) {
            existingData.setPrimaryShellingInput(newData.getPrimaryShellingInput());
            existingData.setPrimaryShellingInputUnit(newData.getPrimaryShellingInputUnit());
        }
        if (newData.isPrimaryShellingOutputOverwrite()) {
            existingData.setPrimaryShellingOutput(newData.getPrimaryShellingOutput());
            existingData.setPrimaryShellingOutputUnit(newData.getPrimaryShellingOutputUnit());
        }

        if (newData.isSecondaryShellingInputOverwrite()) {
            existingData.setSecondaryShellingInput(newData.getSecondaryShellingInput());
            existingData.setSecondaryShellingInputUnit(newData.getSecondaryShellingInputUnit());
        }
        if (newData.isSecondaryShellingOutputOverwrite()) {
            existingData.setSecondaryShellingOutput(newData.getSecondaryShellingOutput());
            existingData.setSecondaryShellingOutputUnit(newData.getSecondaryShellingOutputUnit());
        }

        if (newData.isCoolingInputOverwrite()) {  // Renamed from Chilling to Cooling
            existingData.setCoolingInput(newData.getCoolingInput());
            existingData.setCoolingInputUnit(newData.getCoolingInputUnit());
        }
        if (newData.isCoolingOutputOverwrite()) {  // Renamed from Chilling to Cooling
            existingData.setCoolingOutput(newData.getCoolingOutput());
            existingData.setCoolingOutputUnit(newData.getCoolingOutputUnit());
        }

        if (newData.isBormaDryingInputOverwrite()) {
            existingData.setBormaDryingInput(newData.getBormaDryingInput());
            existingData.setBormaDryingInputUnit(newData.getBormaDryingInputUnit());
        }
        if (newData.isBormaDryingOutputOverwrite()) {
            existingData.setBormaDryingOutput(newData.getBormaDryingOutput());
            existingData.setBormaDryingOutputUnit(newData.getBormaDryingOutputUnit());
        }

        if (newData.isPeelingInputOverwrite()) {
            existingData.setPeelingInput(newData.getPeelingInput());
            existingData.setPeelingInputUnit(newData.getPeelingInputUnit());
        }
        if (newData.isPeelingOutputOverwrite()) {
            existingData.setPeelingOutput(newData.getPeelingOutput());
            existingData.setPeelingOutputUnit(newData.getPeelingOutputUnit());
        }

        if (newData.isSweatingInputOverwrite()) {
            existingData.setSweatingInput(newData.getSweatingInput());
            existingData.setSweatingInputUnit(newData.getSweatingInputUnit());
        }
        if (newData.isSweatingOutputOverwrite()) {
            existingData.setSweatingOutput(newData.getSweatingOutput());
            existingData.setSweatingOutputUnit(newData.getSweatingOutputUnit());
        }

        if (newData.isSortingInputOverwrite()) {
            existingData.setSortingInput(newData.getSortingInput());
            existingData.setSortingInputUnit(newData.getSortingInputUnit());
        }
        if (newData.isSortingOutputOverwrite()) {
            existingData.setSortingOutput(newData.getSortingOutput());
            existingData.setSortingOutputUnit(newData.getSortingOutputUnit());
        }

        if (newData.isPackagingInputOverwrite()) {
            existingData.setPackagingInput(newData.getPackagingInput());
            existingData.setPackagingInputUnit(newData.getPackagingInputUnit());
        }
        if (newData.isPackagingOutputOverwrite()) {
            existingData.setPackagingOutput(newData.getPackagingOutput());
            existingData.setPackagingOutputUnit(newData.getPackagingOutputUnit());
        }
    }
}
