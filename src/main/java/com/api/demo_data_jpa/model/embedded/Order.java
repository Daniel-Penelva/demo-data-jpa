package com.api.demo_data_jpa.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "ORDER_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @EmbeddedId
    private OrderId orderId;          // Chave primária composta

    @Embedded
    private Address address;          // Objeto incorporado

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "another_field")
    private String anotherField;
    
}

/*Anotação:
 * @Embeddable: 
 *   - Especifica uma classe cujas instâncias são armazenadas como parte intrínseca de uma entidade proprietária e compartilham a identidade da 
 *     entidade. Cada uma das propriedades ou campos persistentes do objeto incorporado é mapeada para a tabela de banco de dados da entidade.
 * 
 * @EmbeddedId:
 *   - Aplicado a um campo ou propriedade persistente de uma classe de entidade ou superclasse mapeada para denotar uma chave primária composta 
 *     que é uma classe incorporável. A classe incorporável deve ser anotada como Incorporável.
 *   - A anotação @EmbeddedId é usada para indicar que uma classe incorporável é usada como uma chave primária composta.
 *   - Deve haver apenas uma anotação EmbeddedId e nenhuma anotação Id quando a anotação EmbeddedId for usada.
 *   - Indica que a classe OrderId é uma classe que contém o ID da classe Order.
 * 
 * @Embedded:
 *   - Especifica um campo ou propriedade persistente de uma entidade cujo valor é uma instância de uma classe incorporável. A classe incorporável 
 *     deve ser anotada como Incorporável.
*/
