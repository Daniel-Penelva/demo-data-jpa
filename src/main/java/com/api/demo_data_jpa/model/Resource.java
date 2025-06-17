package com.api.demo_data_jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "RESOURCE_TBL")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder 
public class Resource{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String name;

    private int size;
    private String url;

    // É uma relação BIDIRECIONAL OneToOne entre Resource e Lecture - para ser bidirecional usa a propriedade mappedBy na classe Lecture. Aqui, para acessar a Lecture, você pode acessar o Resource diretamente através da propriedade lecture ou acessar a Lecture através do Resource, pois a relação é bidirecional. 
    // Bom Saber: A classe que possuir @JoinColumn é o lado dono (Lecture). A classe que possuir o mappedBy é o lado inverso (Resource).
    // Lecture é o relacionamento principal, é o lado do dono da relação e o Resource é o lado inverso da relação.
    // mappedBy = "resource" aqui, o Resource apenas aponta para o campo resource da entidade Lecture, apenas para mapear a relação.
    // A relação é OneToOne, então um recurso pode pertencer a uma única palestra, e uma palestra pode ter um único recurso.
    // Como se lê: Um recurso pode pertencer a uma única palestra, e uma palestra pode ter um único recurso.
    @OneToOne(mappedBy = "resource", fetch = FetchType.LAZY)
    @JsonIgnore
    private Lecture lecture;


/* Exemplo de Relacionamento Composição
    // Resource é o relacionamento inverso, e Video é o relacionamento principal (dono), é o lado do dono da relação.
    @OneToOne(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Video video;

    // Resource é o relacionamento inverso, e File é o relacionamento principal (dono), é o lado do dono da relação.
    @OneToOne(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private File file;

    // Resource é o relacionamento inverso, e Text é o relacionamento principal (dono), é o lado do dono da relação.
    @OneToOne(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Text text;
*/
}

/*Anotação:
 * fetch = FetchType.LAZY: O JPA irá carregar os dados somente quando for necessário, caso contrário, irá carregar apenas o ID.
 *  - Isso é útil para evitar carregamento desnecessário de dados, especialmente se a entidade for grande ou se você não precisar dela imediatamente.
 * cascade = CascadeType.ALL:
 *  - Significa que todas as operações (persistência, atualização, remoção, etc.) realizadas na entidade Resource serão propagadas para a entidade relacionada (Lecture, Video, File, Text).
 * orphanRemoval = true:
 * - Significa que, se a entidade Resource for removida da relação, a entidade relacionada (Lecture, Video, File, Text) também será removida do banco de dados.
 * 
 * Herança: Se você tiver uma classe que herda de outra classe, você pode usar a anotação @Inheritance para definir como a herança será mapeada no banco de dados.
 * Existem três estratégias de herança:
 * @Inheritance(strategy = InheritanceType.SINGLE_TABLE):
 * - Significa que todas as entidades filhas (Video, File, Text) serão armazenadas em uma única tabela no banco de dados.
 * - Isso é útil para simplificar o modelo de dados e evitar a criação de várias tabelas para cada entidade filha.
 * 
 * @Inheritance(strategy = InheritanceType.JOINED):
 * - Significa que cada entidade filha (Video, File, Text) terá sua própria tabela no banco de dados, mas todas as tabelas terão uma chave estrangeira para a tabela Resource.
 * - Isso é útil para manter a integridade referencial e evitar duplicação de dados.
 * 
 * @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS):
 * - Significa que cada entidade filha (Video, File, Text) terá sua própria tabela no banco de dados, sem chave estrangeira para a tabela Resource.
 * - Isso é útil quando as entidades filhas não compartilham muitos atributos em comum e você deseja evitar a criação de uma tabela única para todas as entidades filhas.
 * 
 * @DiscriminatorColumn(name = "resource_type"):
 *  - Significa que a tabela Resource terá uma coluna adicional para armazenar o tipo da entidade filha (Video, File, Text).
 *  - Isso é útil para determinar o tipo da entidade filha a partir da tabela Resource.
 *  - O valor da coluna é o nome da classe da entidade filha (por exemplo, "Video", "File", "Text").
 * 
 * @DiscriminatorValue("V")
 *  - Significa que a coluna "resource_type" da tabela Resource terá o valor " V" para as entidades filhas do tipo Video.
 *  - Isso é útil para determinar o tipo da entidade filha a partir da tabela Resource.
 * 
 * 
*/

/* Observação: 
 *
 * Vale para todos os relacionamentos: 
 *  - A classe que possuir @JoinColumn e @JoinTable é o lado dono. 
 *  - A classe que possuir o mappedBy é o lado inverso.
 * */  
