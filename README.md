## 🧩 REGRA UNIVERSAL:
  Regra vale para **todos os tipos de relacionamentos** (unidirecional ou bidirecional) no JPA/Hibernate.

> **A classe que possuir a anotação `@JoinColumn` ou `@joinTable` é o **dono** da relação.**
>
> **A classe que possuir a propriedade `mappedBy` é o **lado inverso** da relação (em relacionamentos bidirecionais).**

---

## 📚 RELACIONAMENTO UNIDIRECIONAL

### ✅ Somente um lado conhece o outro.

* Não existe `mappedBy`, pois só existe **um lado**.
* Esse lado **é sempre o dono da relação**.
* **A `@JoinColumn` aparece nesse lado.**

#### Exemplo:

```java
// Dono da relação Lecture (único lado) - A classe que possuir a anotação `@JoinColumn`
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    private Resource resource;
}
```

---

## 🔁 RELACIONAMENTO BIDIRECIONAL

### ✅ Ambos os lados conhecem um ao outro.

* Um lado é o **dono** (`@JoinColumn`) ou (`@JoinTable`).
* O outro lado é o **inverso** (`mappedBy = "campoDoOutroLado"`).

### Exemplo @OneToOne:

```java
// Dono da relação Lecture (possui @JoinColumn)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    private Resource resource;
}
```

```java
// Lado inverso Resource (usa mappedBy)
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "resource")
    private Lecture lecture;
}
```

---

## 🔄 Isso vale para:

| Tipo de relação | Pode ser bidirecional?  | Usa `mappedBy`? | Dono sempre tem `@JoinColumn`?   |
| --------------- | ----------------------- | --------------- | -------------------------------- |
| `@OneToOne`     | ✅ Sim                   | ✅ Sim           | ✅ Sim                            |
| `@OneToMany`    | ✅ Sim                   | ✅ Sim           | ✅ Sim (se unidirecional)         |
| `@ManyToOne`    | ✅ Sim (mas já é o dono) | ❌ Não           | ✅ Sim                            |
| `@ManyToMany`   | ✅ Sim                   | ✅ Sim           | ✅ Sim (quem define `@JoinTable`) |

---

## 🧠 Dica final

> **Sempre existe apenas um "dono" da relação — o lado que mapeia a chave estrangeira no banco.**
>
> Se for bidirecional, o outro lado apenas referencia usando `mappedBy`.

## ✅ Em relação **unidirecional**:

### 🔸 Apenas um lado conhece o outro.

Por exemplo:

```java
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    private Resource resource;
}
```

```java
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Nenhuma referência para Lecture aqui
}
```

---

## 🧭 Comportamento nesse caso:

* `Lecture` sabe quem é seu `Resource` (porque tem o campo `resource`).
* **`Resource` não tem nenhuma referência à `Lecture`** — ele "não sabe" que está associado a uma `Lecture`.
* Se tiver uma instância de `Resource`, **não é possível obter a `Lecture` associada diretamente**, a não ser que você faça uma consulta manual (ex: `lectureRepository.findByResource(...)`).

---

## 🔁 Já numa relação **bidirecional**:

Você declara no outro lado:

```java
public class Resource {
    @OneToOne(mappedBy = "resource")
    private Lecture lecture;
}
```

Agora sim, `Resource` conhece `Lecture` e o JPA consegue fazer a associação em ambos os sentidos.

---

## ✅ Conclusão

| Tipo de relação   | Pode acessar dos dois lados?      | Precisa de `mappedBy`?          |
| ----------------- | --------------------------------- | ------------------------------- |
| **Unidirecional** | ❌ Não (apenas do lado que mapeia) | ❌ Não usa `mappedBy`            |
| **Bidirecional**  | ✅ Sim (Lecture ⇄ Resource)        | ✅ O lado inverso usa `mappedBy` |

---

# Relacionamento Unidirecional de Um-Para-Um (OneToOne)

Uma **relação unidirecional** significa que **apenas uma entidade conhece a outra**.
Ou seja, só um lado tem uma referência para o outro.

👉 **Em um relacionamento `@OneToOne` unidirecional, não se usa `mappedBy`.**

---

### 🔍 Exemplo de relação `@OneToOne` unidirecional:

Imagine que uma `Palestra` tem um `Recurso`, mas o `Recurso` **não sabe** a quem pertence.

```java
// Dono da relação Lecture (único lado) - A classe que possuir a anotação `@JoinColumn`
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    private Resource resource;
}
```

```java
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Nenhuma referência para Lecture aqui
}
```

### ✅ Ponto-chave:

