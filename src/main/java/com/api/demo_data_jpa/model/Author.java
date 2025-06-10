package com.api.demo_data_jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUTHOR_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 35)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private int age;
    
}


/*
 * Anotação:
 * @Entity: Indica que a classe é uma entidade JPA.
 * @Table: Especifica o nome da tabela no banco de dados.
 *      - name: Nome da tabela no banco de dados.
 *      - schema: Esquema do banco de dados onde a tabela está localizada.
 *      - catalog: Catálogo do banco de dados onde a tabela está localizada.
 *      - uniqueConstraints: Define restrições de unicidade para colunas na tabela.
 *      - indexes: Define índices para colunas na tabela.
 *      - indexes = @Index: Define um índice para uma coluna específica.
 * @Id: Indica o campo que é a chave primária da entidade.
 * @GeneratedValue: Define a estratégia de geração de valores para a chave primária.
 *      - strategy = GenerationType.IDENTITY: A chave primária é gerada pelo banco de dados.
 *      - strategy = GenerationType.AUTO: A chave primária é gerada automaticamente pelo provedor JPA.
 *      - strategy = GenerationType.SEQUENCE: A chave primária é gerada usando uma sequência.
 *            - @SequenceGenerator: Define a sequência usada para gerar a chave primária.
 *                  - name: Nome da sequência.
 *                  - sequenceName: Nome da sequência no banco de dados.
 *                  - allocationSize: Tamanho do bloco de alocação para a sequência.
 *      - strategy = GenerationType.TABLE: A chave primária é gerada usando uma tabela auxiliar.
 *            - @TableGenerator: Define a tabela usada para gerar a chave primária.
 *                  - name: Nome do gerador de tabela.
 *                  - table: Nome da tabela auxiliar.
 *                  - pkColumnName: Nome da coluna que armazena a chave primária.
 *                  - valueColumnName: Nome da coluna que armazena o valor da chave primária.
 *                  - allocationSize: Tamanho do bloco de alocação para a tabela.
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
 *      - insertable: Indica se a coluna pode ser incluída em operações de inserção.
 *      - updatable: Indica se a coluna pode ser atualizada.
 *      - precision: Define a precisão para campos numéricos.
 *      - scale: Define a escala para campos numéricos.
 *      - table: Especifica a tabela onde a coluna está localizada (se diferente da tabela da entidade).
 *      - uniqueConstraints: Define restrições de unicidade para a coluna.
 * @Basic: Define propriedades básicas para o campo.
 *      - fetch: Define o tipo de carregamento (EAGER ou LAZY).
 *      - optional: Indica se o campo é opcional.
 * @Access: Define o tipo de acesso aos campos (por exemplo, FIELD ou PROPERTY).
 * @AccessType: Define o tipo de acesso aos campos (por exemplo, FIELD ou PROPERTY).
 * @Enumerated: Especifica que o campo é um tipo enumerado.
 *      - EnumType.STRING: Armazena o valor do enum como uma string.
 *      - EnumType.ORDINAL: Armazena o valor do enum como um número inteiro (posição).
 * @Bits: Define que o campo deve ser tratado como um tipo de dado binário.
 *     - length: Define o tamanho do campo binário.
 * @BigDecimal: Define que o campo deve ser tratado como um tipo de dado decimal grande.
 * @BigInteger: Define que o campo deve ser tratado como um tipo de dado inteiro grande.
 * @Convert: Define um conversor personalizado para o campo.
 *      - converter: Classe que implementa a interface AttributeConverter.
 *      - autoApply: Indica se o conversor deve ser aplicado automaticamente.
 * @ConvertAttribute: Define um conversor personalizado para o campo.
 *      - converter: Classe que implementa a interface AttributeConverter.
 *      - autoApply: Indica se o conversor deve ser aplicado automaticamente.
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