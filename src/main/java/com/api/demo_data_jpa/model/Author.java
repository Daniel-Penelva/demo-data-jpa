package com.api.demo_data_jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_sequence")
    @SequenceGenerator(
        name = "author_sequence",
        sequenceName = "author_sequence",
        allocationSize = 1
    )
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    
}


/*
 * Anotação:
 * @Entity: Indica que a classe é uma entidade JPA.
 * @Table: Especifica o nome da tabela no banco de dados.
 * @Id: Indica o campo que é a chave primária da entidade.
 * @GeneratedValue: Define a estratégia de geração de valores para a chave primária.
 *      - strategy = GenerationType.IDENTITY: A chave primária é gerada pelo banco de dados.
 *      - strategy = GenerationType.AUTO: A chave primária é gerada automaticamente pelo provedor JPA.
 *      - strategy = GenerationType.SEQUENCE: A chave primária é gerada usando uma sequência.
 *            - @SequenceGenerator: Define a sequência usada para gerar a chave primária.
 *                  - name: Nome da sequência.
 *                  - sequenceName: Nome da sequência no banco de dados.
 *                 - allocationSize: Tamanho do bloco de alocação para a sequência.
 *      - strategy = GenerationType.TABLE: A chave primária é gerada usando uma tabela auxiliar.
 * @Data: Gera automaticamente os métodos getters, setters, toString, equals e hashCode.
 *      - Inclui: @Getter, @Setter, @ToString, @EqualsAndHashCode e @RequiredArgsConstructor.
 * @AllArgsConstructor: Gera um construtor com todos os campos.
 * @NoArgsConstructor: Gera um construtor sem argumentos.
 * @RequiredArgsConstructor: Gera um construtor com campos finais obrigatórios.
 * @Getter: Método gerado automaticamente para obter o valor do campo.
 * @Setter: Método gerado automaticamente para definir o valor do campo.
 * @ToString: Método gerado automaticamente para representar a entidade como uma string.
 * @EqualsAndHashCode: Gera os métodos equals e hashCode com base nos campos da entidade.
 * @Builder: Permite a construção de objetos da entidade usando o padrão Builder.
 * @Column: Define propriedades adicionais para a coluna no banco de dados.
 *      - name: Nome da coluna no banco de dados.
 *      - nullable: Indica se a coluna pode ser nula.
 *      - unique: Indica se a coluna deve ser única.
 *      - length: Define o tamanho máximo da coluna (aplicável a campos String).
 *      - columnDefinition: Define a definição SQL da coluna.
 * @Lob: Indica que o campo deve ser tratado como um tipo de dado grande (LOB).
 * @Transient: Indica que o campo não deve ser persistido no banco de dados.
 * @Temporal: Especifica o tipo de data/hora para campos do tipo Date.
 *      - TemporalType.DATE: Representa apenas a data.
 * @JsonIgnoreProperties: Ignora propriedades durante a serialização/deserialização JSON.
 * @JsonProperty: Define o nome da propriedade JSON correspondente ao campo.
 * @JsonFormat: Especifica o formato de data/hora para serialização/deserialização JSON.
 * @JsonInclude: Define a inclusão de campos na serialização JSON.
 * @JsonValue: Indica que o método deve ser usado para serialização JSON.
 * @JsonCreator: Indica que o método deve ser usado para deserialização JSON.
 * @JsonIgnore: Indica que o campo deve ser ignorado durante a serialização/deserialização JSON.
 * @JsonPropertyOrder: Define a ordem das propriedades na serialização JSON.
*/