* **Não existe `mappedBy` porque só um lado da relação é mapeado.**
* **A chave estrangeira (`resource_id`) será criada na tabela `Lecture`**, porque essa é a entidade que possui a referência (`@JoinColumn`).

---

# Relacionamento Bidirecional Um-Para-Um (OneToOne)

---

## 🧠 Ponto chave: Deve usar `mappedBy`

Só usa `mappedBy` em **relações bidirecionais**, quando **ambas as entidades se referenciam mutuamente**.

### ✅ Análise da modelagem:

#### `Lecture` (lado **dono** da relação):

```java
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    private Resource resource;
}
```

* Define a **chave estrangeira** `resource_id` na tabela `lecture`.
* Isso quer dizer que a **tabela `lecture`** terá uma coluna `resource_id` referenciando `resource(id)`.
* Ou seja, cada `Lecture` está **vinculada a um único `Resource`**.

#### `Resource` (lado **inverso**):

```java
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @OneToOne(mappedBy = "resource")
    private Lecture lecture;
}
```

* Usa `mappedBy = "resource"` para indicar que o **mapeamento está sendo controlado** pelo campo `resource` na entidade `Lecture`.
* Não cria nova coluna no banco (evita FK duplicada).
* Permite que você acesse `lecture.getResource()` e `resource.getLecture()`.

---

### 🧠 Como o banco vai ficar:

* Tabela `lecture`:

  * Colunas: `id`, `resource_id` (chave estrangeira para `resource(id)`)

* Tabela `resource`:

  * Colunas: `id`

---

### 🔄 Navegação:

* `lecture.getResource()` → retorna o recurso relacionado.
* `resource.getLecture()` → retorna a lecture que contém esse recurso.

  **OBS.** Para acessar a Lecture, pode acessar o Resource diretamente através da propriedade lecture ou acessar a Lecture através do Resource, pois a relação é bidirecional. 
---

### ✅ Análise da chave estrangeira:

#### Com **apenas uma chave estrangeira** (no caso, `fk_lecture_resource_id` na tabela `lecture`), você consegue acessar **tanto o `Lecture` a partir do `Resource`** quanto o **`Resource` a partir do `Lecture`**, **desde que tenha a relação bidirecional mapeada corretamente no JPA**.

---

### 🔁 Como isso funciona:

Faz assim:

#### `Lecture` (lado dono):

```java
@OneToOne
@JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
private Resource resource;
```

#### `Resource` (lado inverso):

```java
@OneToOne(mappedBy = "resource")
private Lecture lecture;
```

---

### 📦 O que o JPA faz nos bastidores:

* **Apenas a tabela `lecture`** tem uma **foreign key** para `resource`.
* O JPA entende que a **entidade `Resource` está relacionada a `Lecture`** com base no atributo `mappedBy`.
* Quando você faz `resource.getLecture()`, o JPA faz uma **consulta reversa**, procurando uma `Lecture` onde `lecture.resource_id = resource.id`.

---

### 💡 Isso significa que:

* ✅ **Você só precisa de uma coluna FK no banco** (`lecture.resource_id`).
* ✅ **Você acessa os dois lados da relação** via JPA, graças ao mapeamento bidirecional.

---

### 🧪 Exemplo prático:

Se tiver isso em um controller ou serviço:

```java
Lecture lecture = lectureRepository.findById(1).get();
Resource res = lecture.getResource();

Lecture lectureFromResource = res.getLecture();
```

Tudo funciona sem precisar de uma segunda chave estrangeira, porque o JPA cuida dessa mágica via `mappedBy`.

---

### 🔒 Conclusão:

* ✅ Só com a `fk_lecture_resource_id` (FK em `lecture`) é possível acessar os dois lados.
* ✅ O segredo está no **mapeamento bidirecional do JPA** com `mappedBy`.

### ✅ Resumo:

| Tipo de relação   | `@JoinColumn` | `mappedBy` | Direção        |
| ----------------- | ------------- | ---------- | -------------- |
| **Unidirecional** | ✅ Sim         | ❌ Não      | Um lado só     |
| **Bidirecional**  | ✅ Sim         | ✅ Sim      | Ambos os lados |

---

# Relacionamento Um-Para-Muitos (@OneToMany) / Muitos-Para-Um (@ManyToOne)

---

## 🔁 Relação `@OneToMany` / `@ManyToOne` no JPA

### ✅ **Quem é o dono da relação?**

* **O lado com `@ManyToOne` e `@JoinColumn` é o **dono da relação**.**
* **O dono da relação é quem controla a `FK` no banco de dados.**
* O lado com `@OneToMany(mappedBy = "...")` é o **inverso da relação**.

---

