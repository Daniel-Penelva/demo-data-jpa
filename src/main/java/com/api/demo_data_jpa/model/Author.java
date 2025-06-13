package com.api.demo_data_jpa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

    // Bom Saber: A classe que possuir @JoinColumn e @JoinTable é o lado dono (Course). A classe que possuir o mappedBy é o lado inverso (Author).
    // O Course é o relacionamento principal, é o lado do dono da relação e o Author é o lado dependente da relação.
    // A relação é ManyToMany, então o author pode ter vários cursos e o curso pode ter vários autores.
    // Como se lê: Vários autores podem ter vários cursos.
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Course> courses;
    
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
 * 
 * @manyToMany: Define uma relação ManyToMany entre entidades.
 *      - mappedBy: Indica o lado inverso da relação. Vai ser usado na entidade que não é o dono da relação. Vai mapear a coleção de entidades inversas.
 * @JoinTable: Define a tabela de junção para a relação ManyToMany.
 *      - name: Nome da tabela de junção.
 *      - joinColumns: Define as colunas de junção para a entidade atual.
 *      - inverseJoinColumns: Define as colunas de junção para a entidade inversa.
 * @JoinColumn: Define uma coluna de junção para a relação.
 *      - name: Nome da coluna de junção.
 *      - referencedColumnName: Nome da coluna referenciada na entidade inversa.
 *      - nullable: Indica se a coluna de junção pode ser nula.
 *      - unique: Indica se a coluna de junção deve ser única.
 *      - insertable: Indica se a coluna de junção pode ser incluída em operações de inserção.
 *      - updatable: Indica se a coluna de junção pode ser atualizada.
 *      - foreignKey: Define a chave estrangeira para a coluna de junção.
 * @OrderBy: Define a ordem dos resultados para uma coleção.
 *      - value: Expressão de ordenação (por exemplo, "name ASC").
 * @OrderColumn: Define a coluna de ordenação para uma coleção.
 *      - name: Nome da coluna de ordenação.
 *      - nullable: Indica se a coluna de ordenação pode ser nula.
 * 
 * @OneToMany: Define uma relação OneToMany entre entidades. O "One" representa a entidade principal (One) e o "Many" representa a entidade dependente (Many).
 *      - mappedBy: Indica o lado inverso da relação. Vai ser usado na entidade que não é o dono da relação. Vai mapear a coleção de entidades inversas.
 * @ManyToOne: Define uma relação ManyToOne entre entidades. O "One" representa a entidade principal (One) e o "Many" representa a entidade dependente (Many).
 *      - fetch: Define o tipo de carregamento da relação (EAGER ou LAZY).
 *      - optional: Indica se a relação é opcional (pode ser nula).
 * @JoinColumn: Define uma coluna de junção para a relação.
 *      - name: Nome da coluna de junção.
 *      - referencedColumnName: Nome da coluna referenciada na entidade inversa.
 *      - nullable: Indica se a coluna de junção pode ser nula.
 *      - unique: Indica se a coluna de junção deve ser única.
 *      - insertable: Indica se a coluna de junção pode ser incluída em operações de inserção.
 *      - updatable: Indica se a coluna de junção pode ser atualizada.
 *      - foreignKey: Define a chave estrangeira para a coluna de junção.
 * 
 * @OneToOne: Define uma relação unidirecional ou bidirecional (utiliza o mappedBy) OneToOne entre duas entidades, onde uma entidade possui exatamente uma instância da outra.
 * @JoinColumn: Especifica a coluna que será usada como chave estrangeira na tabela da entidade que possui a relação.
 * @ForeignKey: Define a chave estrangeira com um nome específico, que pode ser útil para manter a integridade referencial no banco de dados.
 * 
 * @OnDelete: Define o comportamento de exclusão em cascata para a relação. Quando uma entidade é deletada, as entidades relacionadas também serão deletadas.
 *      - action: Define a ação a ser executada quando a entidade principal for deletada (ex: CASCADE, SET_NULL, etc.).
 *      - CASCADE: Quando a entidade principal for deletada, todas as entidades relacionadas também serão deletadas.
 *      - SET_NULL: Quando a entidade principal for deletada, a relação será nula.
 * 
 * fetch = FetchType.LAZY: Define o tipo de carregamento da relação. O LAZY significa que os dados serão carregados somente quando necessário, ou seja, quando a coleção for acessada.
 * fetch = FetchType.EAGER: Define o tipo de carregamento da relação. O EAGER significa que os dados serão carregados imediatamente, ou seja, quando a entidade for carregada, as coleções relacionadas também serão carregadas.
 * @JsonIgnore: Anotação do Jackson que indica que o campo deve ser ignorado durante a serialização e desserialização JSON. Isso é útil para evitar loops infinitos em relações bidirecionais.
 * @JsonProperty: Anotação do Jackson que indica que o campo deve ser serializado e desserializado com um nome específico no JSON. Isso é útil para personalizar o nome do campo no JSON.
 * optional = false: Indica que a relação é obrigatória, ou seja, não pode ser nula. (chave estrangeira obrigatória)
 * 
*/

/* Observação: 
 *
 * Vale para todos os relacionamentos: 
 *  - A classe que possuir @JoinColumn e @JoinTable é o lado dono. 
 *  - A classe que possuir o mappedBy é o lado inverso.
 * */  