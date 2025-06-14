package com.api.demo_data_jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VIDEO_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int length;

    // É uma relação BIDIRECIONAL OneToOne entre Video e Resource (para ser bidirecional usa a propriedade mappedBy na classe Resource)
    // Video é o relacionamento principal (dono - possui o @JoinColumn), é o lado do dono da relação e o Resource é o lado inverso da relação (possui mappedBy).
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_video_resource_id"))
    private Resource resource;
    
}

/*Anotação:
 * fetch = FetchType.LAZY:
 * - Significa que a entidade Resource não será carregada automaticamente quando a entidade Video for carregada. A Resource só será carregada quando for explicitamente acessada.
 * Isso é útil para evitar carregamento desnecessário de dados, especialmente se a Resource for grande ou se você não precisar dela imediatamente.
 * optional = false:
 * - Significa que a relação é obrigatória, ou seja, um Video deve sempre ter um Resource associado. Se você tentar salvar um Video sem um Resource, ocorrerá uma exceção.
 * Isso garante que a integridade referencial seja mantida, ou seja, um Video sempre deve ter um Resource associado.
*/