### 🔍 Exemplo:
#### `Section` (lado **dono** da relação):
```java
// Dono da relação Section (único lado) - A classe que possuir a anotação `@JoinColumn`
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_section_course_id"))
    private Course course;
}
```

#### `Course` (lado **inverso** da relação):
```java
// Inverso da relação Course (lado invserso) - A classe que possuir a propriedade mappedBy
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "course")
    private List<Section> sections;
}
```

---

### 💡 Explicação:

| Classe    | Anotação                          | Papel no relacionamento                               |
| --------- | --------------------------------- | ----------------------------------------------------- |
| `Section` | `@ManyToOne`                      | 🔸 **Dona da relação**                                |
|           | `@JoinColumn`                     | FK `course_id` na tabela `section`                    |
| `Course`  | `@OneToMany(mappedBy = "course")` | 🔹 **Inverso da relação** (aponta para quem tem a FK) |

---

### 🧠 Sobre dependência:

* **Dependente no banco de dados** = quem tem a **FK** → `Section`.
* **Principal** = quem é referenciado → `Course`.

> Ou seja: a **tabela filha** (`section`) depende da **tabela pai** (`course`).

### 🧠 Resumo:
| Entidade  | Anotação Principal         | É dono da relação? | Tem a FK no banco? |
| --------- | -------------------------- | ------------------ | ------------------ |
| `Course`  | `@OneToMany(mappedBy)`     | ❌ Inverso          | ❌ Não              |
| `Section` | `@ManyToOne + @JoinColumn` | ✅ Sim              | ✅ Sim              |


---

# Relacionamento Bidirecional Muitos-Para-Muitos (ManyToMany)

Em uma relação `@ManyToMany`, **nenhum dos lados é tecnicamente "principal" ou "dependente" do ponto de vista do banco de dados**. Ambos são entidades independentes e a relação é **simétrica** — ou seja:

> 🔁 **Muitos para muitos**: vários registros de uma entidade estão associados a vários registros da outra.

---

### ✅ Mas no **JPA**, é **preciso escolher um lado como o "dono" da relação**.

Isso não significa que uma entidade é mais importante do que a outra, mas sim:

* Qual entidade **controla a criação e persistência da tabela de junção** no banco.
* Esse lado usará a anotação `@JoinTable`.

---

### 🔑 O lado **"dono"**:

* É o que **não** usa `mappedBy`.
* Define a tabela de junção com `@JoinTable(...)`.
* Faz a persistência da relação ManyToMany.

### 🔒 O lado **"inverso"**:

* Usa `mappedBy` apontando para o nome do atributo no lado dono.
* **Não deve** usar `@JoinTable`.

---

### 🧠 Exemplo prático:

Suponha:

* Um `Curso` pode ter vários `Autores`
* Um `Autor` pode estar em vários `Cursos`

#### `Course` (lado **dono** da relação):
```java
// 🔸 Dono da relação Course (único lado) - A classe que possuir a anotação `@JoinTable`
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @JoinTable(
        name = "courses_authors",
        joinColumns = { @JoinColumn(name = "course_id") },
        inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    List<Author> authors;
}
```

  - ✅ É o dono da relação

  - ✅ Define a @JoinTable

  - ✅ Cria e gerencia a tabela intermediária (courses_authors)

  - ✅ Define os nomes das colunas de chave estrangeira


#### `Author` (lado **inverso** da relação):
```java
// 🔹 Inverso da relação Author (lado inverso) - A classe que possui mappedBy
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "authors")
    private List<Course> courses;
}
```
  - ❌ Não é o dono
  - ✅ Usa mappedBy para apontar para o campo da classe Course
  - ❌ Não define @JoinTable nem @JoinColumn
  - ✅ Apenas reflete a relação

---

### ✅ Resumo:

| Papel na relação     | JPA (não banco)   | Anotação usada                  |
| -------------------- | ----------------- | ------------------------------- |
| Dono (responsável)   | Controla a junção | `@ManyToMany + @JoinTable(...)` |
| Inverso (dependente) | Só espelha        | `@ManyToMany(mappedBy = "...")` |

### 🧠 Resumo:

| Entidade | Dono da relação? | Usa @JoinTable? | Usa mappedBy? |
| -------- | ---------------- | --------------- | ------------- |
| `Course` | ✅ Sim            | ✅ Sim           | ❌ Não         |
| `Author` | ❌ Não            | ❌ Não           | ✅ Sim         |


### Observação:
  * O dono da relação é quem define a @JoinTable, mesmo em @ManyToMany.
  * A entidade com mappedBy é sempre o lado inverso.

