package org.alex323glo.its_simulator.model.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;

/**
 * Planet model.
 * Is a JPA Entity. Represents 'planets' SQL table.
 *
 * @author Alexey_O
 * @version 0.1
 */
@Entity
@Table(name = "planets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planet {

   @Id
   @GeneratedValue
   private Long id;

   @Column(nullable = false, unique = true)
   private String name;


   // Additional

   @Column(nullable = false)
   private Long positionX;

   @Column(nullable = false)
   private Long positionY;

}
