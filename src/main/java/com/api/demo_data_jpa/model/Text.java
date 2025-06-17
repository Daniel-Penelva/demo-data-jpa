package com.api.demo_data_jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true) // Para garantir que o equals e hashCode considerem os campos da classe pai Resource
public class Text extends Resource{

    @Column(length = 500)
    private String content;

/* Exemplo de Relacionamento Composição
    // É uma relação BIDIRECIONAL OneToOne entre Text e Resource (para ser bidirecional usa a propriedade mappedBy na classe Resource) 
    // Text é o relacionamento principal (dono - possui o @JoinColumn), é o lado do dono da relação e o Resource é o lado inverso da relação (possui mappedBy).
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_text_resource_id"))
    private Resource resource;
*/
    
}

/*Anotação:
 * fetch = FetchType.LAZY:
 * - Significa que a entidade Resource não será carregada automaticamente quando a entidade Text for carregada. A Resource só será carregada quando for explicitamente acessada.
 * Isso é útil para evitar carregamento desnecessário de dados, especialmente se a Resource for grande ou se você não precisar dela imediatamente.
 * optional = false:
 * - Significa que a relação é obrigatória, ou seja, um Text deve sempre ter um Resource associado. Se você tentar salvar um Text sem um Resource, ocorrerá uma exceção.
 * Isso garante que a integridade referencial seja mantida, ou seja, um Text sempre deve ter um Resource associado.
 * 
 * @PrimaryKeyJoinColumn(name = "text_id"):
 *  - É utilizada para especificar que uma coluna de chave primária em uma entidade também serve como chave estrangeira para outra entidade.
 *  - A tabela resource_tbl tem uma chave primária "id" que também é chave estrangeira para a tabela text_tbs.
*/