> 🧭 **Dica**: escolha como "dono" o lado que **geralmente manipula** mais (quem inicia a criação da relação).

---
---

## 🧩 O que é `@OnDelete`?

A anotação `@OnDelete` é do **Hibernate** (não do JPA puro) e serve para delegar a **remoção em cascata ao banco de dados** por meio de `ON DELETE CASCADE`, em vez de o Hibernate fazer isso em memória com `orphanRemoval` ou `cascade`.

---

### 🔧 Sintaxe Básica

```java
@OnDelete(action = OnDeleteAction.CASCADE)
```

* Isso significa que, quando a **entidade pai for deletada**, o banco **automaticamente remove as entidades filhas** (em vez de Hibernate fazer isso com várias `DELETE` individuais).

---

## ✅ Análise Entidade por Entidade

### 🔹 `Course` → `Section` (OneToMany)
#### ✅ Entidade Course
```java
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "course")
    List<Section> sections;
}
```
#### ✅ Entidade Section
```java
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_section_course_id"))
    private Course course;
}
```

📌 **Solução recomendada:**

```java
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_section_course_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;
}
```

  ✔️ Ao deletar um `Course`, todas as `Sections` associadas serão removidas automaticamente.

  ✔️ Para usar `@OnDelete` é preciso adicioná-la no lado `@ManyToOne`, ou seja, em `Section`.

  ✔️ O `@OnDelete` precisa estar no lado que tem a `@JoinColumn` (ou seja, `@ManyToOne` ou `@OneToOne` dono).

  ✔️ Agora, **quando um `Course` for deletado**, o banco de dados automaticamente deletará as `Sections` relacionadas, sem precisar carregar os objetos na memória.

---

### 🔹 `Section` → `Lecture` (OneToMany)
#### ✅ Entidade Section
```java
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "section")
    private List<Lecture> lectures;
}
```
#### ✅ Entidade Lecture
```java
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "section_id", foreignKey = @ForeignKey(name = "fk_lecture_section_id"))
    private Section section;
}
```

📌 **Solução recomendada:**

```java
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "section_id", foreignKey = @ForeignKey(name = "fk_lecture_section_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Section section;
}
```

  ✔️ Ao deletar uma `Section`, todas as `Lectures` serão deletadas também.
  
  ✔️ O `@OnDelete` precisa estar no lado que tem a `@JoinColumn` (ou seja, `@ManyToOne` ou `@OneToOne` dono).

---

### 🔹 `Lecture` → `Resource` (OneToOne)
#### ✅ Entidade Lecture
```java
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    private Resource resource;
}
```

#### ✅ Entidade Resource
```java
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "resource")
    private Lecture lecture;
}
```

📌 **Situação especial:**

* `Lecture` é o **dono** da relação.
* Ao deletar uma `Lecture`, pode-se desejar que o `Resource` seja deletado também.
* Porém: ⚠️ isso **só faz sentido se o `Resource` não for compartilhado** com outras entidades.

📌 **Solução recomendada (se Resource for exclusivo da Lecture):**

```java
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Resource resource;
}
```
  ✔️ O `@OnDelete` precisa estar no lado que tem a `@JoinColumn` (ou seja, `@ManyToOne` ou `@OneToOne` dono).

---

### 🔹 `Course` ↔ `Author` (ManyToMany)
#### ✅ Entidade Course
```java
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @JoinTable(
        name = "courses_authors",
        joinColumns = { @JoinColumn(name = "course_id") },
        inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    List<Author> authors;
}
```

#### ✅ Entidade Author
```java
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(mappedBy = "authors")
    List<Course> courses;
}
```

📌 **Solução recomendada:**

```java
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToMany
    @JoinTable(
        name = "courses_authors",
        joinColumns = { @JoinColumn(name = "course_id") },
        inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Author> authors;
}
```

✔️ Ao deletar um `Course`, os registros da tabela de junção `courses_authors` serão removidos automaticamente.

✔️ Isso garante que **ao deletar um `Course`, os registros da tabela `courses_authors` relacionados também serão removidos automaticamente**.

⚠️ *Isso **não deleta os autores** do banco, apenas a **associação entre eles e os cursos**.*

---

## ❌ Onde **não** usar `@OnDelete`

### ⛔ Em listas do lado `@OneToMany` (como `sections`, `lectures`)

Esses lados são **mapeados inversamente**. O `@OnDelete` precisa estar no lado que tem a `@JoinColumn` (ou seja, `@ManyToOne` ou `@OneToOne` dono).

---

## ✅ Resumo Final com Sugestões

