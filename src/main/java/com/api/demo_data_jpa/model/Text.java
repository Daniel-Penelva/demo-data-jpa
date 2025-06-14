package com.api.demo_data_jpa.model;

import jakarta.persistence.Column;
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
@Table(name = "TEXT_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500)
    private String content;

    // É uma relação BIDIRECIONAL OneToOne entre Text e Resource (para ser bidirecional usa a propriedade mappedBy na classe Resource) 
    // Text é o relacionamento principal (dono - possui o @JoinColumn), é o lado do dono da relação e o Resource é o lado inverso da relação (possui mappedBy).
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_text_resource_id"))
    private Resource resource;
    
}

/*Anotação:
 * fetch = FetchType.LAZY:
 * - Significa que a entidade Resource não será carregada automaticamente quando a entidade Text for carregada. A Resource só será carregada quando for explicitamente acessada.
 * Isso é útil para evitar carregamento desnecessário de dados, especialmente se a Resource for grande ou se você não precisar dela imediatamente.
 * optional = false:
 * - Significa que a relação é obrigatória, ou seja, um Text deve sempre ter um Resource associado. Se você tentar salvar um Text sem um Resource, ocorrerá uma exceção.
 * Isso garante que a integridade referencial seja mantida, ou seja, um Text sempre deve ter um Resource associado.
*/
