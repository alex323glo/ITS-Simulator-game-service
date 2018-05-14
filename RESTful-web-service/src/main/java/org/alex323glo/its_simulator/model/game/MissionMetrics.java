package org.alex323glo.its_simulator.model.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Container for Mission metrics.
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see Mission
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MissionMetrics {
    
    private String shipName;
    private Integer shipLevel;
    private Double shipSpeed;
    private Double shipMaxPayload;

    private Double actualPayload;

    private String startPlanetName;
    private Long startPositionX;
    private Long startPositionY;

    private String destinationPlanetName;
    private Long destinationPositionX;
    private Long destinationPositionY;

    private Double distance;
    private Long duration;

    public void refreshDistanceAndDuration(Double shipLevelCoefficient, Double timeCoefficientSeconds) {
        distance = calculateDistance(startPositionX, startPositionY, destinationPositionX, destinationPositionY);
        duration = calculateDuration(distance, shipSpeed, shipLevel, shipLevelCoefficient, timeCoefficientSeconds);
    }

    /**
     *
     * @param startX
     * @param startY
     * @param destinationX
     * @param destinationY
     * @return
     */
    public static Double calculateDistance(Long startX, Long startY, Long destinationX, Long destinationY) {
        if (startX == null || startY == null || destinationX == null || destinationY == null) {
            // TODO log ...
            // TODO throw Exception ...
            return null;
        }

        if (startX < 0 || startY < 0 || destinationX < 0 || destinationY < 0) {
            // TODO log ...
            // TODO throw Exception ...
            return null;
        }

        return Math.sqrt((destinationX - startX) * (destinationX - startX) +
                        (destinationY - startY) * (destinationY - startY));
    }

    /**
     *
     * @param distance
     * @param shipSpeed
     * @param shipLevel
     * @param shipLevelCoefficient
     * @param timeCoefficientSeconds
     * @return
     */
    public static Long calculateDuration(Double distance, Double shipSpeed, Integer shipLevel,
                                              Double shipLevelCoefficient, Double timeCoefficientSeconds) {
        if (distance == null || shipSpeed == null || shipLevel == null) {
            // TODO log ...
            // TODO throw Exception ...
            return null;
        }

        if (distance <= 0 || shipSpeed <= 0 || shipLevel < 1) {
            // TODO log ...
            // TODO throw Exception ...
            return null;
        }

        Double time = distance / (shipSpeed + (shipLevel * shipLevelCoefficient) );

        time *= timeCoefficientSeconds;

        return Math.round(time);
    }
}