| Entidade  | Campo                     | Aplicar `@OnDelete`? | Justificativa                                                |
| --------- | ------------------------- | -------------------- | ------------------------------------------------------------ |
| `Section` | `course`                  | ✅ Sim                | Deletar Course → deleta Sections                             |
| `Lecture` | `section`                 | ✅ Sim                | Deletar Section → deleta Lectures                            |
| `Lecture` | `resource`                | ✅ Se exclusivo       | Deletar Lecture → deleta Resource (se não for compartilhado) |
| `Course`  | `authors` (`@ManyToMany`) | ✅ Sim                | Deletar Course → remove vínculos na tabela `courses_authors` |

---

## 🧪 Resumo Prático para as entidades

| Relacionamento             | Onde aplicar `@OnDelete`                   | Resultado                                                         |
| -------------------------- | ------------------------------------------ | ----------------------------------------------------------------- |
| `Course` → `Section`       | Em `Section.course`                        | Ao deletar um Course, as Sections são removidas                   |
| `Section` → `Lecture`      | Em `Lecture.section`                       | Ao deletar uma Section, as Lectures são removidas                 |
| `Lecture` → `Resource`     | Em `Lecture.resource`                      | Ao deletar uma Lecture, o Resource pode ser removido              |
| `Course` → `Author` (join) | Em `@ManyToMany` do Course com `@OnDelete` | Ao deletar um Course, as linhas da tabela de junção são removidas |

---

## ✅ Onde o `@OnDelete` pode ser usado?

| Situação      | Pode usar `@OnDelete`?                                                 |
| ------------- | ---------------------------------------------------------------------- |
| `@OneToMany`  | ❌ **Não diretamente** (precisa ser na entidade filha, no `@ManyToOne`) |
| `@ManyToOne`  | ✅ Sim                                                                  |
| `@OneToOne`   | ✅ Sim (lado dono)                                                      |
| `@ManyToMany` | ✅ Sim (na `@JoinTable`)                                                |

---

## 🚫 Cuidados com `@OnDelete`

* Ele depende do **banco de dados suportar `ON DELETE CASCADE`**.
* Só funciona com **Hibernate** (não é JPA padrão).
* Se você deletar via JPA e quiser que o Hibernate cuide disso com cascata em memória, você usaria `cascade = CascadeType.REMOVE`.

---
---

## 🚀 **1️⃣ `fetch = FetchType.LAZY`**

### 👉 O que é?

* O `fetch` controla **como o JPA/Hibernate carrega relacionamentos entre entidades**:

  * `LAZY` → Carrega **sob demanda** (só quando acessar o atributo).
  * `EAGER` → Carrega **imediatamente**, junto com a entidade principal.

✅ **Boas práticas:**

* Para `@OneToMany` e `@ManyToOne`: quase sempre preferível `LAZY` para performance (evita joins enormes).
* `@ManyToOne` é `EAGER` por padrão → portanto é comum mudar explicitamente para `LAZY`.

---

## 🚀 **2️⃣ `optional = false`**

### 👉 O que é?

* Usado em `@ManyToOne` ou `@OneToOne`.
* Diz que o relacionamento **não pode ser `null`** → ou seja, é **obrigatório** no banco de dados e na validação ORM.
* Cria no DDL: `NOT NULL`.

Exemplo:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
```

---

## 🚀 **3️⃣ `@JsonIgnore`**

### 👉 O que é?

* Anotação do **Jackson** (JSON serializer usado pelo Spring Boot).
* Evita **loops infinitos** de serialização (problema comum com relacionamentos bidirecionais).
* Garante que campos `LAZY` não explodam quando o Jackson tentar converter para JSON.
* O seu uso indica que o campo deve ser **ignorado** durante a serialização/deserialização JSON.

---

## ✅ **Aplicando nas ENTIDADES**

## 🔹1. `Course` ↔ `Author` (ManyToMany)
### ✅ **`Author`**

```java
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Course> courses;
}
```

### ✅ **`Course`**

```java
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "courses_authors",
        joinColumns = { @JoinColumn(name = "course_id") },
        inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    @JsonIgnore
    private List<Author> authors;
}
```

## 👉 Dúvida: **Pode usar `fetch = FetchType.LAZY` nos dois lados?**

**SIM!**
Em um relacionamento `@ManyToMany`, tanto o **lado dono** (`Course` neste caso) quanto o **lado inverso** (`Author`) podem (e normalmente DEVEM) ter `fetch = FetchType.LAZY`.

### Por quê?

* `@ManyToMany` é **EAGER** por padrão no JPA, o que pode gerar **joins gigantescos** e `N+1 selects`.
* Definindo `LAZY` nos dois lados, você carrega os autores **só quando quiser** (e vice-versa).

Assim:

```java
@ManyToMany(fetch = FetchType.LAZY)
```

em ambos os lados = **ótima prática** para performance.

---

## 👉 Dúvida: **Pode usar `@JsonIgnore` nos dois lados?**

**SIM!**
É **recomendado** para evitar **loop infinito** de serialização JSON:

➡️ Exemplo do problema:

* Você carrega um `Course` → JSON gera todos `Authors` → cada `Author` carrega todos `Courses` → que carregam todos `Authors`... **Loop infinito**!

➡️ Então, usando `@JsonIgnore` em ambos os lados:

```java
// Classe Author
@ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
@JsonIgnore
private List<Course> courses;

