package com.api.demo_data_jpa.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    private String streetName;
    private String houseNumber;
    private String zipCode;
    
}

/*Anotação:
 * Não é uma classe de persistência.
 * @Embeddable: 
 *   - Especifica uma classe cujas instâncias são armazenadas como parte intrínseca de uma entidade proprietária e compartilham a identidade da 
 *     entidade. Cada uma das propriedades ou campos persistentes do objeto incorporado é mapeada para a tabela de banco de dados da entidade.
*/

