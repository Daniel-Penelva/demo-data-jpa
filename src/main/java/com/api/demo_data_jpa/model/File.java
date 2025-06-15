package com.api.demo_data_jpa.model;

import jakarta.persistence.DiscriminatorValue;
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
@DiscriminatorValue("F")  // Indica que a classe é uma subclasse de Resource e que o valor do campo "type" é "F".
public class File extends Resource{

    private String type;

/* Exemplo de Relacionamento Composição
    // É uma relação BIDIRECIONAL OneToOne entre File e Resource (para ser bidirecional usa a propriedade mappedBy na classe Resource)
    // File é o relacionamento principal (dono - possui o @JoinColumn), é o lado do dono da relação e o Resource é o lado inverso da relação (possui mappedBy).
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_file_resource_id"))
    private Resource resource;
*/
    
}

/*Anotação:
 * fetch = FetchType.LAZY:
 * - Significa que a entidade Resource não será carregada automaticamente quando a entidade File for carregada. A Resource só será carregada quando for explicitamente acessada.
 * Isso é útil para evitar carregamento desnecessário de dados, especialmente se a Resource for grande ou se você não precisar dela imediatamente.
 * optional = false:
 * - Significa que a relação é obrigatória, ou seja, um File deve sempre ter um Resource associado. Se você tentar salvar um File sem um Resource, ocorrerá uma exceção.
 * Isso garante que a integridade referencial seja mantida, ou seja, um File sempre deve ter um Resource associado.
*/