// Classe Course
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(
        name = "courses_authors",
        joinColumns = { @JoinColumn(name = "course_id") },
        inverseJoinColumns = { @JoinColumn(name = "author_id") }
)
@JsonIgnore
private List<Author> authors;
```

✅ Garante:

* Serialização controlada.
* Lazy não explode exceção.
* Você decide explicitamente o que expor no JSON usando DTOs ou projeções.

---

## 🔑 **Resumo prático para `@ManyToMany`:**

| Ponto         | Course                   | Author              |
| ------------- | ------------------------ | ------------------- |
| `fetch`       | `FetchType.LAZY`         | `FetchType.LAZY`    |
| `@JsonIgnore` | ✅                        | ✅                   |
| `JoinTable`   | Somente no lado **dono** | 🚫 (usa `mappedBy`) |

---

## 📌 **Regra de ouro**

* `JoinTable` = só no **lado dono** (nunca nos dois).
    * Course é o lado dono → define @JoinTable.
    * Author é o lado inverso → usa mappedBy = "authors".
* `fetch = LAZY` = melhor prática nos dois lados.
* `@JsonIgnore` = melhor prática nos dois lados (ou use DTOs).

## ✅ Explicação detalhada
| Item                              | Course                                    | Author                                  |
| --------------------------------- | ----------------------------------------- | --------------------------------------- |
| **Anotação**                      | `@ManyToMany` + `@JoinTable`              | `@ManyToMany(mappedBy = "authors")`     |
| **`fetch`**                       | `FetchType.LAZY`                          | `FetchType.LAZY`                        |
| **`@JsonIgnore`**                 | ✅                                         | ✅                                    |
| **JoinTable**                     | Cria a tabela de junção `courses_authors` | Não cria nada, só mapeia o lado inverso |
| **Propriedade de ligação**        | `List<Author> authors`                    | `List<Course> courses`                  |
| **Responsável pela persistência** | Course (dono)                             | Author (inverso)                        |

---
## 🔹2. `Course` → `Section` (OneToMany + ManyToOne)
### ✅ **`Course`**

```java
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Section> sections;
}
```

### ✅ **`Section`**

```java
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_section_course_id"))
    @JsonIgnore
    private Course course;
}
```

## ✅ **1️⃣ Quem é o lado dono e quem é o inverso?**

* `Section` é o **lado dono** → pois tem `@ManyToOne` com `@JoinColumn` para `Course`.
* `Course` é o **lado inverso** → pois tem `@OneToMany(mappedBy = "course")`.

## ✅ **2️⃣ Pode usar `fetch = FetchType.LAZY` em ambos?**

👉 **Sim!** E é até recomendado:

| Lado                        | Anotação                                                  | Default | É bom usar `LAZY`?                                                                        |
| --------------------------- | --------------------------------------------------------- | ------- | ----------------------------------------------------------------------------------------- |
| `@ManyToOne` (lado dono)    | `@ManyToOne(fetch = FetchType.LAZY)`                      | EAGER   | Sim, melhor trocar para `LAZY` para não carregar o `Course` sempre que pegar um `Section` |
| `@OneToMany` (lado inverso) | `@OneToMany(mappedBy = "course", fetch = FetchType.LAZY)` | LAZY    | Já é LAZY por padrão, mas deixar explícito é boa prática                                  |

✅ **Resumo**: No `@ManyToOne` **não é LAZY por padrão**, então é interessante colocar `fetch = FetchType.LAZY` explicitamente.


## ✅ **3️⃣ Pode usar `@JsonIgnore` em ambos?**

👉 **Sim!** É **altamente recomendado** quando expõe via API, para evitar:

* Recursão infinita (`Course` -> `Section` -> `Course` ...)
* Respostas JSON gigantes e confusas

Portanto, no `Section` faz:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_section_course_id"))
@JsonIgnore
private Course course;
```

E no `Course`:

```java
@OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
@JsonIgnore
private List<Section> sections;
```

