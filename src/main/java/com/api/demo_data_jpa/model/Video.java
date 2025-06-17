package com.api.demo_data_jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "VIDEO_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true) // Para garantir que o equals e hashCode considerem os campos da classe pai Resource
@PrimaryKeyJoinColumn(name = "video_id")
public class Video extends Resource{

    private int length;

/* Exemplo de Relacionamento Composição
    // É uma relação BIDIRECIONAL OneToOne entre Video e Resource (para ser bidirecional usa a propriedade mappedBy na classe Resource)
    // Video é o relacionamento principal (dono - possui o @JoinColumn), é o lado do dono da relação e o Resource é o lado inverso da relação (possui mappedBy).
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_video_resource_id"))
    private Resource resource;
*/

}

/*Anotação:
 * fetch = FetchType.LAZY:
 * - Significa que a entidade Resource não será carregada automaticamente quando a entidade Video for carregada. A Resource só será carregada quando for explicitamente acessada.
 * Isso é útil para evitar carregamento desnecessário de dados, especialmente se a Resource for grande ou se você não precisar dela imediatamente.
 * optional = false:
 * - Significa que a relação é obrigatória, ou seja, um Video deve sempre ter um Resource associado. Se você tentar salvar um Video sem um Resource, ocorrerá uma exceção.
 * Isso garante que a integridade referencial seja mantida, ou seja, um Video sempre deve ter um Resource associado.
 * 
 * @PrimaryKeyJoinColumn(name = "video_id"):
 *  - É utilizada para especificar que uma coluna de chave primária em uma entidade também serve como chave estrangeira para outra entidade.
 *  - A tabela resource_tbl tem uma chave primária "id" que também é chave estrangeira para a tabela video_tbs.
*/