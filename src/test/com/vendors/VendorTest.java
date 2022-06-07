package test.com.vendors;

import main.com.vendors.app.lib.enums.Rank;
import main.com.vendors.app.models.Vendor;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VendorTest {
    private Vendor vendor;

    @BeforeEach
    void setUp() {
        vendor = new Vendor(30627091, "David", Rank.BRONCE, 500000);
    }

    @Test
    void assignNewRank() {
        // Arrange
        Rank rankBeforeAssign = vendor.getCurrentRank();

        // Act
        Rank newRank = Rank.COBRE;
        vendor.setCurrentRank(Rank.COBRE);

        Rank previousRank = vendor.getPreviousRank();
        Rank currentRank = vendor.getCurrentRank();

        // Assert
        Assertions.assertEquals(rankBeforeAssign, previousRank);
        Assertions.assertEquals(newRank, currentRank);
    }

    @Test
    void assignPersonalCommission() {
        // Arrange
        double personalCommission = 0.15;

        // Act
        vendor.assignPersonalCommission(); // of previous month
        double assignedPersonalCommission = vendor.getCommissionType("personal");

        // Assert
        Assertions.assertEquals(personalCommission, assignedPersonalCommission);
    }

    @Test
    void assignLevelUpCommission() {
        // Arrange
        Rank newRank = Rank.PLATA;
        vendor.setCurrentRank(newRank); // for example

        // Act
        Rank currentRank = vendor.getCurrentRank();
        vendor.assignLevelUpCommission();
        double levelUpCommission = 0.10;
        double assignedLevelUpCommission = vendor.getCommissionType("level up");

        // Assert
        Assertions.assertEquals(newRank, currentRank);
        Assertions.assertEquals(levelUpCommission, assignedLevelUpCommission);
    }

    @Test
    void assignLevelCommission() {
        // Arrange
        int childrenLevel = 2;
        double levelOneCommissionExpected = 0.01;
        double levelTwoCommissionExpected = 0.02;

        // Act
        vendor.assignLevelCommission(childrenLevel);
        double levelOneCommission = vendor.getCommissionType("level 1");
        double levelTwoCommission = vendor.getCommissionType("level 2");

        // Assert
        Assertions.assertEquals(levelOneCommissionExpected, levelOneCommission);
        Assertions.assertEquals(levelTwoCommissionExpected, levelTwoCommission);
    }

    @Test
    void calculateVendorCommission() {
        // Arrange
        vendor.setCurrentRank(Rank.PLATA);
        vendor.setCommissionType("personal", 0.2);
        vendor.setCommissionType("level up", 0.1);

        double sales = vendor.getSalesMonthly();
        double commissionExpected = (sales * 0.2) + (sales * 0.1);

        // Act
        double commission = vendor.getCommission();

        // Assert
        Assertions.assertEquals(commissionExpected, commission);
    }

    @Test
    void calculateCommissionWithLevels() {
        // Arrange
        vendor.setCurrentRank(Rank.ORO);
        vendor.setCommissionType("personal", 0.25);
        vendor.setCommissionType("level up", 0.15);
        vendor.setCommissionType("level 1", 0.01);
        vendor.setCommissionType("level 2", 0.02);
        vendor.setCommissionType("level 3", 0.03);

        double sales = vendor.getSalesMonthly();
        double salesLevelOne = 320000;
        double salesLevelTwo = 200000;
        double salesLevelThree = 400000;
        double levelCommission = (salesLevelOne * 0.01) + (salesLevelTwo * 0.02) + (salesLevelThree * 0.03);
        double commissionExpected = (sales * 0.25) + (sales * 0.15) + levelCommission;

        // Act
        vendor.addLevelCommission(levelCommission);
        double commission = vendor.getCommission();

        // Assert
        Assertions.assertEquals(commissionExpected, commission);
    }

    @Test
    void getCommissionDescription() {
        // Arrange
        vendor.setCurrentRank(Rank.PLATA);
        vendor.setCommissionType("personal", 0.2);
        vendor.setCommissionType("level up", 0.1);
        String descriptionExpected = "10% level up + 20% personal";

        // Act
        String description = vendor.getCommissionDescription();

        // Assert
        Assertions.assertEquals(descriptionExpected, description);
    }

    @Test
    void serializeProperties() {
        JSONObject vendorSerialized = vendor.toJSON();

        boolean hasAllProperties = false;

        if (
              vendorSerialized.has("name")
              && vendorSerialized.has("prevrank")
              && vendorSerialized.has("currank")
              && vendorSerialized.has("comision")
              && vendorSerialized.has("comisiondesc")
        ) {
            hasAllProperties = true;
        }

        Assertions.assertTrue(hasAllProperties);
    }
}