## ✅ **4️⃣ Por que é seguro usar `LAZY` + `@JsonIgnore` dos dois lados?**

* `LAZY` → Banco de dados só carrega o que for explicitamente solicitado (eficiência!)
* `@JsonIgnore` → Evita loops quando converter para JSON na API REST.

## 🎓 **📌 Conclusão**

| Entidade  | Relação                     | `fetch`                                            | `@JsonIgnore` |
| --------- | --------------------------- | -------------------------------------------------- | ------------- |
| `Section` | `@ManyToOne` (lado dono)    | **LAZY (colocar explicitamente)**                  | **Sim**       |
| `Course`  | `@OneToMany` (lado inverso) | **LAZY (já é default, mas pode deixar explícito)** | **Sim**       |


✅ **Essa prática vale para praticamente todas as relações bidirecionais quando usa Spring REST + Hibernate.**

---

## 🔹3. `Lecture` → `Section` (OneToMany + ManyToOne)
### ✅ **`Lecture`**
```java
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", foreignKey = @ForeignKey(name = "fk_lecture_section_id"))
    @JsonIgnore
    private Section section;
}
```

### ✅ **`Section`**

```java
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Lecture> lectures;
}
```

---
## 🔹3. `Lecture` → `Resource` (OneToOne)
### ✅ **`Lecture`**

```java
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_lecture_resource_id"))
    private Resource resource;
}
```

### ✅ **`Resource`**

```java
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "resource", fetch = FetchType.LAZY)
    @JsonIgnore
    private Lecture lecture;
}
```

## ✅ **1️⃣ Revisão rápida do `OneToOne`**

No caso:

* **`Lecture`** tem `@OneToOne` com `@JoinColumn` → **lado dono**
* **`Resource`** tem `@OneToOne(mappedBy = "resource")` → **lado inverso**


## ✅ **2️⃣ Como funciona o `fetch` no `OneToOne`**

| Relação                    | Valor padrão | Melhor prática                                    |
| -------------------------- | ------------ | ------------------------------------------------- |
| `@OneToOne` (lado dono)    | `EAGER`      | Mudar para `LAZY` para não carregar sempre        |
| `@OneToOne` (lado inverso) | `EAGER`      | Também mudar para `LAZY` por clareza e eficiência |

➡️ No `OneToOne` **ambos são `EAGER` por padrão**, diferente do `OneToMany` que é `LAZY` no inverso. Por isso é **muito comum querer mudar os dois para `LAZY`**.


## ✅ **3️⃣ Usar `@JsonIgnore` nos dois lados**

**Sim, é muito recomendável!**

* Evita loop infinito na serialização JSON:

  * `Lecture` → `Resource` → `Lecture` ...
* Evita resposta JSON desnecessariamente grande.


## ✅ **4️⃣ Resumo**

| Lado                        | Relação               | `fetch`                  | `@JsonIgnore` |
| --------------------------- | --------------------- | ------------------------ | ------------- |
| **Lecture (lado dono)**     | `@OneToOne`           | `LAZY, optional = false` | ✔️            |
| **Resource (lado inverso)** | `@OneToOne(mappedBy)` | `LAZY`                   | ✔️            |

---

## 🚀 **Pronto!**

Assim:

* Você evita consultas automáticas pesadas (graças ao `LAZY`)
* Garante API limpa, sem loops (graças ao `@JsonIgnore`)
* Mantém a coerência do mapeamento bidirecional

---

## 📌 **Resumo prático**

✅ `fetch = FetchType.LAZY` → otimiza performance.
✅ `optional = false` → garante integridade (chave estrangeira obrigatória).
✅ `@JsonIgnore` → evita problemas de loops infinitos no JSON e lazy-loading que explode no Jackson.

---
---

## ✅ **1️⃣ O que é o `@JsonProperty`?**

* É uma anotação do Jackson (mesmo pacote do `@JsonIgnore`).
* **Serve para personalizar o nome de uma propriedade no JSON** ou garantir que ela seja incluída mesmo que a convenção de nome do Java seja diferente.
* Também pode ser usada em métodos `getter` e `setter` para forçar Jackson a serializar/desserializar mesmo quando não é automático.

**Exemplo:**

```java
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 35)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @JsonProperty("last_name")
    private String lastName;
}
```

➡️ Isso faz com que no JSON apareça como:

```json
{
  "first_name": "Daniel",
  "last_name": "Penelva"
}
```

mesmo que no Java o nome seja `firstName`.


## ✅ **2️⃣ Quando usar `@JsonProperty` faz sentido**

| Uso                                                         | Para quê?                                                                        |
| ----------------------------------------------------------- | -------------------------------------------------------------------------------- |
| ✅ **Renomear campos**                                       | Para seguir convenções de API (snake\_case, camelCase) sem mudar o nome no Java. |
| ✅ **Forçar inclusão/exclusão em getters/setters especiais** | Útil quando Jackson não consegue detectar automaticamente.                       |
| ✅ **Configurar ordem ou valor default**                     | Em casos mais avançados com serialização customizada.                            |



## ✅ **3️⃣ Diferença para `@JsonIgnore`**

| Anotação            | O que faz                                                      |
| ------------------- | -------------------------------------------------------------- |
| **`@JsonProperty`** | Diz: **“Inclua este campo no JSON, e chame ele assim”**        |
| **`@JsonIgnore`**   | Diz: **“Ignore este campo — não serialize e não deserialize”** |



## ✅ **4️⃣ Vale a pena usar `@JsonProperty`?**

Depende:

* 👉 Se **só quer evitar loop** de serialização, **`@JsonIgnore` é o suficiente e mais simples**.
* 👉 Se quer **API com nomes bonitos no JSON** (por exemplo `first_name` em vez de `firstName`), aí `@JsonProperty` é ótimo.
* 👉 Se usa `snake_case` no JSON mas camelCase no Java, é uma boa prática padronizar com `@JsonProperty`.


## 🎓 **Resumo**

✅ Use **`@JsonIgnore`**:

* Para **quebrar loops**
* Para **não expor informações sensíveis**
* Para **ocultar campos técnicos**

✅ Use **`@JsonProperty`**:

* Para **dar nomes mais amigáveis ou formatados**
* Para **ajustar a forma que o JSON é exposto**


## 🚀 **Dica final**

**Combinar os dois é comum**:

* `@JsonIgnore` em relações bidirecionais ou sensíveis.
* `@JsonProperty` em campos de valor (strings, números) para formatar o nome no JSON.


## 🎯 **Exemplo Prático**

Detalhando com um exemplo prático para fixar:


## ✅ **1️⃣ No backend (Java)**

```java
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("first_name") // 👉 isso força o JSON a usar snake_case
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    // getters e setters
}
```

Quando expõe esse Author numa API REST (ex: `/api/authors`), o JSON retornado será:

```json
{
  "id": 1,
  "first_name": "Daniel",
  "last_name": "Andrade"
}
```

## ✅ **2️⃣ No frontend (Angular) ou no Postman**

No Angular, se criar uma **interface** para esse modelo, **tem que seguir o mesmo nome do JSON**, porque o Angular vai fazer o `HttpClient` mapear **diretamente do JSON para o objeto**.

### **Correto:** no Angular, a interface deve ser assim:

```ts
export interface Author {
  id: number;
  first_name: string; // 👈 igual ao JSON!
  last_name: string;  // 👈 igual ao JSON!
}
```
### **Correto** no Postman, o JSON deve ser assim:
✅ No Postman (ou qualquer cliente HTTP), para criar ou atualizar um Author você deve enviar o corpo assim:

```json
{
  "first_name": "Daniel",
  "last_name": "Andrade",
  "email": "daniel@email.com",
  "age": 30
}
```

📌 Se você mandar com firstName (camelCase), o Spring vai ignorar ou pode não mapear corretamente — porque o Jackson (biblioteca de serialização) casa o nome do JSON com o nome especificado no @JsonProperty.

## ✅ **3️⃣ Se usar camelCase no Angular e snake\_case no JSON**

Nesse caso, se o JSON for `first_name` mas se quiser `firstName` na interface, aí tem que fazer o mapeamento manual — por exemplo, usando um método de transformação no serviço Angular.

Exemplo:

```ts
// JSON vem assim: { "first_name": "Daniel" }
// Você quer: { firstName: "Daniel" }

export interface Author {
  id: number;
  firstName: string;
  lastName: string;
}

getAuthors(): Observable<Author[]> {
  return this.http.get<any[]>('/api/authors').pipe(
    map(authors => authors.map(a => ({
      id: a.id,
      firstName: a.first_name,
      lastName: a.last_name
    })))
  );
}
```

## ✅ **4️⃣ Dica**

➡️ **Se não quer complicar o front-end**, o mais comum é:

* usar camelCase **tanto no backend quanto no frontend**
* assim, não precisa de `@JsonProperty` — Jackson já usa os nomes da propriedade Java como estão.

## 🚀 **Resumo**

✅ `@JsonProperty("first_name")` força o nome no JSON.
✅ O **nome do JSON = nome na interface** para funcionar direto sem mapeamento.
✅ Se quiser camelCase no front e snake\_case no JSON, tem que mapear manualmente.

































