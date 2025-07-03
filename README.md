# Relacionamento entre as classes
## 🧩 REGRA UNIVERSAL:
  Regra vale para **todos os tipos de relacionamentos** (unidirecional ou bidirecional) no JPA/Hibernate.

> **A classe que possuir a anotação `@JoinColumn` ou `@joinTable` é o **dono** da relação.**
>
> **A classe que possuir a propriedade `mappedBy` é o **lado inverso** da relação (em relacionamentos bidirecionais).**



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



## 🔄 Isso vale para:

| Tipo de relação | Pode ser bidirecional?  | Usa `mappedBy`? | Dono sempre tem `@JoinColumn`?   |
| --------------- | ----------------------- | --------------- | -------------------------------- |
| `@OneToOne`     | ✅ Sim                   | ✅ Sim           | ✅ Sim                            |
| `@OneToMany`    | ✅ Sim                   | ✅ Sim           | ✅ Sim (se unidirecional)         |
| `@ManyToOne`    | ✅ Sim (mas já é o dono) | ❌ Não           | ✅ Sim                            |
| `@ManyToMany`   | ✅ Sim                   | ✅ Sim           | ✅ Sim (quem define `@JoinTable`) |



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



## 🧭 Comportamento nesse caso:

* `Lecture` sabe quem é seu `Resource` (porque tem o campo `resource`).
* **`Resource` não tem nenhuma referência à `Lecture`** — ele "não sabe" que está associado a uma `Lecture`.
* Se tiver uma instância de `Resource`, **não é possível obter a `Lecture` associada diretamente**, a não ser que você faça uma consulta manual (ex: `lectureRepository.findByResource(...)`).



## 🔁 Já numa relação **bidirecional**:

Você declara no outro lado:

```java
public class Resource {
    @OneToOne(mappedBy = "resource")
    private Lecture lecture;
}
```

Agora sim, `Resource` conhece `Lecture` e o JPA consegue fazer a associação em ambos os sentidos.



## ✅ Conclusão

| Tipo de relação   | Pode acessar dos dois lados?      | Precisa de `mappedBy`?          |
| ----------------- | --------------------------------- | ------------------------------- |
| **Unidirecional** | ❌ Não (apenas do lado que mapeia) | ❌ Não usa `mappedBy`            |
| **Bidirecional**  | ✅ Sim (Lecture ⇄ Resource)        | ✅ O lado inverso usa `mappedBy` |



# Relacionamento Unidirecional de Um-Para-Um (OneToOne)

Uma **relação unidirecional** significa que **apenas uma entidade conhece a outra**.
Ou seja, só um lado tem uma referência para o outro.

👉 **Em um relacionamento `@OneToOne` unidirecional, não se usa `mappedBy`.**



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



# Relacionamento Bidirecional Um-Para-Um (OneToOne)



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



### 🧠 Como o banco vai ficar:

* Tabela `lecture`:

  * Colunas: `id`, `resource_id` (chave estrangeira para `resource(id)`)

* Tabela `resource`:

  * Colunas: `id`



### 🔄 Navegação:

* `lecture.getResource()` → retorna o recurso relacionado.
* `resource.getLecture()` → retorna a lecture que contém esse recurso.

  **OBS.** Para acessar a Lecture, pode acessar o Resource diretamente através da propriedade lecture ou acessar a Lecture através do Resource, pois a relação é bidirecional. 


### ✅ Análise da chave estrangeira:

#### Com **apenas uma chave estrangeira** (no caso, `fk_lecture_resource_id` na tabela `lecture`), você consegue acessar **tanto o `Lecture` a partir do `Resource`** quanto o **`Resource` a partir do `Lecture`**, **desde que tenha a relação bidirecional mapeada corretamente no JPA**.



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



### 📦 O que o JPA faz nos bastidores:

* **Apenas a tabela `lecture`** tem uma **foreign key** para `resource`.
* O JPA entende que a **entidade `Resource` está relacionada a `Lecture`** com base no atributo `mappedBy`.
* Quando você faz `resource.getLecture()`, o JPA faz uma **consulta reversa**, procurando uma `Lecture` onde `lecture.resource_id = resource.id`.



### 💡 Isso significa que:

* ✅ **Você só precisa de uma coluna FK no banco** (`lecture.resource_id`).
* ✅ **Você acessa os dois lados da relação** via JPA, graças ao mapeamento bidirecional.



### 🧪 Exemplo prático:

Se tiver isso em um controller ou serviço:

```java
Lecture lecture = lectureRepository.findById(1).get();
Resource res = lecture.getResource();

Lecture lectureFromResource = res.getLecture();
```

Tudo funciona sem precisar de uma segunda chave estrangeira, porque o JPA cuida dessa mágica via `mappedBy`.



### 🔒 Conclusão:

* ✅ Só com a `fk_lecture_resource_id` (FK em `lecture`) é possível acessar os dois lados.
* ✅ O segredo está no **mapeamento bidirecional do JPA** com `mappedBy`.

### ✅ Resumo:

| Tipo de relação   | `@JoinColumn` | `mappedBy` | Direção        |
| ----------------- | ------------- | ---------- | -------------- |
| **Unidirecional** | ✅ Sim         | ❌ Não      | Um lado só     |
| **Bidirecional**  | ✅ Sim         | ✅ Sim      | Ambos os lados |



# Relacionamento Um-Para-Muitos (@OneToMany) / Muitos-Para-Um (@ManyToOne)



## 🔁 Relação `@OneToMany` / `@ManyToOne` no JPA

### ✅ **Quem é o dono da relação?**

* **O lado com `@ManyToOne` e `@JoinColumn` é o **dono da relação**.**
* **O dono da relação é quem controla a `FK` no banco de dados.**
* O lado com `@OneToMany(mappedBy = "...")` é o **inverso da relação**.



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



### 💡 Explicação:

| Classe    | Anotação                          | Papel no relacionamento                               |
| --------- | --------------------------------- | ----------------------------------------------------- |
| `Section` | `@ManyToOne`                      | 🔸 **Dona da relação**                                |
|           | `@JoinColumn`                     | FK `course_id` na tabela `section`                    |
| `Course`  | `@OneToMany(mappedBy = "course")` | 🔹 **Inverso da relação** (aponta para quem tem a FK) |



### 🧠 Sobre dependência:

* **Dependente no banco de dados** = quem tem a **FK** → `Section`.
* **Principal** = quem é referenciado → `Course`.

> Ou seja: a **tabela filha** (`section`) depende da **tabela pai** (`course`).

### 🧠 Resumo:
| Entidade  | Anotação Principal         | É dono da relação? | Tem a FK no banco? |
| --------- | -------------------------- | ------------------ | ------------------ |
| `Course`  | `@OneToMany(mappedBy)`     | ❌ Inverso          | ❌ Não              |
| `Section` | `@ManyToOne + @JoinColumn` | ✅ Sim              | ✅ Sim              |




# Relacionamento Bidirecional Muitos-Para-Muitos (ManyToMany)

Em uma relação `@ManyToMany`, **nenhum dos lados é tecnicamente "principal" ou "dependente" do ponto de vista do banco de dados**. Ambos são entidades independentes e a relação é **simétrica** — ou seja:

> 🔁 **Muitos para muitos**: vários registros de uma entidade estão associados a vários registros da outra.



### ✅ Mas no **JPA**, é **preciso escolher um lado como o "dono" da relação**.

Isso não significa que uma entidade é mais importante do que a outra, mas sim:

* Qual entidade **controla a criação e persistência da tabela de junção** no banco.
* Esse lado usará a anotação `@JoinTable`.



### 🔑 O lado **"dono"**:

* É o que **não** usa `mappedBy`.
* Define a tabela de junção com `@JoinTable(...)`.
* Faz a persistência da relação ManyToMany.

### 🔒 O lado **"inverso"**:

* Usa `mappedBy` apontando para o nome do atributo no lado dono.
* **Não deve** usar `@JoinTable`.



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

## Digrama de Classe - Relacionamentos
<p align="center">
  <img src=".\src\main\resources\static\img\Example_Class_Diagram.png" alt="Diagrama de Classe Relacionamentos" width=800/>
</p>

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

# Comportamento de exclusão `@OnDelete`

## 🧩 O que é `@OnDelete`?

A anotação `@OnDelete` é do **Hibernate** (não do JPA puro) e serve para delegar a **remoção em cascata ao banco de dados** por meio de `ON DELETE CASCADE`, em vez de o Hibernate fazer isso em memória com `orphanRemoval` ou `cascade`.



### 🔧 Sintaxe Básica

```java
@OnDelete(action = OnDeleteAction.CASCADE)
```

* Isso significa que, quando a **entidade pai for deletada**, o banco **automaticamente remove as entidades filhas** (em vez de Hibernate fazer isso com várias `DELETE` individuais).



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



## ❌ Onde **não** usar `@OnDelete`

### ⛔ Em listas do lado `@OneToMany` (como `sections`, `lectures`)

Esses lados são **mapeados inversamente**. O `@OnDelete` precisa estar no lado que tem a `@JoinColumn` (ou seja, `@ManyToOne` ou `@OneToOne` dono).



## ✅ Resumo Final com Sugestões

| Entidade  | Campo                     | Aplicar `@OnDelete`? | Justificativa                                                |
| --------- | ------------------------- | -------------------- | ------------------------------------------------------------ |
| `Section` | `course`                  | ✅ Sim                | Deletar Course → deleta Sections                             |
| `Lecture` | `section`                 | ✅ Sim                | Deletar Section → deleta Lectures                            |
| `Lecture` | `resource`                | ✅ Se exclusivo       | Deletar Lecture → deleta Resource (se não for compartilhado) |
| `Course`  | `authors` (`@ManyToMany`) | ✅ Sim                | Deletar Course → remove vínculos na tabela `courses_authors` |



## 🧪 Resumo Prático para as entidades

| Relacionamento             | Onde aplicar `@OnDelete`                   | Resultado                                                         |
| -------------------------- | ------------------------------------------ | ----------------------------------------------------------------- |
| `Course` → `Section`       | Em `Section.course`                        | Ao deletar um Course, as Sections são removidas                   |
| `Section` → `Lecture`      | Em `Lecture.section`                       | Ao deletar uma Section, as Lectures são removidas                 |
| `Lecture` → `Resource`     | Em `Lecture.resource`                      | Ao deletar uma Lecture, o Resource pode ser removido              |
| `Course` → `Author` (join) | Em `@ManyToMany` do Course com `@OnDelete` | Ao deletar um Course, as linhas da tabela de junção são removidas |



## ✅ Onde o `@OnDelete` pode ser usado?

| Situação      | Pode usar `@OnDelete`?                                                 |
| ------------- | ---------------------------------------------------------------------- |
| `@OneToMany`  | ❌ **Não diretamente** (precisa ser na entidade filha, no `@ManyToOne`) |
| `@ManyToOne`  | ✅ Sim                                                                  |
| `@OneToOne`   | ✅ Sim (lado dono)                                                      |
| `@ManyToMany` | ✅ Sim (na `@JoinTable`)                                                |



## 🚫 Cuidados com `@OnDelete`

* Ele depende do **banco de dados suportar `ON DELETE CASCADE`**.
* Só funciona com **Hibernate** (não é JPA padrão).
* Se você deletar via JPA e quiser que o Hibernate cuide disso com cascata em memória, você usaria `cascade = CascadeType.REMOVE`.

---
---
# Tipo de carregamento (`fetch = FetchType.LAZY`) e Ignorar Serialização/Desserialização `@JsonIgnore`

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
# Anotação `@JsonProperty`

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

---
---

# Base Entity (Entidade Base) 
  👉 É uma classe que contém as propriedades comuns a todas as entidades do sistema. Ela é usada como base para as outras entidades, herdando suas propriedades e comportamentos.

  👉 O uso de uma BaseEntity para centralizar auditoria e identificadores comuns.

  👉 Entidade Base é chamado de entidade base, entidade abstrata, classe base, superclasse mapeada ou entidade pai — todos esses nomes são válidos, dependendo do contexto. 

## ✅ **Exemplo real com `BaseEntity`**

## 🎓 **Classe Base** **(`BaseEntity`)**

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate 
    private LocalDateTime createdAt; // Vai criar a data e a hora automaticamente quando o objeto for criado

    @LastModifiedDate
    private LocalDateTime lastModifiedAt; // Vai criar a data e na hora automaticamente quando o objeto for atualizado

    @CreatedBy
    private String createdBy; // Vai criar o usuário que criou o objeto

    @LastModifiedBy
    private String lastModifiedBy; // Vai criar o usuário que alterou o objeto
    
}
```

## 📌 **1️⃣ `@MappedSuperclass` — O que faz**

```java
@MappedSuperclass
```
    ✅ Indica que BaseEntity não é uma tabela própria, mas suas colunas serão herdadas por todas as entidades concretas que a estendem (Author, Course, etc).

    ➡️ Assim, nenhuma tabela base_entity é criada no banco — apenas as colunas id, createdAt, lastModifiedAt, createdBy,   lastModifiedBy aparecem diretamente nas tabelas filhas.

    ➡️ É uma **classe abstrata** que **não é uma entidade isolada**, ou seja, não gera tabela sozinha.

    ➡️ Ela serve para **fornecer campos comuns** e comportamentos para outras entidades concretas que a estendem.

    ➡️ Assim, todas as entidades filhas **herdam esses campos e seus mapeamentos JPA**, sem precisar duplicar código.


## 📌 **2️⃣ `@EntityListeners(AuditingEntityListener.class)` — Por que usar**

```java
@EntityListeners(AuditingEntityListener.class)
```

> ✅ Ativa o **Spring Data JPA Auditing**, permitindo que o Spring preencha **automaticamente** as anotações:

* `@CreatedDate` → data de criação
* `@LastModifiedDate` → data da última atualização
* `@CreatedBy` → usuário que criou
* `@LastModifiedBy` → usuário que alterou

Para funcionar, **não esqueça de ativar no projeto**:

```java
@SpringBootApplication
@EnableJpaAuditing
public class MyApplication { ... }
```

## 📌 **3️⃣ `@SuperBuilder` — Vantagem**

> ✅ O `@SuperBuilder` (do Lombok) permite usar o builder padrão **inclusive para classes que herdam de uma superclasse com builder**.

Assim, consegue:

```java
Author author = Author.builder()
    .firstName("Ana")
    .lastName("Silva")
    .email("ana@email.com")
    .age(30)
    .build();
```

E o `id` e auditoria são gerenciados pelo JPA — não precisa passar.


## 📌 **4️⃣ `@EqualsAndHashCode(callSuper = true)`**

> ✅ Garante que o `equals()` e `hashCode()` considerem também os campos da superclasse (`id`, auditoria).
> Sem isso, o Lombok gera só com os campos da própria classe filha.


## 📌 **5️⃣ Exemplo na prática — resultado**

Tabela `AUTHOR_TBL`:

| id | created\_at | last\_modified\_at | created\_by | last\_modified\_by | first\_name | last\_name | email | age |
| -- | ----------- | ------------------ | ----------- | ------------------ | ----------- | ---------- | ----- | --- |

✅ Nenhuma coluna duplicada.
✅ Controle de auditoria automático.
✅ Fácil de reaproveitar em todas as entidades do sistema.


## 📌 **6️⃣ Considerações de boas práticas**

✔️ **Use para todos os campos comuns**: `id`, `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `isActive` (se tiver soft delete).
✔️ Evite colocar campos de negócio na `BaseEntity`. Deixe apenas **infraestrutura de persistência**.
✔️ Use com DTOs se quiser expor datas formatadas ou mascarar `createdBy`.


## 📌 **7️⃣ Dica extra — para `createdBy` e `lastModifiedBy` funcionar**

Esses campos **precisam de um AuditorAware** para informar quem é o usuário atual:

```java
@Bean
public AuditorAware<String> auditorProvider() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
}
```

Assim o Spring sabe de onde pegar o usuário autenticado.


## ✅ **Resumo do que foi feito**

| Item                                       | O que faz                           |
| ------------------------------------------ | ----------------------------------- |
| `@MappedSuperclass`                        | Herança de colunas sem criar tabela |
| `@EntityListeners`                         | Ativa auditoria automática          |
| `@SuperBuilder`                            | Facilita `builder` com herança      |
| `@EqualsAndHashCode(callSuper = true)`     | Inclui superclasse no `equals`      |
| Campos `@CreatedDate`, `@LastModifiedDate` | Preenchidos pelo Spring             |
| Campos `@CreatedBy`, `@LastModifiedBy`     | Pegam usuário autenticado           |

---

## 🎓 **Usando nas Entidades**

Agora, é só estender a base **(`public class Author extends BaseEntity{...}`)**:

```java
@Entity
@Table(name = "AUTHOR_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Author extends BaseEntity{

    @Column(name = "first_name", nullable = false, length = 35)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @JsonProperty("last_name")
    private String lastName;

    // outros campos...
}
```
> ✅ OBSERVAÇÃO.  **Essa configuração vai ser o mesmo para todas as outras entidades.**

## 📌 **1️⃣ `@EqualsAndHashCode(callSuper = true)`**

> ✅ Garante que o `equals()` e `hashCode()` considerem também os campos da superclasse (`id`, auditoria).
> Sem isso, o Lombok gera só com os campos da própria classe filha.

## 🗝️ **O que isso faz na prática?**

✅ Todos os campos `id`, `createdAt`, `lastModifiedAt`, `createdBy`, `lastModifiedBy` **são mapeados automaticamente** em `Course`, `Author`, `Section`, etc.

✅ Você não precisa duplicar essas anotações.

✅ Se mudar a base, muda para todas.

## Digrama de Classe BaseEntity
<p align="center">
  <img src=".\src\main\resources\static\img\Example_BaseEntity_Class_Diagram.png" alt="Diagrama de Classe Relacionamento BaseEntity" width=800/>
</p>


## ✅ **Resumo**

| Item                      | Herança com `@MappedSuperclass`            |
| ------------------------- | ------------------------------------------ |
| Tabela para a superclasse | ❌ Não cria                                 |
| Campos herdados           | ✅ Sim                                      |
| Compartilha mapeamentos   | ✅ Sim                                      |
| Usa polimorfismo JPA      | ❌ Não, só herda campos                     |
| Usado para                | Campos comuns (audit, id, timestamps, etc) |


## 🔑 **Quando NÃO usar**

* Se precisar de polimorfismo JPA (consultar todas as filhas juntas), use `@Inheritance` e `@Entity` na base.
* `@MappedSuperclass` não gera uma tabela própria.

--- 

## ✅ **Propósito Real BaseEntity**
    > ✅ Proposito Real do BaseEntity quando ele inclui campos como `createdBy` e `lastModifiedBy`.


## 📌 **1️⃣ O `BaseEntity` é para controle genérico de auditoria**

* **Independente do Spring Security**, um `BaseEntity` com `id`, `createdAt` e `lastModifiedAt` já é **muito útil**:
  Ele sempre registra **quando foi criado** e **quando foi alterado**, mesmo que não haja login/autenticação.

  👉 **Esses campos (`createdAt` e `lastModifiedAt`) são 100% automáticos**, só exigem `@EnableJpaAuditing`.



## 📌 **2️⃣ Para `createdBy` e `lastModifiedBy`, é **opcional** usar Spring Security**

* Esses campos são **para registrar *quem* fez a ação**.
* Sozinhos, eles **não funcionam automaticamente** — precisam de uma fonte de usuário: por exemplo, `SecurityContextHolder` do Spring Security.
* Ou seja:

  * **Com Spring Security:** funciona lindamente, cada `save()` guarda o usuário logado.
  * **Sem autenticação:** você precisa definir um valor padrão (`"SYSTEM"` ou `"ANONYMOUS"`) ou omitir esses campos.


## 📌 **3️⃣ Resumindo**

| Campo            | Precisa de Spring Security? | Funciona sem autenticação?  |
| ---------------- | --------------------------- | --------------------------- |
| `id`             | ❌                           | ✅                           |
| `createdAt`      | ❌                           | ✅                           |
| `lastModifiedAt` | ❌                           | ✅                           |
| `createdBy`      | ✅ para valor real           | ⚠️ Precisa de um valor fixo |
| `lastModifiedBy` | ✅ para valor real           | ⚠️ Precisa de um valor fixo |



## 📌 **4️⃣ Então o `BaseEntity` é útil em qualquer cenário**

✅ **Para rastrear datas de criação/modificação — SEM segurança já é valioso.**
✅ **Se quiser rastrear *quem* fez, aí sim precisa do AuditorAware e (geralmente) do Spring Security.**



## 🎓 **5️⃣ Boas práticas**

✅ DICA: Se sua aplicação não tem autenticação agora, mas terá no futuro — **deixe os campos `createdBy` e `lastModifiedBy` preparados**, mas preencha com `"SYSTEM"` ou `"ADMIN"` via AuditorAware. Assim, a estrutura já fica correta.

✅ Se nunca vai usar autenticação — pode até omitir esses campos, ou mantê-los só para preencher valores genéricos.



## ✅ **Resumo final**

> 🗂️ **`BaseEntity` = reuso + rastreamento de auditoria + consistência.**
>
> 🔑 **`@CreatedBy` e `@LastModifiedBy` só fazem sentido com AuditorAware.**
>
> 🔒 **Para preencher com o usuário logado, geralmente é usado Spring Security.**

---
---

# Relacionamento - Composição

**Composição** significa que `Video`, `File` e `Text` **têm** um `Resource` — eles **usam** o recurso, mas não **são** o recurso.

Quando decide modelar Resource como entidade pai, e Video, File e Text como “extensões” de Resource (composição via relacionamento), isso é uma forma de associação estruturada. E sim:
  - Você representa isso com um @OneToOne entre Resource e cada subentidade.

## ➡️ **Usa-se quando:**

* As entidades não são naturalmente uma variação polimórfica.
* Aqui, prefere modularidade sem acoplamento de herança.
* Tem mais liberdade para evoluir os tipos sem restrição do supertipo.

## ➡️ **Como fica na prática (JPA):**

✅ **Classe Resource** - `relacionamento inverso (dependente)`
```java
@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private int size;
    private String url;

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
}
```

👉 `cascade = CascadeType.ALL:`
  - Significa que todas as operações (persistência, atualização, remoção, etc.) realizadas na entidade Resource serão propagadas para a entidade relacionada (Lecture, Video, File, Text).

👉 `orphanRemoval = true:`
  - Significa que, se a entidade Resource for removida da relação, a entidade relacionada (Lecture, Video, File, Text) também será removida do banco de dados.

👉 `fetch = FetchType.LAZY:`
  - O JPA irá carregar os dados somente quando for necessário, caso contrário, irá carregar apenas o ID.
  - Isso é útil para evitar carregamento desnecessário de dados, especialmente se a entidade for grande ou se você não precisar dela imediatamente.

✅ **Classe Video** - `relacionamento principal (dono)`
```java
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
```

✅ **Classe File** - `relacionamento principal (dono)`
```java
@Entity
@Table(name = "FILE_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    // É uma relação BIDIRECIONAL OneToOne entre File e Resource (para ser bidirecional usa a propriedade mappedBy na classe Resource)
    // File é o relacionamento principal (dono - possui o @JoinColumn), é o lado do dono da relação e o Resource é o lado inverso da relação (possui mappedBy).
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_file_resource_id"))
    private Resource resource;
    
}
```

✅ **Classe Text** - `relacionamento principal (dono)`
```java

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
```

  👉 Assim cada Video, File ou Text depende de um Resource.
  
  👉 Cada Resource pode ter no máximo um Video, um File e um Text — é restrito um para um.

➡️ **Resultado no banco:**

* Cada entidade tem sua própria tabela.
* Cada uma tem uma FK para a tabela `Resource`.

➡️ **Prós:**

* Mais flexível: `Resource` pode ser usado por outros tipos.
* Menos acoplamento.
* Mais controle de relacionamento.

➡️ **Contras:**

* Não tem polimorfismo: Não se faz `List<Resource>` para misturar todos.
* Precisa gerenciar as relações manualmente.

---

## Digrama de Classe - Relacionamento com Composição
<p align="center">
  <img src=".\src\main\resources\static\img\Example_Composition_Class_Diagram.png" alt="Diagrama de Classe Relacionamento Composição" width=800/>
</p>

---

## 📌 É realmente composição?
➡ Tecnicamente, isso é associação via chave estrangeira, mas modela o conceito de composição:

Video não existe sem Resource — logo, é dependente.

O ciclo de vida é gerenciado junto (CascadeType.ALL).

Logo: se o Resource é deletado, o Video (ou File ou Text) também é deletado.

Por isso chamamos de composição via modelagem relacional.


## ✅ Composição via Associação (@OneToOne)
🔹 Objetivo
Um Resource pode ter:

um Video com metadados específicos,

um File com metadados de arquivo,

um Text com metadados de texto.

Assim, ele pode ter um ou mais filhos ao mesmo tempo.


## 🎓 Relações — Composição (OneToOne)
**Relações:**
  - Resource tem um Video (1:1)
  - Resource tem um File (1:1)
  - Resource tem um Text (1:1)

Cada filho (Video, File, Text) tem uma FK para Resource.

## ✅ Conceitos sobre Composição via Associação (OneToOne) e Agregação via Associação (OneToOne)
### 📐 Símbolos UML para Composição, Agregação e Associação

| Relação                | Símbolo UML                                           | Significado                                                                        |
| ---------------------- | ----------------------------------------------------- | ---------------------------------------------------------------------------------- |
| **Associação simples** | Linha normal                                          | Apenas indica que uma classe conhece a outra                                       |
| **Agregação**          | Linha com **losango vazio** na extremidade            | Parte–todo fraco — o “todo” não possui completamente a “parte”                     |
| **Composição**         | Linha com **losango preto preenchido** na extremidade | Parte–todo forte — o “todo” possui totalmente a “parte” (ciclo de vida dependente) |


### ✅ Relacionamento de composição
Para a estrutura com Resource + Video, File, Text via OneToOne, em UML se for realmente composição, o correto é usar linha com losango preto na extremidade do todo, apontando para a parte.

**Por exemplo:**

```css
Resource <>─── Video
```

Aqui:

  - O losango preto fica no lado do Resource (todo)

  - A ponta da seta vai para Video (parte)

Assim para os outros:

```css
Resource <>─── File
Resource <>─── Text
```
Significa:

  - Resource é dono do ciclo de vida de Video, File ou Text.

  - Se o Resource for deletado, as partes também devem ser deletadas (ou seja, forte coesão).

## ⚡ Resumo visual

| Caso           | Símbolo              |
| -------------- | -------------------- |
| **Composição** | Losango preto (`◆`)  |
| **Agregação**  | Losango branco (`◇`) |
| **Associação** | Linha simples        |
| **Herança**    | Triângulo aberto     |

## Definindo - Relacionamento Composição X Relacionamento Agregação

### ✅ 1️⃣ Composição (Parte–Todo Forte)
#### 🔑 Definição:

  * A parte (ex: Video) não faz sentido existir sem o todo (Resource).

  * O ciclo de vida da parte é totalmente dependente do todo:

  * Se o Resource é deletado, o Video também é removido.

  * **Exemplo prático:**

      - Um coração dentro de um corpo humano: o coração não existe isolado, só dentro do corpo.

#### 📌 No meu caso:

  * Um Video é um detalhamento de um Resource.

  * Logo: Um Video sem Resource não existe → Composição.

---

### ✅ 2️⃣ Agregação (Parte–Todo Fraco)
#### 🔑 Definição:

  * A parte (ex: Video) pode existir independentemente do todo (Resource).

  * O ciclo de vida é separado: se o todo é removido, a parte pode continuar existindo.

  * **Exemplo prático:**

      - Uma turma (classe escolar) e seus alunos: os alunos continuam existindo mesmo que a turma seja desfeita.

#### 📌 Se Aplicado no meu caso:

  * Se modelar como agregação, significa:

      - Video pode ser um objeto independente reaproveitado por vários Resource ou até não ter nenhum Resource.

  * Ou seja: não há forte vínculo de posse.

### 🗝️ Resumo - Composição X Agregação

| Relação        | Vínculo     | Independência               |
| -------------- | ----------- | --------------------------- |
| **Composição** | Forte posse | Parte não existe sem o todo |
| **Agregação**  | Posse fraca | Parte pode existir sozinha  |

---

## 🧩 **Resumo prático - Herança X Composição**

| Aspecto       | **Herança**                            | **Composição**                        |
| ------------- | -------------------------------------- | ------------------------------------- |
| Estrutura     | Subclasses estendem a superclasse      | Entidades referenciam outra entidade  |
| Polimorfismo  | Sim                                    | Não                                   |
| Consulta      | Mais fácil para geral                  | Mais manual                           |
| Flexibilidade | Mais rígida                            | Mais flexível                         |
| Banco         | Pode ter JOINs e estrutura hierárquica | Tabelas independentes, ligadas por FK |


## 🗝️ **Quando usar cada um - Herança X Composição**

✅ **Herança:**

* Se `Video` é de fato um tipo de `Resource` — e precisa ser tratado genericamente como tal.

✅ **Composição:**

* Se `Resource` é um **recurso reutilizável** (um arquivo na nuvem, por exemplo) e `Video`, `File` e `Text` são **entidades que utilizam esse arquivo**, mas não precisam ser tratadas como o mesmo tipo no código.


## 🔑 **Dica**

No mundo real, para arquivos, **composição é mais comum**, pois:

* O recurso pode ser compartilhado por várias entidades (ex: um `Resource` pode ser linkado em vários contextos).
* Você separa bem o metadado (`Resource`) do uso específico (`Video` com legenda, `File` com tipo MIME).

---
---

# Relacionamento - Herança **`(@Inheritance)`**

  - A superclasse é a classe que herda atributos e métodos para as subclasses.
  - A subclasse é a classe que herda atributos e métodos da superclasse.
  - A subclasse pode ter atributos e métodos adicionais.
  - A subclasse pode ter atributos e métodos que sobrescrevem os da superclasse.
  - A subclasse pode ter atributos e métodos que não existem na superclasse.
  - No meu caso:
    - Herança significa que Video, File e Text são um Resource.
    - Video, File e Text herdam de Resource.

### ➡️ Usa-se quando:

  - As subclasses têm características em comum (atributos e métodos) que podem ser generalizados.

  - Você quer tratar todos os tipos de forma polimórfica. Ex: uma lista de Resource pode conter Video, File e Text.

### ➡️ Resultado no banco:

  - Resource tem colunas comuns.

  - Video tem colunas extras específicas.

  - Mesma coisa para File e Text.

### ➡️ Prós:

  - Reuso de atributos.

  - Consulta polimórfica (SELECT * FROM Resource traz todos).

  - Código limpo para coisas comuns.

---

## Digrama de Classe - Relacionamento com Herança
<p align="center">
  <img src=".\src\main\resources\static\img\Example_Inheritance_Class_Diagram.png" alt="Diagrama de Classe Relacionamento Herança" width=800/>
</p>

## 🗂️ Para Herança
  - Usa-se uma linha com um triângulo aberto na ponta, apontando para a superclasse:

```mathematica
Video ──▷ Resource  
File ──▷ Resource  
Text ──▷ Resource
```

## Tipos de estratégias de Herança

  * **1️⃣ `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`**

  * **2️⃣ `@Inheritance(strategy = InheritanceType.JOINED)`**

  * **3️⃣ `@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)`**

  * **@Inheritance - Define a estratégia de herança.**

### 1️⃣ @Inheritance(strategy = InheritanceType.SINGLE_TABLE)

  - Significa que todas as entidades filhas (subclasses: Video, File, Text) serão armazenadas em uma única tabela no banco de dados da superclasse (Resource).
    - Só existe uma tabela **`RESOURCE_TBL`** que guarda todos os campos de todas as subclasses.

  - O Hibernate cria uma **`coluna discriminadora (DTYPE por padrão)`** para saber o tipo da linha.

  - Cada entidade filha terá um campo/coluna chamado `type` que indica o tipo da entidade. **Ex:** Para representar a classe Filha **`Video`** pode usar o `type = 'V'`.

  - Para criar a coluna usa-se o `@DiscriminatorColumn(name = "type")`. **Ex:** `@DiscriminatorColumn(name = "resource_type")`.
    - Essa coluna `resource_type` é usada para identificar o tipo de entidade.

  - Para criar o valor do campo usa-se o `@DiscriminatorValue(value = "value")`. **Ex:** `@DiscriminatorValue(value = "V")`
    - Essa anotação indica que a classe `Video` é uma subclasse `Video` da superclasse `Resource`.
    - A subclasse `Video` vai ser identficada pelo valor `'V'` no campo `resource_type`.
    - O mesmo para as outras subclasses:
      - A subclasse `File` vai ser identficada pelo valor `'F'` no campo `resource_type`.
      - A subclasse `Text` vai ser identficada pelo valor `'T'` no campo `resource_type`.

**Exemplo de uso:**

<p align="center">
  <img src=".\src\main\resources\static\img\Database_inheritance_single_table.png" alt="Diagrama de Classe Relacionamento Herança" width=800/>
</p>

**Na prática - implementação classe pai e classes filhas**

✅ **`Superclasse Resource`- Superclass (classe pai)** 
```java
@Entity
@Table(name = "RESOURCE_TBL")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorColumn(name = "resource_type") 
public class Resource{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String name;

    private int size;
    private String url;
}
```
✅ **`Subclasse Video`- Subclass (classe filha)** 

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true) 
@DiscriminatorValue("V")
public class Video extends Resource{

    private int length;
}
```

✅ **`Subclasse File`- Subclass (classe filha)** 

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("F")
public class File extends Resource{

    private String type;
}
```

✅ **`Subclasse Text`- Subclass (classe filha)** 

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("T")
public class Text extends Resource{

    @Column(length = 500)
    private String content;
}
```

**Na prática - testando**

```java
@Component
public class InheritanceClassExample implements CommandLineRunner {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TextRepository textRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var video = Video.builder()
                .name("Video 1")
                .size(15)
                .url("video1.com")
                .length(5)
                .build();

        var file = File.builder()
                .name("File 1")
                .size(5)
                .url("file1.com")
                .type("png")
                .build();

        var text = Text.builder()
                .name("Text 1")
                .size(10)
                .url("text1.com")
                .content("Este é um arquivo de texto.")
                .build();

        videoRepository.save(video);
        fileRepository.save(file);
        textRepository.save(text);

        System.out.println("Nome do vídeo: " + video.getName() + " - Duração: " + video.getLength());
        System.out.println("Nome do Arquivo: " + file.getName() + " - Tipo: " + file.getType());
        System.out.println("Nome do Arquivo: " + text.getName() + " - Conteúdo: " + text.getContent());

    }

}
```

### 2️⃣ InheritanceType.JOINED

  👉 Cria uma tabela para a superclasse + uma para cada subclasse, ligadas por FK. Ou seja, cada subclasse tem uma tabela própria e uma tabela para a superclasse.

✅ Como funciona JOINED:

  - Superclass (Classe Pai)

    - RESOURCE_TBL: contém id + campos comuns.

  - Subclasses (Classe Filha)

    - VIDEO_TBL: contém id (FK para RESOURCE_TBL) + campos da Video (length).

    - FILE_TBL: contém id (FK para RESOURCE_TBL) + campos da File (type).

    - TEXT_TBL: contém id (FK para RESOURCE_TBL) + campos da Text (content).

➡️ É mais normalizado e evita colunas NULL que o SINGLE_TABLE pode ter.

✅ Anotação **`@PrimaryKeyJoinColumn(name = "video_id")`**

  - É utilizada para especificar que uma coluna de chave primária em uma entidade também serve como chave estrangeira para outra entidade.

  - Por exemplo, A tabela resource_tbl tem uma chave primária "id" que também é chave estrangeira para a tabela video_tbs.

<p align="center">
  <img src=".\src\main\resources\static\img\Database_exemploHeranca_joined.png" alt="Diagrama de Classe Relacionamento Herança" width=800/>
</p>

**Na prática - implementação classe pai e classes filhas**

✅ **`Superclasse Resource`- Superclass (classe pai)** 
```java
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
}
```
✅ **`Subclasse Video`- Subclass (classe filha)** 

```java
@Entity
@Table(name = "VIDEO_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "video_id")
public class Video extends Resource{

    private int length;
}
```

✅ **`Subclasse File`- Subclass (classe filha)** 

```java
@Entity
@Table(name = "FILE_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true) 
@PrimaryKeyJoinColumn(name = "file_id")
public class File extends Resource{

    private String type;
}
```

✅ **`Subclasse Text`- Subclass (classe filha)** 

```java
@Entity
@Table(name = "TEXT_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "text_id")
public class Text extends Resource{

    @Column(length = 500)
    private String content;
}
```

### 3️⃣ InheritanceType.TABLE_PER_CLASS

  👉 Cada subclasse gera sua tabela própria, com todos os campos herdados copiados (NÃO HÁ TABELA PARA A SUPERCLASSE).

✅ Como funciona TABLE_PER_CLASS:

  - NÃO existe tabela RESOURCE.

  - Hibernate cria VIDEO, FILE e TEXT, cada uma contendo id + campos comuns + campos específicos.

  - Consultas polimórficas podem ser mais lentas porque Hibernate faz UNION ALL para juntar tudo.

✅ Detalhe técnico na estratégia TABLE_PER_CLASS:

  - A estratégia `TABLE_PER_CLASS` **não funciona com** `GenerationType.IDENTITY`.

  - **Por que isso acontece?** 
    - Isso acontece porque, para TABLE_PER_CLASS, o Hibernate gera uma tabela separada para cada subclasse, e cada tabela precisa ter seu próprio mecanismo de geração de ID — mas o IDENTITY é baseado na tabela pai, que não existe nesse caso

    - **Solução:** Para `TABLE_PER_CLASS` se deve usar `GenerationType.TABLE` ou `GenerationType.SEQUENCE` **(recomendado para bancos que suportam SEQUENCE, como PostgreSQL)**. Esses métodos funcionam independente da tabela pai existir ou não.

```java 
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Integer id;
```
   - Isso cria uma tabela auxiliar de sequenciamento (hibernate_sequences ou algo do tipo) para gerar os IDs de todas as entidades.

<p align="center">
  <img src=".\src\main\resources\static\img\Database_exemploHeranca_table_per_class.png" alt="Diagrama de Classe Relacionamento Herança" width=800/>
</p>

**Na prática - implementação classe pai e classes filhas**

✅ **`Superclasse Resource`- Superclass (classe pai)** 
```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Resource{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(length = 100)
    private String name;

    private int size;
    private String url;
}
```
✅ **`Subclasse Video`- Subclass (classe filha)** 

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Video extends Resource{

    private int length;
}
```

✅ **`Subclasse File`- Subclass (classe filha)** 

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class File extends Resource{

    private String type;
}
```

✅ **`Subclasse Text`- Subclass (classe filha)** 

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Text extends Resource{

    @Column(length = 500)
    private String content;
}
```

---
---
# Polimorfismo no Hibernate

O **`@Polymorphism`** **não faz parte do JPA padrão**, mas é uma anotação **específica do Hibernate** para controlar como o Hibernate realiza **consultas polimórficas** quando usa herança — especialmente com `TABLE_PER_CLASS`.

## ✅ **👉 O que é polimorfismo no exemplo abaixo**

Neste caso:

```java
Resource r = new Video();
```

**Significa:**

* `Resource` é a **classe base**.
* `Video` é uma **subclasse**.
* Aqui, está usando uma **referência do tipo `Resource`** para apontar para um **objeto do tipo `Video`**.

Isto é **polimorfismo em Java puro**:
Você trata vários tipos derivados de forma genérica usando o tipo pai.


## ✅ **👉 E polimorfismo no Hibernate?**

**O Hibernate replica esse polimorfismo no banco:**

* Quando faz:

  ```java
  resourceRepository.findAll();
  ```

  Com `TABLE_PER_CLASS` e `@Polymorphism(IMPLICIT)`, o Hibernate executa um **UNION**:

  ```sql
  SELECT * FROM Video
  UNION
  SELECT * FROM File
  UNION
  SELECT * FROM Text;
  ```

  → E monta uma **lista de `Resource`**, que na prática contém objetos reais: `Video`, `File`, `Text`.


## ✅ **Resumo**

| Onde?                | O que é polimorfismo?                                                              |
| -------------------- | ---------------------------------------------------------------------------------- |
| **No Java**          | `Resource r = new Video();`                                                        |
| **No Hibernate/JPA** | Consultar `Resource` e receber instâncias de subclasses (`Video`, `File`, `Text`). |


## ✅ **Na prática**

Se fizer:

```java
Resource r = resourceRepository.findById(1).get();
System.out.println(r.getClass()); // 👈 Vai mostrar Video, File ou Text de verdade!
```

Mesmo que `r` seja do tipo `Resource`, o **tipo real na memória** é a subclasse correta.


## 📌 **O que é a anotação `@Polymorphism`**

No Hibernate, quando se usa herança (`SINGLE_TABLE`, `JOINED` ou `TABLE_PER_CLASS`), uma operação polimórfica (ex.: `select r from Resource r`) pode trazer **todas as subclasses** (Video, File, Text) ou apenas a tabela específica.

O `@Polymorphism` **instrui o Hibernate** se ele deve usar SQL polimórfico **ou não**.


## ✅ **Tipos**

| Valor                   | O que faz                                                                                                                                                                   |
| ----------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`IMPLICIT` (padrão)** | Consultas polimórficas consideram todas as subclasses automaticamente. Por exemplo, `select * from Resource` faz `UNION ALL` com as tabelas `Video`, `File`, `Text`.        |
| **`EXPLICIT`**          | Consultas polimórficas só funcionam **se usar `TREAT` ou consultas explícitas**, caso contrário, Hibernate consulta **somente a tabela específica** (não faz `UNION`). |



## 🎯 **Onde é útil?**

* No `TABLE_PER_CLASS`, o `IMPLICIT` força o Hibernate a fazer `UNION ALL` em todas as tabelas quando faz uma consulta na superclasse.

  * Isso pode ser pesado.
* O `EXPLICIT` diz: **não faça `UNION` sozinho**, só traga a tabela base, a menos que eu especifique explicitamente a subclasse.



## ✅ **Usando na prática**

Aplica-se na **superclasse**, por exemplo:

```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Polymorphism(type = PolymorphismType.EXPLICIT) // ou IMPLICIT (padrão)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    private String name;
    private int size;
    private String url;
}
```


## ⚡ **Como funciona na prática**

| Cenário                                    | `IMPLICIT`                                                | `EXPLICIT`                                                          |
| ------------------------------------------ | --------------------------------------------------------- | ------------------------------------------------------------------- |
| `select r from Resource r`                 | Faz `UNION` de todas as tabelas (`Video`, `File`, `Text`) | Consulta **só** `Resource` (não acha `Video` nem `File` nem `Text`) |
| `select v from Video v`                    | Consulta só `Video`                                       | Consulta só `Video`                                                 |
| `select treat(r as Video) from Resource r` | Consulta `Video` dentro de `Resource`                     | Consulta `Video` explicitamente                                     |



## ⚙️ **Quando usar**

✅ **IMPLICIT (padrão)**

* Conveniente: consultas polimórficas automáticas.
* Bom se você sempre quer ver todos os tipos.

✅ **EXPLICIT**

* Evita `UNION ALL` automático (que pode ser pesado com muitas tabelas).
* Você tem controle total para decidir **quando** fazer consultas polimórficas (via `TREAT` ou JOIN).



## 🔑 **Resumo para a minha estrutura**

**Para `TABLE_PER_CLASS`**, é comum deixar o `IMPLICIT` no começo:

```java
@Polymorphism(type = PolymorphismType.IMPLICIT) // ou omita, é o default
```

Se o meu projeto tiver **muitas subclasses** e performance virar problema, teste `EXPLICIT`:

```java
@Polymorphism(type = PolymorphismType.EXPLICIT)
```

E aí consulte `Video`, `File` e `Text` **diretamente**, ou use `TREAT`.


## ✅ **Exemplo com `EXPLICIT` + `TREAT`**

```java
// Vai buscar só Resource, NÃO faz UNION
select r from Resource r

// Vai buscar só Video dentro de Resource
select treat(r as Video) from Resource r
```

## Se especificar o `@Polymorphism` na subclasse?

### ✅ **Regra principal**

* O `@Polymorphism` só faz sentido na **classe que está no `FROM` da consulta**.
* Na prática, **o Hibernate só usa `@Polymorphism` da superclasse** quando resolve a consulta polimórfica.
* Se aplicar `@Polymorphism` **na subclasse**, Hibernate **ignora** — porque a subclasse **não é polimórfica** por si só. É apenas um nó da hierarquia.


### 📌 **Exemplo**

Supondo:

✅ **Classe Resource**
```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Polymorphism(type = PolymorphismType.EXPLICIT)
public class Resource { ... }
```

✅ **Classe Video**
```java
@Entity
@Polymorphism(type = PolymorphismType.IMPLICIT)  // <- colocado na subclasse
public class Video extends Resource { ... }
```

### 👉 E você faz:

```jpql
select r from Resource r
```

**Resultado:**
➡️ Vale o `@Polymorphism` da **classe `Resource`** → ou seja, `EXPLICIT`.
Então Hibernate **NÃO faz `UNION` automático**.

O `@Polymorphism` na `Video` **não afeta nada**, pois não é a entidade principal da consulta polimórfica.


### ✅ **Se consultar a subclasse diretamente:**

```jpql
select v from Video v
```

➡️ Aqui o Hibernate só faz `SELECT * FROM video_tbl`.
Não tem polimorfismo nesse caso: é uma tabela concreta só.


### 📌 **Resumo prático**

| Onde usar `@Polymorphism`? | Efeito                         |
| -------------------------- | ------------------------------ |
| **Superclasse da herança** | ✅ Controla `UNION` polimórfico |
| Subclasse isolada          | ❌ Não faz nada prático         |


### 🚀 **Regra de ouro**

> 🔑 **Usar `@Polymorphism` apenas na superclasse abstrata ou raiz da hierarquia.**

Assim você controla **como consultas como `select r from Resource r`** se comportam:

* `IMPLICIT` → faz `UNION` automático.
* `EXPLICIT` → não faz `UNION` automático.

## Testando dentro de um CommandLineRunner

✅ **Classe InheritanceClassExample**
```java
@Component
public class InheritanceClassExample implements CommandLineRunner {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var video = Video.builder()
                .name("Video 1")
                .size(15)
                .url("video1.com")
                .length(5)
                .build();

        var file = File.builder()
                .name("File 1")
                .size(5)
                .url("file1.com")
                .type("png")
                .build();

        var text = Text.builder()
                .name("Text 1")
                .size(10)
                .url("text1.com")
                .content("Este é um arquivo de texto.")
                .build();

        videoRepository.save(video);
        fileRepository.save(file);
        textRepository.save(text);

        System.out.println("Nome do vídeo: " + video.getName() + " - Duração: " + video.getLength());
        System.out.println("Nome do Arquivo: " + file.getName() + " - Tipo: " + file.getType());
        System.out.println("Nome do Arquivo: " + text.getName() + " - Conteúdo: " + text.getContent());

        
        // Exemplo utilizando Polimorfismo no Hibernate

        // 1) Consulta Polimórfica (EXPLICIT): Isso não traz as subclasses
        System.out.println("\n=== Consulta Resource ===");
        List<Resource> resources = resourceRepository.findAll();
        resources.forEach(System.out::println);  // Vai trazer somente Resource se existir - não traz Video, File e Text!


        // (2) Consulta explícita usando JPQL + TREAT
        System.out.println("\n=== Consulta Polimórfica usando TREAT - CONSULTA VIDEO ===");
        List<Video> videos = entityManager.createQuery(
                "SELECT TREAT(r AS Video) FROM Resource r WHERE TYPE(r) = Video",
                Video.class
        ).getResultList();

        videos.forEach(System.out::println);


        // (3) Consulta explícita de teste com instanceof
        System.out.println("\n=== Consulta Polimórfica usando INSTANCEOF ===");
        List<Resource> resources2 = resourceRepository.findAll();
        
        for(Resource resource : resources2) {
                System.out.println("Id: " + resource.getId() + " | Name: " + resource.getName());

                // Polimorfismo qual é o tipo real?
                if (resource instanceof Video) {
                        System.out.println("É um vídeo! Length: " + ((Video) resource).getLength());  // Utilizando Cast Video para acessar o método getLength()
                } else if (resource instanceof File) {
                        System.out.println("É um File! Type: " + ((File) resource).getType());  // Utilizando Cast File para acessar o método getType()
                } else if (resource instanceof Text) {
                        System.out.println("É um Text! Content: " + ((Text) resource).getContent());  // Utilizando Text para acessar o método getContent()
                } else {
                        System.out.println("Tipo desconhecido!");
                }
                System.out.println("-----------------------------------");
        }
    }
}
```

### ✅ O que este exemplo mostra

| Parte                          | O que demonstra                                           |
| ------------------------------ | --------------------------------------------------------- |
| `videoRepository.save` etc     | Insere **cada tipo na sua tabela separada**               |
| `resourceRepository.findAll()` | **NÃO traz `Video` nem `File`** (por causa de `EXPLICIT`) |
| `JPQL TREAT`                   | Consulta polimórfica explicitamente                       |

### 🔑 Resumo
✅ Com @Polymorphism(EXPLICIT):

  - O Hibernate não faz UNION automático na superclasse.

  - Você precisa ser explícito: usar TREAT ou consultar direto Video, File, Text.

## Supondo Superclasse agora com implicit

✅ **Classe InheritanceClassExample**
```java
@Component
public class InheritanceClassExample implements CommandLineRunner {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var video = Video.builder()
                .name("Video 1")
                .size(15)
                .url("video1.com")
                .length(5)
                .build();

        var file = File.builder()
                .name("File 1")
                .size(5)
                .url("file1.com")
                .type("png")
                .build();

        var text = Text.builder()
                .name("Text 1")
                .size(10)
                .url("text1.com")
                .content("Este é um arquivo de texto.")
                .build();

        videoRepository.save(video);
        fileRepository.save(file);
        textRepository.save(text);

        System.out.println("Nome do vídeo: " + video.getName() + " - Duração: " + video.getLength());
        System.out.println("Nome do Arquivo: " + file.getName() + " - Tipo: " + file.getType());
        System.out.println("Nome do Arquivo: " + text.getName() + " - Conteúdo: " + text.getContent());

        
        // Exemplo utilizando Polimorfismo no Hibernate

        // AGORA O PONTO-CHAVE: buscar polimorficamente direto do Resource!
        System.out.println("\n=== Consulta Resource ===");
        List<Resource> resources = resourceRepository.findAll(); // COM IMPLICIT, faz UNION AUTOMÁTICO!
        resources.forEach(System.out::println);

        // Consulta específica ainda funciona
        System.out.println("\n=== Consulta só Videos ===");
        List<Video> videos = videoRepository.findAll();
        videos.forEach(System.out::println);

    }
}
```

### 📌 O que muda aqui

| IMPLICIT                                                                           | EXPLICIT                                                                |
| ---------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| **`resourceRepository.findAll()` faz `UNION` automático** de `Video + File + Text` | `resourceRepository.findAll()` NÃO faz `UNION` (só busca Resource puro) |
| Não precisa `TREAT` para usar polimorfismo                                         | Precisa `TREAT` para buscar filhos                                      |
| Mais prático para queries genéricas                                                | Mais controle para queries complexas                                    |


## A anotação `@Polymorphism` só é específica para estratégia de herança `TABLE_PER_CLASS`?

### ✅ **1️⃣ A anotação `@Polymorphism` é específica do Hibernate**

* **`@Polymorphism` NÃO faz parte do JPA padrão** — é uma extensão do Hibernate.
* Serve para **controlar como o Hibernate executa consultas polimórficas** (ou seja, consultas na superclasse que retornam instâncias de subclasses).


### ✅ **2️⃣ Só faz diferença prática em `TABLE_PER_CLASS`**

#### ➤ **Por que?**

* Em `SINGLE_TABLE`:

  * Todas as subclasses estão na **mesma tabela única**, então o polimorfismo é **implícito** e natural — Hibernate só filtra pela coluna discriminadora (`DTYPE`).
  * Não faz sentido controlar `UNION` porque não existe mais de uma tabela.

* Em `JOINED`:

  * Existe uma tabela base + tabelas de detalhes, mas o Hibernate faz `JOIN` para montar o objeto completo.
  * O polimorfismo já depende do `JOIN`. Controlar `UNION` não se aplica aqui — o mapeamento de JOIN resolve.

* Em `TABLE_PER_CLASS`:

  * **Cada subclasse é uma tabela independente, sem tabela comum**.
  * Então, para consultar a superclasse, o Hibernate **precisa decidir se vai executar um `UNION` de todas as tabelas concretas** ou não.
  * Aqui o `@Polymorphism` faz diferença:

    * `IMPLICIT` = faz o `UNION` sozinho.
    * `EXPLICIT` = não faz o `UNION` sozinho; você precisa especificar.

👉 Por isso, **só no `TABLE_PER_CLASS` faz diferença prática**.


### ✅ **3️⃣ Resumo**

| Estratégia        | `@Polymorphism` faz sentido? | Por quê?                                                 |
| ----------------- | ---------------------------- | -------------------------------------------------------- |
| `SINGLE_TABLE`    | **Não**                      | Tudo numa tabela só, filtrado por coluna discriminadora. |
| `JOINED`          | **Não**                      | Hibernate faz `JOIN` para montar herança.                |
| `TABLE_PER_CLASS` | **Sim!**                     | Precisa decidir se vai `UNION` ou não entre tabelas.     |


### ✅ **4️⃣ Conclusão**

✔️ **Use `@Polymorphism` só quando estiver usando `TABLE_PER_CLASS`** e quiser controlar explicitamente como o Hibernate faz queries na hierarquia.


## ⚡ **Dica**

Se estiver usando `TABLE_PER_CLASS`:

* **Se quer conveniência:** deixe `IMPLICIT` (ou omita, é o default).
* **Se quer performance ou controle fino:** use `EXPLICIT` + `TREAT` ou consultas específicas.

---
---

# Campos Compostos e Chave Primária Composta 

Existem **dois conceitos muito importantes do JPA** para trabalhar com **componentes incorporados (embedded)**:

* 👉 **`@Embeddable` + `@Embedded`** → Para **campos compostos não-chave** (endereços, contatos, etc.)
* 👉 **`@Embeddable` + `@EmbeddedId`** → Para **definir uma chave primária composta (Composite Primary Key)**

## ✅ **1️⃣ O que foi feito**

### 📌 **(A)** `OrderId` como chave composta

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderId implements Serializable {

    private String username;
    private LocalDateTime orderDate;
}
```

* `@Embeddable`: diz ao JPA que esta classe **não é uma entidade**, mas sim um **tipo incorporável**.
* Implementa `Serializable`: **obrigatório** para chave composta funcionar no JPA.
* Campos simples: `username` + `orderDate` => **representam a chave única do pedido**.


### 📌 **(B)** `Address` como objeto incorporado

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    private String streetName;
    private String houseNumber;
    private String zipCode;
}
```

* Outro `@Embeddable`, mas **não faz parte da chave primária**.
* Usado apenas como **campo composto**, incorporado na tabela do `Order`.


### 📌 **(C)** `Order` usando ambos

```java
@Entity
@Table(name = "ORDER_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @EmbeddedId
    private OrderId orderId;    // Chave primária composta

    @Embedded
    private Address address;    // Objeto incorporado

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "another_field")
    private String anotherField;
}
```

* `@EmbeddedId`: **indica a chave primária composta**.
* `@Embedded`: inclui os campos do `Address` como **colunas na mesma tabela**.
* Campos extras complementam os dados do pedido.

---

## ✅ **2️⃣ Resumo do comportamento**

| Anotação      | Para que serve?                                                   |
| ------------- | ----------------------------------------------------------------- |
| `@Embeddable` | Marca a classe como **componente embutível** (sem tabela própria) |
| `@EmbeddedId` | Usa um `@Embeddable` como **chave primária composta**             |
| `@Embedded`   | Usa um `@Embeddable` como **campo composto normal**               |

---

## ✅ **3️⃣ Como isso vira tabela**

Com a minha classe `Order`:

* No banco, a tabela `ORDER_TBL` fica mais ou menos assim:

  ```sql
  CREATE TABLE ORDER_TBL (
    username VARCHAR,
    order_date DATETIME,
    street_name VARCHAR,
    house_number VARCHAR,
    zip_code VARCHAR,
    order_info VARCHAR,
    another_field VARCHAR,
    PRIMARY KEY (username, order_date)
  );
  ```

Ou seja:

* Os campos do `OrderId` viram colunas que compõem a PK.
* Os campos do `Address` viram colunas adicionais na mesma tabela.

---

## ✅ **4️⃣ Boas práticas para `@EmbeddedId`**

✔️ **Implemente `equals()` e `hashCode()` corretamente** no `OrderId` — o Lombok `@Data` gera isso, mas verifique que é com base em todos os campos da PK.

✔️ **Implemente `Serializable`** — é obrigatório para chave composta.

✔️ **Prefira tipos imutáveis** ou final para PK se possível.

---

## ✅ **5️⃣ Usando na prática**

✅ `Repositorio OrderRepository`
```java
@Repository
public interface OrderRepository extends JpaRepository<Order, OrderId>{

    /* OBSERVAÇÃO: o tipo da PK JpaRepository é OrderId */

    // Buscar todos os pedidos pelo username
    @Query("SELECT o FROM Order o WHERE o.orderId.username = :username")
    List<Order> findByUsername(@Param("username") String username);

    // Buscando endereço pelo zipCode
    @Query("SELECT o FROM Order o WHERE o.address.zipCode = :zipCode")
    List<Order> findByZipCode(@Param("zipCode") String zipCode);
    
}
```

✅ `EmbeddableClassExample`

- **Exemplos de uso**:
    - 1️⃣ Consultar por ID composto inteiro
    - 2️⃣ Consultar por parte da chave (exemplo: só username)
    - 3️⃣ Consultar por campo específico `@Embedded` (exemplo: só zipCode do Address)

```java
@Component
public class EmbeddableClassExample implements CommandLineRunner{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1) Exemplo 1 - Consultar por Id composto inteiro
        System.out.println("\n=== Consultar por Id composto inteiro ===");

        // Criando o ID Composto
        OrderId id = new OrderId("Daniel", LocalDateTime.now());

        // Criando um endereço
        Address address = new Address("Rua A", "123", "99999-999");

        // Criando um pedido
        Order order = new Order(id, address, "Pedido de Teste", "Outro campo");

        // salvando no BD
        orderRepository.save(order);

        System.out.println("Pedido salvo no banco com ID: " + id);

        Optional<Order> optionalOrder = orderRepository.findById(id);
        optionalOrder.ifPresent(o -> System.out.println(
            "Pedido Encontrado - Nome: " + o.getOrderId().getUsername()
            + " | Data: " + o.getOrderId().getOrderDate()
            + " | Info: " + o.getOrderInfo()
        ));


        // 2) Exemplo 2 - Consultar por parte da chave (exemplo: só username)
        System.out.println("\n=== Consultar por parte da chave (exemplo: só username) ===");

        List<Order> orders = orderRepository.findByUsername("Daniel");
        
        orders.forEach(o -> System.out.println(
            "Buscando por parte da chave username - Nome: " + o.getOrderId().getUsername()
            + " | Data: " + o.getOrderId().getOrderDate()
            + " | Info: " + o.getOrderInfo()
        ));


        // 3) Exemplo 3 - Consultar usando o campo @Embedded (ex: zipCode do Address)
        System.out.println("\n=== Consultar usando o campo @Embedded (ex: zipCode do Address) ===");

        List<Order> ordersZipCode = orderRepository.findByZipCode("99999-999");

        ordersZipCode.forEach(o -> System.out.println(
            "Buscando por zipCode - Nome: " + o.getOrderId().getUsername()
            + " | Endereço: " + o.getAddress().getStreetName() 
            + " | Número da Casa: " + o.getAddress().getHouseNumber()
            + " | CEP: " + o.getAddress().getZipCode()
        ));

    }
    
}
```

### ✅ Resumo

| O que fazer                 | Como fazer                |
| --------------------------- | ------------------------- |
| **Chave composta inteira**  | `findById(OrderId)`       |
| **Parte da chave composta** | JPQL: `o.orderId.username`|
| **Campo do @Embedded**      | JPQL: `o.address.zipCode` |


## 🚀 **Resumo final**

* `@EmbeddedId` para chave composta
* `@Embedded` para campo incorporado

📌 **Chave composta = identidade da linha**

📌 **Campo incorporado = modela atributos complexos sem criar outra tabela**

---
---

# Consultas Derivadas (JPQL)

## 📌 **1️⃣ O que são consultas derivadas**

No Spring Data JPA, **consultas derivadas** são **métodos do repositório** que o Spring entende e transforma automaticamente em SQL/JPQL **baseado no nome do método**.

Você não escreve JPQL nem `@Query` — o Spring faz isso para você.


## 🎯 **2️⃣ Como funciona**

### ✅ **Sintaxe básica**

* `findBy` → retorna **1 ou mais registros** que atendem à condição.
* `findAllBy` → igual ao `findBy`, mas semântico: enfatiza que espera **vários resultados**.
* `countBy` → retorna um **número** (quantos registros atendem à condição).
* `existsBy` → retorna **true/false** se existir ou não.

### 📌 **Regras**

* O nome do **atributo** na entidade deve ser igual ao nome no método (case-insensitive).
* Pode usar `And`, `Or`, `Between`, `GreaterThan`, `Like`, `In` etc.



## ✅ **3️⃣ Usando com a entidade `Author`**

### **Entidade - `Author`**

```java
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

    private String firstName;
    private String lastName;
    private String email;
    private int age;
}
```

---

### ✅ **Repositório - `AuthorRepository`**

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{

    // SQL: SELECT * FROM author WHERE email = 'daniel@gmail.com'
    // JPQL: SELECT a FROM Author a WHERE a.email = :email
    // Buscar un autor por email
    Author findByEmail(String email);

    // SQL: SELECT * FROM author WHERE first_name = "Daniel";
    // JPQL: SELECT a FROM Author a WHERE a.first_name = :first_name
    // Equivalente a List<Author> findByFirstName(String firstName);
    // Buscar por nome exato
    List<Author> findAllByFirstName(String firstName);

    // SQL: SELECT * FROM author WHERE first_name = "Daniel"
    // JPQL: SELECT a FROM Author a WHERE a.first_name = :first_name
    // Buscar por nome exato ignorando letras maiúsculas e minúsculas.
    List<Author> findByFirstNameIgnoreCase(String firstName);

    // SQL: SELECT * FROM author WHERE first_name = "Daniel"
    // JPQL: SELECT a FROM Author a WHERE a.first_name = :keyword
    // Buscar por nome contendo o nome ignorando letras maiúsculas e minúsculas - utiliza o operador LiKE '%Dan%'. 
    List<Author> findByFirstNameContainingIgnoreCase(String keyword);

    // SQL: SELECT * FROM author WHERE first_name LIKE 'Dan%'
    // JPQL: SELECT a FROM Author a WHERE a.first_name LIKE :keyword
    // Buscar por todos com nome começando com a palavra "Dan" (LiKE 'Dan%') ignorando letras maiúsculas e minúsculas.
    List<Author> findByFirstNameStartsWithIgnoreCase(String prefix);

    // SQL: SELECT * FROM author WHERE first_name LIKE '%Dan'
    // JPQL: SELECT a FROM Author a WHERE a.first_name LIKE :prefix 
    // Buscar por todos com nome terminando com a palavra "Dan" (LiKE '%Dan') ignorando letras maiúsculas e minúsculas.
    List<Author> findByFirstNameEndsWithIgnoreCase(String prefix);

    // SQL: SELECT * FROM Author WHERE first_name in('daniel', 'ped', 'marc')
    // JPQL: SELECT a FROM Author a WHERE a.first_name IN (:firstNames)
    // Buscar por nome exato utilizando uma lista de valores 
    List<Author> findByFirstNameInIgnoreCase(List<String> firstNames);

    // SQL: SELECT * FROM Author WHERE first_name = "Daniel" OR last_name = "Penelva";
    // JPQL: SELECT a FROM Author a WHERE a.first_name = :first_name OR a.last_name = :last_name;
    // Buscar por nome exato ou por sobrenome exato ignorando letras maiúsculas e minúsculas.
    List<Author> findByFirstNameOrLastNameIgnoreCase(String firstname, String lastname);

    // SQL: SELECT * FROM Author WHERE age > 30;
    // JPQL: SELECT a FROM Author a WHERE a.age > :age;
    // Buscar por idade maior que...
    List<Author> findByAgeGreaterThan(int age);
    
    // SQL: SELECT * FROM Author WHERE age BETWEEN 30 AND 40;
    // JPQL: SELECT a FROM Author a WHERE a.age BETWEEN :start AND :end;
    // Buscar por idade entre...
    List<Author> findByAgeBetween(int start, int end);

    // SQL: SELECT * FROM author WHERE email = "gmail"
    // JPQL: SELECT a FROM Author a WHERE a.email LIKE :keyword
    // Buscar por email contendo...
    List<Author> findByEmailContaining(String keyword);

    // SQL: SELECT * FROM author WHERE EXISTS (SELECT email FROM author WHERE email = "gmail")
    // JPQL: SELECT a FROM Author a WHERE EXISTS (SELECT b FROM Author b WHERE b .email = :email)
    // Verificar se existe autor com email.
    boolean existsByEmail(String email);

    // SQL: SELECT COUNT(age) FROM author
    // JPQL: SELECT COUNT(a.age) FROM Author a
    // Contar autores com idade maior que...
    long countByAgeGreaterThan(int age);

    // SQL: SELECT COUNT(age) FROM author
    // JPQL: SELECT COUNT(a.age) FROM Author a
    // Contar autores com idade maior ou igual a...
    long countByAgeGreaterThanEqual(int age);

    // Buscar todos com o nome começando com...
    // SQL: SELECT * FROM author WHERE first_name LIKE 'Dan%'
    // JPQL: SELECT a FROM Author a WHERE a.first_name LIKE :prefix
    // Buscar todos com o nome começando com...
    List<Author> findAllByFirstNameStartingWith(String prefix);
}
```

### ✅ **Testando - `testDerivedQueries`**

```java
@Component
public class testDerivedQueries implements CommandLineRunner {

    private final FileRepository fileRepository;

    private final DemoDataJpaApplication demoDataJpaApplication;

    @Autowired
    private AuthorRepository authorRepository;

    testDerivedQueries(DemoDataJpaApplication demoDataJpaApplication, FileRepository fileRepository) {
        this.demoDataJpaApplication = demoDataJpaApplication;
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Marcelo")
                .lastName("Silva")
                .email("marcelo@gmail.com")
                .age(35)
                .build();

        var author3 = Author.builder()
                .firstName("Pedro")
                .lastName("Mota")
                .email("pedro@gmail.com")
                .age(27)
                .build();

        var author4 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(31)
                .build();

        // Salvando os autores
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
        authorRepository.save(author4);

        // Criando uma lista de Autores
        List<Author> authors = new ArrayList<>();

        // Adicionando os autores numa lista de Autores
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);
        authors.add(author4);

        // Gerar uma lista de autores
        System.out.println("=== Criando Autor ===");
        authors.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | Sobrenome: " + a.getLastName()
                + " | Email: " + a.getEmail()
                + " | Idade: " + a.getAge()));

        // 1) Buscar un autor por email
        System.out.println("\n=== Buscar Autor por Email ===");
        var authorByEmail = authorRepository.findByEmail("marcelo@gmail.com");
        System.out.println("Dados do Autor - Nome: " + authorByEmail.getFirstName()
                + " | E-mail: " + authorByEmail.getEmail());

        
        // 2) Buscar por nome exato
        System.out.println("\n=== Buscar por nome exato ===");

        List<Author> authorsByName = authorRepository.findAllByFirstName("Daniel");
        authorsByName.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));

        
        // 3) Buscar por nome exato ignorando letras maiúsculas e minúsculas
        System.out.println("\n=== Buscar por nome exato ignorando letras maiúsculas");
        List<Author> authorsByNameIgnoringCase = authorRepository.findByFirstNameIgnoreCase("maria");
        authorsByNameIgnoringCase.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));


        // 4) Buscar por nome contendo o nome ignorando letras maiúsculas e minúsculas
        System.out.println("\n=== Buscar por nome contendo o nome e ignorando letras maiúsculas e minúsculas");
        List<Author> authorsByNameContaining = authorRepository.findByFirstNameContainingIgnoreCase("ma");
        if (authorsByNameContaining != null && !authorsByNameContaining.isEmpty()) {
            authorsByNameContaining.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 5) Buscar por todos os nomes começando com a palavra "m" (LiKE 'm%') ignorando letras maiúsculas e minúsculas.
        System.out.println("\n=== Buscar por todos os nomes começando com a(s) letra(s) e ignorando letras maiúsculas e minúsculas");
        List<Author> authorsByNameStartingWith = authorRepository.findByFirstNameStartsWithIgnoreCase("p");
        if (authorsByNameStartingWith != null && !authorsByNameStartingWith.isEmpty()) {
            authorsByNameStartingWith.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 6) Buscar por todos com nome terminando com a palavra "o" e ignorando letras maiúsculas e minúsculas.
        System.out.println("\n=== Buscar por todos com nome terminando a(s) letra(s) e ignorando letras maiúsculas e minúsculas.");
        List<Author> authorsByNameEndingWith = authorRepository.findByFirstNameEndsWithIgnoreCase("o");
        if (authorsByNameEndingWith != null && !authorsByNameEndingWith.isEmpty()) {
            authorsByNameEndingWith.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 7) Buscar por nome exato utilizando uma lista de valores
        System.out.println("\n=== Buscar por nome exato utilizando uma lista de valores");
        List<String> names = Arrays.asList("Daniel", "Maria");
        List<Author> authorsByNameIn = authorRepository.findByFirstNameInIgnoreCase(names);
        if (authorsByNameIn != null && !authorsByNameIn.isEmpty()) {
            authorsByNameIn.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 8) Buscar por nome exato ou por sobrenome exato ignorando letras maiúsculas e minúsculas.
        System.out.println("\n=== Buscar por nome exato ou por sobrenome exato ignorando letras maiúsculas e minúsculas.");
        List<Author> authorsByNameOrLastName = authorRepository.findByFirstNameOrLastNameIgnoreCase("daniel", "penelva");
        if (authorsByNameOrLastName != null && !authorsByNameOrLastName.isEmpty()) {
            authorsByNameOrLastName.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 9) Buscar por idade maior que...
        System.out.println("\n === Buscar por idade maior que...");
        List<Author> authorsByAgeGreaterThan = authorRepository.findByAgeGreaterThan(30);
        if (authorsByAgeGreaterThan != null && !authorsByAgeGreaterThan.isEmpty()) {
            authorsByAgeGreaterThan.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()
                + " | Idade: " + a.getAge()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 10) Buscar por idade entre...
        System.out.println("\n === Buscar por idade entre...");
        List<Author> authorsByAgeBetween = authorRepository.findByAgeBetween(20, 35);
        if (authorsByAgeBetween != null && !authorsByAgeBetween.isEmpty()) {
            authorsByAgeBetween.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()
                + " | Idade: " + a.getAge()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 11) Buscar por email contendo...
        System.out.println("\n === Buscar por email contendo...");
        List<Author> authorsByEmailContaining = authorRepository.findByEmailContaining("gmail.com");
        if (authorsByEmailContaining != null && !authorsByEmailContaining.isEmpty()) {
            authorsByEmailContaining.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()
                + " | E-mail: " + a.getEmail()));
        } else {
            System.out.println("Nenhum autor encontrado");
        }


        // 12) Verificar se existe autor com email.
        System.out.println("\n === Verificar se existe autor com email.");
        boolean existsAuthorByEmail = authorRepository.existsByEmail("davi.@gmail.com");
        if (!existsAuthorByEmail){
            System.out.println("NÃO EXISTE autor com o e-mail");
        } else {
            System.out.println("Existe autor com o e-mail");
        }


        // 13) Contar autores com idade maior que...
        System.out.println("\n === Contar autores com idade maior que...");
        Long countAuthors = authorRepository.countByAgeGreaterThan(30);
        if (countAuthors == 0) {
            System.out.println("NÃO FOI ENCONTRADO autores com idade maior que 35 anos!");
        } else {
            System.out.println("FOI ENCONTRADO " + countAuthors + " autor(es) com idade maior que 35 anos!");
        }

        /*Então: 
         *  Daniel (37) -> 37 é maior que 35 = 1
         *  Marcelo (35) -> 35 é maior que 35 = 1
         *  Pedro (27) -> 27 é menor que 30 = 0
         *  Maria (31) -> 30 é maior que 36 = 1 
         *                                     / 3 autores ao todo
        */


        // 14) Contar autores com idade maior ou igual a...
        System.out.println("\n === Contar autores com idade maior ou igual a...");
        Long countAuthors2 = authorRepository.countByAgeGreaterThanEqual(35);
        if (countAuthors2 == 0) {
            System.out.println("NÃO FOI ENCONTRADO autores com idade maior que 35 anos!");
        } else {
            System.out.println("FOI ENCONTRADO " + countAuthors2 + " autor(es) com idade maior ou igual que 35 anos!");
        }

        /*Então: 
         *  Daniel (37) -> 37 é maior que 35 = 1
         *  Marcelo (35) -> 35 é igual a 35 = 1
         *  Pedro (27) -> 27 é menor que 35 = 0
         *  Maria (31) -> 30 é menor que 35 = 0 
         *                                     / 2 autores ao todo
        */


        // 15) Buscar todos com o nome começando com...
        System.out.println("\n === Buscar todos com o nome começando com...");
        List<Author> authorsByFirstNameStartingWith = authorRepository.findAllByFirstNameStartingWith("M");
        if (authorsByFirstNameStartingWith.isEmpty()) {
            System.out.println("NÃO FOI ENCONTRADO autor com o nome começando com M");
        } else {
            authorsByFirstNameStartingWith.forEach(a -> System.out.println("Dados do Autor - Nome: " + a.getFirstName()));
            System.out.println("FOI ENCONTRADO " + authorsByFirstNameStartingWith.size() + " autor(es) com o nome começando com M");
        }
        
        
    }

}
```

## ✅ **4️⃣ Diferença `findBy` vs `findAllBy`**

✔️ **`findBy`**

* Serve para 1 ou vários resultados.
* Retorna `Optional<Author>` se for algo único (ex: `findByEmail`).
* Ou `List<Author>` se puder ter vários.

✔️ **`findAllBy`**

* Semântico: deixa claro que espera **vários registros**.
* Não muda nada no SQL, só ajuda a clareza.

Exemplo:

```java
List<Author> findAllByAgeGreaterThan(int age);
```

É igual a:

```java
List<Author> findByAgeGreaterThan(int age);
```

---

## ✅ **5️⃣ Dica de boas práticas**

✔️ Para buscar 1 registro único:

```java
Optional<Author> findByEmail(String email);
```

✔️ Para buscar vários registros:

```java
List<Author> findAllByFirstName(String firstName);
```

✔️ Para consultas complexas: usar `@Query`.


## ✅ **6️⃣ Palavras-chave mais comuns**

| Palavra        | O que faz   |
| -------------- | ----------- |
| `And`          | E           |
| `Or`           | OU          |
| `Not`          | NEGAÇÃO     |
| `Is`           | IGUAL `(=)` |
| `Equals`       | IGUAL `(=)` |
| `Between`      | ENTRE       |
| `LessThan`     | MENOR QUE `(<)`  |
| `LessThanEqual`| MENOR OU IGUAL QUE `(<=)`   |
| `GreaterThan`  | MAIOR QUE `(>)`  |
| `GreaterThanEqual`  | MAIOR OU IGUAL QUE `(>=)`|
| `After`        | `>` (datas) |
| `Before`       | `<` (datas) |
| `Containing`   | LIKE %x%    |
| `StartingWith` | LIKE x%     |
| `EndingWith`   | LIKE %x     |
| `OrderBy`      | ORDER BY    |
| `IsNull`       | IS NULL     |
| `IsNotNull`    | IS NOT NULL |
| `In`           | IN (...)    |
| `NotIn`        | NOT IN (...)|
| `IgnoreCase`           | IGNORA MAIÚSCULAS/MINÚSCULAS |

**Por exemplo: `findByFirstNameOrLastNameIgnoreCase`**

## 🚀 **Resumo**

👉 **Consultas derivadas** = menos código = mais legibilidade.
👉 São ótimas para **filtros simples e diretos**.
👉 Para consultas mais complexas → use `@Query` ou `Specification`.

IgnoreCase	Ignora maiúsculas/minúsculas

---
---
# Faker - Geração de Dados

## ✅ **O que é insert fake data?**

Em um projeto real, você geralmente quer **popular o banco com dados de exemplo** — para:

* testar consultas,
* validar lógica de negócio,
* ter registros prontos para desenvolvimento e demonstrações,
* simular cenários reais com dados realistas.

## ✅ **Dependência do Faker**

🔑 Para usar o **Java Faker**, você precisa adicionar a dependência no seu `pom.xml` (caso use Maven) ou `build.gradle` (se for Gradle).

📦 **Para Maven:**

```xml
<dependency>
    <groupId>com.github.javafaker</groupId>
    <artifactId>javafaker</artifactId>
    <version>1.0.2</version>
</dependency>
```

📦 **Para Gradle:**

```groovy
implementation 'com.github.javafaker:javafaker:1.0.2'
```

✅ **Dica:**

* A versão `1.0.2` é a última estável mais usada.
* Depois de adicionar, execute `mvn install` ou `gradle build` para baixar a lib.


## ⚙️ **Maneiras comuns de inserir dados falsos**

### 1️⃣ **Usar `CommandLineRunner`**

Essa é a forma mais direta no Spring Boot:
VAqui, cria um **bean** que roda **assim que a aplicação inicia** e insere registros no banco.

**Exemplo utilizando entidade `Author`:**

```java
@Component
public class AuthorDataSeeder implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public void run(String... args) throws Exception {
        if (authorRepository.count() == 0) {
            // Só insere se estiver vazio, para não duplicar
            Author author1 = Author.builder()
                    .firstName("Daniel")
                    .lastName("Penelva")
                    .email("daniel@gmail.com")
                    .age(37)
                    .build();

            Author author2 = Author.builder()
                    .firstName("Marcelo")
                    .lastName("Silva")
                    .email("marcelo@gmail.com")
                    .age(35)
                    .build();

            Author author3 = Author.builder()
                    .firstName("Pedro")
                    .lastName("Mota")
                    .email("pedro@gmail.com")
                    .age(27)
                    .build();

            // Salva todos de uma vez
            authorRepository.saveAll(List.of(author1, author2, author3));

            System.out.println("=== Fake autores Inseridos ===");
        }
    }
}
```

🔑 **Dica:**
A verificação `if (authorRepository.count() == 0)` é boa prática para não duplicar dados toda vez que reiniciar a aplicação.

---

### 2️⃣ **Usar bibliotecas de geração de dados fake**

Aqui, pode usar **bibliotecas populares**, como:

* **[Faker](https://github.com/DiUS/java-faker)** (muito usada)
* **RandomBeans**
* Ou criar seu próprio utilitário

**Exemplo com `Faker`:**

```java
package com.api.demo_data_jpa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.Author;
import com.api.demo_data_jpa.repository.AuthorRepository;
import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

@Component
public class AuthorFakeDataFaker implements CommandLineRunner{

    @Autowired
    private AuthorRepository authorRepository;

    private final Faker faker = new Faker();


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (authorRepository.count() == 0) {     // Esse if é uma boa prática para não duplicar dados toda vez que reiniciar a aplicação.

            List<Author> authors = new ArrayList<>();

            for(int i=0; i< 50; i++) {
                Author author = Author.builder()
                        .firstName(faker.name().firstName())         // Um nome aleatório
                        .lastName(faker.name().lastName())           // Um sobrenome aleatório
                        .email(faker.internet().emailAddress())      // Um e-mail plausível
                        .age(faker.number().numberBetween(20, 60))   // Um número dentro do intervalo de 20 a 60
                        .build();

                authors.add(author);
            }
            
            authorRepository.saveAll(authors);
            System.out.println("=== Fake autores gerados com Faker ===");

            for (Author author : authors) {
                System.out.println("Nome do autor: " + author.getFirstName() 
                    + " | Sobrenome: " + author.getLastName()
                    + " | E-mail: " + author.getEmail()
                    + " | Idade: " + author.getAge());
            }
        }
    }
    
}
```

### 3️⃣ **Usar scripts SQL**

Outra forma clássica:

* Criar um arquivo `data.sql` em `src/main/resources`:

  ```sql
  INSERT INTO author_tbl (id, first_name, last_name, email, age)
  VALUES (1, 'Daniel', 'Penelva', 'daniel@gmail.com', 37);

  INSERT INTO author_tbl (id, first_name, last_name, email, age)
  VALUES (2, 'Marcelo', 'Silva', 'marcelo@gmail.com', 35);
  ```

* O Spring Boot executa isso automaticamente na inicialização (se configurado com `spring.datasource.initialization-mode=always` ou usando `schema.sql` e `data.sql`).

## 🚦 **Qual usar?**

| Método                | Vantagem                                                 | Desvantagem                                 |
| --------------------- | -------------------------------------------------------- | ------------------------------------------- |
| **CommandLineRunner** | Flexível, usa código Java, pode rodar lógica condicional | Precisa compilar                            |
| **Biblioteca Faker**  | Gera dados realistas, ideal para grande volume           | Precisa adicionar dependência               |
| **SQL (`data.sql`)**  | Simples e claro, fácil de controlar                      | Difícil de gerar dados grandes ou dinâmicos |


## ✅ **Ídeia Geral do Faker**

Quando se fala em usar **fake data**, especialmente com uma **biblioteca como o [Java Faker](https://github.com/DiUS/java-faker)**, a ideia é **gerar valores aleatórios realistas automaticamente** para as propriedades do seu objeto — sem você ter que inventar manualmente.

### 📌 **Como funciona na prática**

Por exemplo:
No `Faker`:

| **Propriedade** | **Método do Faker**                    | O que ele gera?               |
| --------------- | -------------------------------------- | ----------------------------- |
| `firstName`     | `faker.name().firstName()`             | Um nome aleatório             |
| `lastName`      | `faker.name().lastName()`              | Um sobrenome aleatório        |
| `email`         | `faker.internet().emailAddress()`      | Um e-mail plausível           |
| `age`           | `faker.number().numberBetween(20, 60)` | Um número dentro do intervalo |

Assim, para cada loop, você monta um objeto com dados únicos, por exemplo:

```java
Faker faker = new Faker();

Author author = Author.builder()
    .firstName(faker.name().firstName())   // exemplo: "Lucas"
    .lastName(faker.name().lastName())     // exemplo: "Souza"
    .email(faker.internet().emailAddress())// exemplo: "lucas.souza@example.com"
    .age(faker.number().numberBetween(20, 60)) // exemplo: 45
    .build();
```

Cada vez que roda, ele inventa outros valores.


### 📍 **Resumo**

✅ **Com Faker**:
Você **não precisa definir manualmente** cada valor.
O Faker cria dados que **parecem reais** (nomes, e-mails, endereços, CPFs, frases, datas, etc).

⚡ **Sem Faker**:
Você mesmo escreve cada valor fixo (ex: `.firstName("Daniel")`).


### 💡 **Por que usar Faker?**

* Automatiza a geração de muitos registros de teste.
* Deixa seu banco com aparência de dados reais.
* Evita repetição de nomes iguais.
* Ideal para testes de performance, paginação, relatórios, etc.


## ✅ **Conclusão**

📌 Para um projeto real:

* Usar `CommandLineRunner` com `Faker` para dados dinâmicos.
* Para seed inicial simples, `data.sql` é suficiente.

---
---

# @Modifying - Para executar operações de alteração no banco de dados

## 📌 **@Modifying: o que é e quando usar**

No Spring Data JPA, o `@Modifying` é uma anotação usada **para executar operações de escrita diretamente com `@Query`**, como `UPDATE`, `DELETE` ou `INSERT` customizados.

➡️ Por padrão, o Spring Data entende que consultas `@Query` são **somente leitura** — então, quando você faz algo que modifica o banco, precisa informar explicitamente com `@Modifying`.


## ⚙️ **Como funciona**

### ✅ **Quando usar:**

* Quando quer **executar um SQL JPQL ou SQL nativo** de atualização/exclusão em lote.
* Quando não quer carregar a entidade inteira, apenas atualizar um campo específico.
* Quando quer atualizar muitos registros de uma vez sem usar `save()` para cada um.


## 📌 **Exemplo com a entidade `Author`**

### 🔹 **Passo 1:**

Definindo o método no repositório (`AuthorRepository`):

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    // Atualizar nome do autor pelo Id
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Author a SET a.firstName = :firstName WHERE a.id = :id")
    int updateFirstNameById(@Param("id") Integer id, @Param("firstName") String firstName);


    // Atualizar idade do autor pelo Id
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Author a SET a.age = :age WHERE a.id = :id")
    int updateAgeById(@Param("id") Integer id, @Param("age") Integer age);


    // Excluir autor com idade menor que...
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Author a WHERE a.age < :age")
    int deleteAuthorsYoungerThan(@Param("age") int age);

}
```

### 🔹 **Explicação:**

  ✅ `@Modifying` → diz ao Spring que a `@Query` vai modificar dados.

  ✅ `clearAutomatically = true` → Limpa o cache do EntityManager após a atualização e evita inconsistências de leitura logo após um @Modifying.

  ✅ `@Transactional` → operações de escrita precisam de transação.

  ✅ `@Query` → define o JPQL de atualização.

  ✅ O método retorna `int` → quantidade de linhas afetadas.


## 🔹 **Passo 2:**

Usando esses métodos em um serviço ou `CommandLineRunner`:

✅ **Exemplo 1:** `Classe de teste AuthorFakeDataFaker`

```java
@Component
public class AuthorFakeDataFaker implements CommandLineRunner{

    @Autowired
    private AuthorRepository authorRepository;

    private final Faker faker = new Faker();


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (authorRepository.count() == 0) {   // Esse if é uma boa prática para não duplicar dados toda vez que reiniciar a aplicação.

            List<Author> authors = new ArrayList<>();

            for(int i=0; i< 10; i++) {
                Author author = Author.builder()
                        .firstName(faker.name().firstName())         // Um nome aleatório
                        .lastName(faker.name().lastName())           // Um sobrenome aleatório
                        .email(faker.internet().emailAddress())      // Um e-mail plausível
                        .age(faker.number().numberBetween(20, 60))   // Um número dentro do intervalo de 20 a 60
                        .build();

                authors.add(author);
            }
            
            authorRepository.saveAll(authors);
            System.out.println("=== Fake autores gerados com Faker ===");

            for (Author author : authors) {
                System.out.println("Nome do autor: " + author.getFirstName() 
                    + " | Sobrenome: " + author.getLastName()
                    + " | E-mail: " + author.getEmail()
                    + " | Idade: " + author.getAge());
            }
        }

        // ---- UPDATE ----
        // Atualizando o nome do autor com id 1 
        int rowsUpdateFirstName = authorRepository.updateFirstNameById(1, "Daniel Updated");
        System.out.println("Atualizar Linha do nome: " + rowsUpdateFirstName);


        // Atualizando a idade do autor com id 1
        int rowsUpdateAge = authorRepository.updateAgeById(1, 30);
        System.out.println("Atualizar linha da idade: " + rowsUpdateAge);


        // ---- DELETE ----
        int rowsDelete = authorRepository.deleteAuthorsYoungerThan(30);
        System.out.println("Linhas deletadas (autores com idade < 30): " + rowsDelete);


        // ---- Estado Final dos Autores ----
        System.out.println("\nEstado Final dos Autores:");
        List<Author> remainingAuthors = authorRepository.findAll();
        remainingAuthors.forEach(a -> System.out.println(
            "ID: " + a.getId()
            + " | Nome: " + a.getFirstName()
            + " | Idade: " + a.getAge()
        ));
    }
    
}
```

✅ **Exemplo 2:** `Classe de teste AuthorFakeDataFaker`

```java
@Component
public class ModifyingQueryExample implements CommandLineRunner{

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Marcelo")
                .lastName("Silva")
                .email("marcelo@gmail.com")
                .age(35)
                .build();

        var author3 = Author.builder()
                .firstName("Pedro")
                .lastName("Mota")
                .email("pedro@gmail.com")
                .age(27)
                .build();

        var author4 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(31)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3, author4));

        System.out.println("\n=== Lista de Autores ===");
        authorRepository.findAll().forEach(a -> System.out.println(
            "ID: " + a.getId() 
            + " | Nome: " + a.getFirstName() 
            + " | Idade: " + a.getAge()
        ));

        // 1) Atualizar nome do author
        System.out.println("\n=== Atualizar nome do autor ===");
        authorRepository.updateFirstNameById(1, "Daniel Updated");

        authorRepository.findAll().forEach(a -> System.out.println(
            "ID: " + a.getId() 
            + " | Nome: " + a.getFirstName()
        ));

        // 2) Atualizar idade do author
        System.out.println("\n=== Atualizar idade do autor ===");
        authorRepository.updateAgeById(1, 31);

        authorRepository.findAll().forEach(a -> System.out.println(
            "ID: " + a.getId() 
            + " | Nome: " + a.getFirstName() 
            + " | Idade: " + a.getAge()
        ));


        // 3) Deletar autores com idade menor que 30
        System.out.println("\n=== Deletar autores com idade menor que 30 ===");
        authorRepository.deleteAuthorsYoungerThan(30);
        

        System.out.println("\n=== Depois do delete ===");
        
        authorRepository.findAll().forEach(a -> System.out.println(
            "ID: " + a.getId() 
            + " | Nome: " + a.getFirstName() 
            + " | Idade: " + a.getAge()
        ));

    }   
}
```

## 📍 **Observações importantes**

✅ **Por que não usar só `save()`?**

* Com `save()`, você precisa buscar a entidade, alterar o campo, depois salvar.
* Com `@Modifying`, você faz direto no banco → melhor para atualizações em lote ou simples.

⚠️Quando se usa `@Modifying` com uma `@Query`, **você está escrevendo SQL ou JPQL manualmente**, e o Spring Data JPA **executa diretamente no banco de dados**, sem passar pelos métodos padrões do `save()` ou `EntityManager.persist()`.


### ✅ Vantagens do `@Modifying`

* **Mais performático** para:

  * Atualizações em **lote** (`UPDATE ... WHERE ...`)
  * **Deleções específicas** sem carregar os objetos na memória
* Evita o custo de conversão de entidade e sincronização do estado no Hibernate
* Ideal quando você **não precisa modificar o objeto em memória**, apenas atualizar no banco


### ⚠️ Quando usar `save(...)`?

* Quando está **criando** ou **atualizando entidades específicas em memória**
* Quando quer aproveitar o ciclo de vida da entidade (eventos como `@PrePersist`, `@PreUpdate`, etc.)
* Quando os dados **vêm do usuário** ou de alguma camada da aplicação


### 🗂️ **Resumo**

| O que faz                                 | Qual anotação usar                         |
| ----------------------------------------- | ------------------------------------------ |
| Query de leitura                          | Só `@Query`                                |
| Query de escrita (UPDATE, DELETE, INSERT) | `@Query` + `@Modifying` + `@Transactional` |


### 🗂️ **Melhor Abordagem**

| Cenário                                                             | Melhor abordagem          |
| ------------------------------------------------------------------- | ------------------------- |
| Atualizar muitos registros direto no banco (sem carregar entidades) | `@Modifying` com `@Query` |
| Atualizar 1 entidade carregada da base ou criada na app             | `save()` ou `saveAll()`   |
| Deletar vários registros com filtro                                 | `@Modifying` com `DELETE` |
| Deletar um objeto já carregado                                      | `delete(...)`             |





## Comparativo **`@Modifying` com `save()`**

✅ Para comparar **`@Modifying` com `save()`** usando a entidade `Author`.


✅ 📌 Entidade `Author`:

```java
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private int age;
}
```

### ✅ 1. Atualizando com `save()` (padrão JPA)

#### 🔁 Fluxo:

* Carrega o autor do banco com `findById`
* Modifica os atributos
* Usa `save()` para sincronizar com o banco

#### 🧪 Exemplo:

```java
Optional<Author> optionalAuthor = authorRepository.findById(1);

if (optionalAuthor.isPresent()) {
    Author author = optionalAuthor.get();
    author.setFirstName("Daniel Updated");
    author.setAge(40);
    
    authorRepository.save(author); // Aqui sincroniza com o banco
}
```

#### ✅ Vantagens:

* Simples e seguro
* Usa o contexto de persistência (Hibernate controla o estado)

#### ⚠️ Desvantagens:

* Carrega o objeto na memória
* Pode ser **ineficiente para grandes volumes** (N objetos → N queries)


### ✅ 2. Atualizando com `@Modifying` + `@Query` (SQL/JPQL direto)

#### 📍 Repositório: `AuthorRepository`

```java
@Modifying
@Transactional
@Query("UPDATE Author a SET a.firstName = :name WHERE a.id = :id")
int updateFirstNameById(@Param("id") Integer id, @Param("name") String name);
```

#### 🧪 Exemplo:

```java
int updated = authorRepository.updateFirstNameById(1, "Daniel Updated");
System.out.println("Linhas atualizadas: " + updated);
```

#### ✅ Vantagens:

* **Mais performático**: não carrega entidade
* Perfeito para **atualizações em lote**

#### ⚠️ Desvantagens:

* Não atualiza o objeto em memória (caso precise)
* Sem suporte automático a validações ou eventos JPA


### ✅ Comparativo

| Aspecto                     | `save()`                          | `@Modifying` com `@Query`    |
| --------------------------- | --------------------------------- | ---------------------------- |
| Controle de estado          | Sim (Hibernate)                   | Não                          |
| Performance (grandes lotes) | Mais lenta                        | Muito mais rápida            |
| Precisa de `findById`       | Sim                               | Não                          |
| Atualiza objeto em memória  | Sim                               | Não                          |
| Ideal para                  | Atualização pontual ou com lógica | Atualização em massa simples |


## 🔥 Dica Extra

Pode combinar os dois:

```java
// Atualiza por JPQL rápido...
authorRepository.updateFirstNameById(1, "Atualizado");

// E recarrega caso precise exibir depois:
Author updated = authorRepository.findById(1).orElse(null);
System.out.println(updated.getFirstName());
```

---
---

# O que são `@NamedQuery` e `@NamedQueries`?

O uso de **`@NamedQuery` / `@NamedQueries`** são ótimos para **centralizar consultas**, para organizar melhor **consultas mais reutilizáveis** e para **consultas mais complexas**.

* São **consultas JPQL estáticas** associadas a uma **entidade**.
* Definidas **na própria classe da entidade**, geralmente no topo.
* Compiladas **em tempo de inicialização** da aplicação → ajudam a evitar erros de sintaxe em tempo de execução.
* Permite centralizar e reutilizar **lógicas de negócio repetitivas ou complexas**.

## Utilizando `@NamedQuery`

### 🧩 1. Entidade com `@NamedQuery`

✅ `Entidade Course`

```java
@Entity
@Table(name = "COURSE_TBL")
@NamedQuery(
    name = "Course.findByName",
    query = "SELECT c FROM Course c WHERE c.name = :name"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Course extends BaseEntity{

    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;
}
```

✅ `Repositório CourseRepository`

```java
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{

    /* ==== Utilizando @NamedQuery ==== */ 

    @Query(name = "Course.findByName")
    Optional<Course> buscarPorNome(@Param("name") String name);
    
}
```

✅ `Classe de Teste NamedQueryExample`

```java
@Component
public class NamedQueryExample implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Course course1 = Course.builder()
                .name("Programação Orientado a Objeto")
                .description("Texto sobre POO")
                .build();

        Course course2 = Course.builder()
                .name("Programação JAVA")
                .description("Texto sobre Programação JAVA")
                .build();

        Course course3 = Course.builder()
                .name("Arquitetura de Software")
                .description("Texto sobre Arquitetura de Software")
                .build();

        Course course4 = Course.builder()
                .name("Modelagem de Dados")
                .description("Texto sobre Modelagem de dados")
                .build();

        courseRepository.saveAll(List.of(course1, course2, course3, course4));

        // Buscar Cursos por Nome
        System.out.println("\n === Buscando Curso por nome ===");
        courseRepository.buscarPorNome("Arquitetura de Software").ifPresentOrElse(
                c -> System.out.println("Curso Encontrado: " + c.getName()),
                () -> System.out.println("Curso não encontrado"));
    }

}
```

### ✅ Diferença entre `@NamedQuery` vs `@Query`

| Aspecto         | `@NamedQuery`                       | `@Query`                      |
| --------------- | ----------------------------------- | ----------------------------- |
| Onde é definida | Na **entidade**                     | No **repositório**            |
| Avaliada        | **Na inicialização** do Hibernate   | Em **tempo de execução**      |
| Boa para reuso  | ✅                                   | Apenas em um ponto específico |
| Performance     | Ligeiramente melhor (pré-compilada) | Leve diferença                |


## Utilizando `@NamedQueries`

### 🧩 2. Entidade com `@NamedQueries`

✅ `Entidade Author`

```java
@Entity
@Table(name = "AUTHOR_TBL")
@NamedQueries({
    @NamedQuery(
        name = "Author.findByEmail",
        query = "SELECT a FROM Author a WHERE a.email = :email"
    ),
    @NamedQuery(
        name = "Author.findByFirstName",
        query = "SELECT a FROM Author a WHERE a.firstName = :firstName"
    ),
    @NamedQuery(
        name = "Author.findByAgeGreaterThan",
        query = "SELECT a FROM Author a WHERE a.age > :age"
    ),
    @NamedQuery(
        name = "Author.countByFirstName",
        query = "SELECT COUNT(a) FROM Author a WHERE a.firstName = :firstName"
    )
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class Author extends BaseEntity{

    @Column(name = "first_name", nullable = false, length = 35)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @JsonProperty("last_name")
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private int age;
}
```

✅ `Repositório AuthorRepository`

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{

    /* ==== Utilizando @NamedQueries ==== */ 
    @Query(name = "Author.findByEmail")
    Optional<Author> buscarPorEmail(@Param("email") String email);

    @Query(name = "Author.findByFirstName")
    List<Author> buscarPorNome(@Param("firstName") String firstName);

    @Query(name = "Author.findByAgeGreaterThan")
    List<Author> buscarPorIdadeMaiorQue(@Param("age") int idade);

    @Query(name = "Author.countByFirstName")
    long contarPorNome(@Param("firstName") String nome);
    
}
```

✅ `Classe de Teste NamedQueriesExample`

```java
@Component
public class NamedQueriesExample implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Marcelo")
                .lastName("Silva")
                .email("marcelo@gmail.com")
                .age(35)
                .build();

        var author3 = Author.builder()
                .firstName("Pedro")
                .lastName("Mota")
                .email("pedro@gmail.com")
                .age(27)
                .build();

        var author4 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(31)
                .build();

        var author5 = Author.builder()
                .firstName("Daniel")
                .lastName("Mota")
                .email("danielmota@gmail.com")
                .age(34)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3, author4, author5));

        // 1) Buscar Autor por E-mail
        System.out.println("\n=== Exemplo 1: Buscar Autor por E-mail ===");

        authorRepository.buscarPorEmail("pedro@gmail.com").ifPresentOrElse(
                a -> System.out.println("E-mail Encontrado: " + a.getEmail()),
                () -> System.out.println("E-mail não encontrado"));

        // Ou pode fazer assim - código menos limpo:
        System.out.println("\n=== Exemplo 2: Buscar Autor por E-mail ===");
        var authorByEmail = authorRepository.buscarPorEmail("danielmota@gmail.com");
        if (authorByEmail.isPresent()) {
            System.out.println("E-mail Encontrado: " + authorByEmail.get().getEmail());
        } else {
            System.out.println("E-mail Não Encontrado.");
        }


        // 2) Buscar Autor por Nome

        System.out.println("\n=== Exemplo 1: Buscar Autor por nome ===");
        authorRepository.buscarPorNome("Daniel").forEach(a -> System.out.println(
                "Nome: " + a.getFirstName()
                        + " - Idade: " + a.getAge()));

        // Ou pode fazer assim condicionando
        System.out.println("\n=== Exemplo 2: Buscar Autor por nome ===");
        List<Author> autores = authorRepository.buscarPorNome("Daniel");
        if (!autores.isEmpty()) {
            autores.forEach(a -> System.out.println("Nome encontrado: " + a.getFirstName()));
        } else {
            System.out.println("Nome não encontrado.");
        }


        // 3) Buscar por idade maior que...
        System.out.println("\n=== Exemplo 1: Buscar por idade maior que... ===");
        authorRepository.buscarPorIdadeMaiorQue(30).forEach(System.out::println);

        // Ou pode fazer assim:
        System.out.println("\n=== Exemplo 2: Buscar por idade maior que... ===");
        authorRepository.buscarPorIdadeMaiorQue(35).forEach(a -> System.out.println(
                "Nome: " + a.getFirstName()
                        + " - Idade: " + a.getAge()));


        // 4) Contar Por nome
        System.out.println("\n=== Contar Por nome ===");
        long contarNome = authorRepository.contarPorNome("Daniel");
        System.out.println("São ao todo: " + contarNome + " nome(s) encontrado(s)");
    }
}
```

### ✅ Quando usar `@NamedQueries`?

| Caso de uso                                   | Indicação               |
| --------------------------------------------- | ----------------------- |
| Lógicas de consulta repetidas                 | ✅                       |
| Consultas **complexas** com múltiplos filtros | ✅                       |
| Reuso entre diferentes pontos do projeto      | ✅                       |
| Deseja **validação antecipada** (compilação)  | ✅                       |
| Consulta muito simples (e pouco reutilizável) | ❌ — use `@Query` direto |


## 🧠 Dica extra (boas práticas)

* Use nomes descritivos, ex: `"Author.findByEmail"` → fácil de encontrar.
* Ideal para projetos maiores com muitas camadas (ex: DDD, arquitetura hexagonal).
* Ajuda a evitar **duplicação de lógica** nos serviços ou repositórios.


## É perfeitamente possível utilizar `@Modifying` junto com `@NamedQuery` ou `@NamedQueries`

**É perfeitamente possível usar `@Modifying` junto com `@NamedQuery` ou `@NamedQueries`** quando estiver definindo queries do tipo **`UPDATE` ou `DELETE`**.


### ✅ Como funciona

Por padrão, o Spring Data JPA trata `@Query` como **SELECT**. Para executar **modificações no banco de dados**, como `UPDATE` ou `DELETE`, é necessário indicar isso explicitamente com a anotação `@Modifying`.


### ✅ Exemplo completo com `@NamedQuery` + `@Modifying`

#### 👇 1. Definindo a `@NamedQuery` na entidade:

✅ `Entidade Author`

```java
@Entity
@Table(name = "AUTHOR_TBL")
@NamedQueries({
    @NamedQuery(
        name = "Author.updateAgeByEmail",
        query = "UPDATE Author a SET a.age = :age WHERE a.email = :email"
    ),
    @NamedQuery(
        name = "Author.deleteByAgeLessThan",
        query = "DELETE FROM Author a WHERE a.age < :age"
    )
})
public class Author {
    // campos...
}
```

#### 👇 2. Usando no repositório:

✅ `Repositório AuthorRepository`

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Modifying
    @Transactional
    @Query(name = "Author.updateAgeByEmail")
    int atualizarIdadePorEmail(@Param("age") int age, @Param("email") String email);

    @Modifying
    @Transactional
    @Query(name = "Author.deleteByAgeLessThan")
    int deletarPorIdadeMenorQue(@Param("age") int age);
}
```

✅ `Classe de teste NamedQueryModifyingRunner`
```java
@Component
public class NamedQueryModifyingRunner implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Criando autores
        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(25)
                .build();

        var author3 = Author.builder()
                .firstName("Carlos")
                .lastName("Silva")
                .email("carlos@gmail.com")
                .age(28)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3));

        System.out.println("\n=== Antes da atualização ===");
        authorRepository.findAll().forEach(a ->
                System.out.println("Nome: " + a.getFirstName() + " | Idade: " + a.getAge()));

        // Atualizar idade do Daniel
        int rowsUpdated = authorRepository.atualizarIdadePorEmail(40, "daniel@gmail.com");
        System.out.println("\nIdade atualizada. Linhas afetadas: " + rowsUpdated);

        // Deletar autores com idade menor que 30
        int rowsDeleted = authorRepository.deletarPorIdadeMenorQue(30);
        System.out.println("Autores deletados com idade < 30: " + rowsDeleted);

        System.out.println("\n=== Depois da modificação ===");
        authorRepository.findAll().forEach(a ->
                System.out.println("Nome: " + a.getFirstName() + " | Idade: " + a.getAge()));
    }
}
```

### ⚠️ Atenção:

* **@Modifying** é obrigatório para `UPDATE` e `DELETE`.
* **@Transactional** também é necessário, pois essas operações modificam o banco.


### ✅ Quando usar NamedQuery para update/delete?

Use `@NamedQuery` para **centralizar** as regras de negócio em um único lugar (na entidade), especialmente quando:

* Você quer **evitar duplicação** de queries.
* Precisa **reutilizar** essas instruções em múltiplos pontos do código.
* Está em um ambiente com **auditoria ou validações** específicas e quer manter regras agrupadas por entidade.

---
---

# O que são Projeções no Spring Data JPA?

**Projeções** permitem retornar **apenas os campos que você precisa**, em vez de carregar a entidade inteira com todos os relacionamentos e colunas.

Elas podem ser feitas de 3 formas principais:

1. **Projeção baseada em interface** ✅ (mais simples)
2. **Projeção baseada em classe (DTO)** 🧾
3. **Projeção dinâmica com `@Query` ou `SpEL`** 🔍


## 🔹 1. Projeção baseada em interface

### 🧩 Exemplo com a entidade `Author`

### 📦 Interface de projeção:

```java
public interface AuthorView {
    String getFirstName();
    String getEmail();
    int getAge();
}
```

> Dica: os métodos **devem ter exatamente os mesmos nomes** dos atributos da entidade.


### 📁 Repositório com método:

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    List<AuthorView> findByAgeGreaterThan(int age);

    @Query("SELECT a.firstName AS firstName, a.email AS email, a.age AS age FROM Author a WHERE a.firstName = :firstName")
    List<AuthorView> findByFirstName(String firstName);
}
```

**📌 Detalhe importante:**

  - Os aliases (AS firstName, AS lastName etc.) devem corresponder exatamente aos nomes dos métodos getters da interface AuthorView. Isso é obrigatório.

**🧠 Resumo**

| Erro comum                           | Correção                                                      |
| ------------------------------------ | ------------------------------------------------------------- |
| `SELECT a FROM AuthorView`           | ❌ `AuthorView` não é uma entidade                             |
| `SELECT a.firstName AS firstName...` | ✅ Use projeção baseada nos campos da entidade real (`Author`) |
| Falta de `AS nome`                   | ❌ Spring não consegue mapear para interface                   |
| Interface precisa de getters         | ✅ Os nomes devem bater exatamente com os aliases da query     |


### 🧪 Uso no `CommandLineRunner`:

```java
@Component
public class ProjectionExample implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var author1 = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .age(35)
                .build();

        var author2 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(31)
                .build();

        var author3 = Author.builder()
                .firstName("Fabiana")
                .lastName("Silva")
                .email("fabiana@gmail.com")
                .age(29)
                .build();

        var author4 = Author.builder()
                .firstName("Daniel")
                .lastName("Mota")
                .email("daniel.mota@gmail.com")
                .age(25)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3, author4));

        // 1) Exemplo 1: Buscar autores com idade menor ou igual a 30 anos
        List<AuthorView> authors = authorRepository.findByAgeLessThanEqual(32);

        // Exemplo de uso de projeção de atributos para obter apenas o primeiro nome, email e idade dos autores
        System.out.println("\n ==== Buscar autores com projeção de atributos ==== ");

        System.out.println("Total de autores encontrados com idade menor ou igual a 30 anos: " + authors.size());
        for (AuthorView a : authors) {
            System.out.println("Nome: " + a.getFirstName()
                    + " | Sobrenome: " + a.getEmail()
                    + " | Idade: " + a.getAge());
        }


        // 2) Buscar autore(s) pelo nome exato
        System.out.println("\n ==== Buscar autores pelo nome ==== ");
        
        List<AuthorView> authorsByName = authorRepository.findByFirstName("Daniel");
        
        System.out.println("Total de autores encontrados com nome 'Daniel': " + authorsByName.size());

        for (AuthorView a : authorsByName) {
            System.out.println("Nome: " + a.getFirstName()
                    + " | Email: " + a.getEmail());
        }

        /*
            // Ou pode fazer assim também:

            authors.forEach(a ->
            System.out.println(a.getFirstName() + " | " + a.getAge() + " | " + a.getEmail())
            );
        */ 

    }
}
```

### ✅ Vantagens dessa abordagem:

* Reduz o tráfego com o banco.
* Evita carregar dados e relacionamentos desnecessários.
* É **muito performática** e **fácil de usar**.

---

## 🔹 2. Projeção baseada em classe (DTO)

Pode retornar um DTO diretamente usando `@Query` com `new`.

### 🎯 Objetivo:
  - Criar um DTO AuthorDTO com apenas os campos firstName, lastName, email e age.

  - Utilizar uma consulta JPQL com **`new`** no repositório para preencher o DTO.

  - Exibir os dados no console com um CommandLineRunner.

### 🧾 DTO record personalizado:

```java
public record AuthorDTO(String firstName, String lastName, String email, int age) {}
```

**📌 Detalhe importante:**
  
  - O **`record`** é ótimo para criar objetos imutáveis de forma compacta (Java 16+).

### 🔍 Repositório:

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{
    
    @Query("SELECT new com.api.demo_data_jpa.dto.AuthorDTO(a.firstName, a.lastName, a.email, a.age) FROM Author a WHERE a.age > :age")
    List<AuthorDTO> buscarAutoresDTO(@Param("age") int age);
}
```

**📌 Detalhe importante:**

  - Se o caminho do pacote do AuthorDTO estiver errado vai ocasionar um erro de **`Could not resolve class 'AuthorDTO' named for instantiation`** isso acontece porque o JPQL precisa do nome completamente qualificado da classe DTO, ou seja, com o pacote completo no `SELECT new com.api.demo_data_jpa.dto.AuthorDTO(a.firstName, a.lastName, a.email, a.age)`.

  - ❗️ Nunca use apenas new AuthorDTO(...) sem o nome do pacote completo, a menos que o DTO esteja na mesma classe do repositório (o que não é prática recomendada).

### 🧪 Uso no `CommandLineRunner`:

```java
@Component
public class AuthorDTOExample implements CommandLineRunner{

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // Criando autores
        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(25)
                .build();

        var author3 = Author.builder()
                .firstName("Carlos")
                .lastName("Silva")
                .email("carlos@gmail.com")
                .age(28)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3));

        System.out.println("\n ==== Buscar autores com DTO ==== ");
        List<AuthorDTO> authorDTOs = authorRepository.buscarAutoresDTO(27);

        authorDTOs.forEach(dto -> System.out.println(
            "Nome: " + dto.firstName() 
            + " | Sobrenome: " + dto.lastName()
            + " | Email: " + dto.email()
            + " | Idade: " + dto.age()
        ));
    }
    
}
```

### ✨ Benefícios de usar DTO:

  - Evita carregar entidades completas e relacionamentos.

  - Melhora a performance em APIs públicas.

  - Garante segurança ao expor apenas os campos desejados.

---

## 🔹 3. Projeções Dinâmicas

Aqui, pode fazer **projeções dinâmicas** retornando diferentes interfaces no mesmo método com generics:

### 🔍 Repositório:

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{

    <T> List<T> findByAgeLessThan(int age, Class<T> type); 
}
```

### 🧪 Uso no `CommandLineRunner`:

```java
@Component
public class ProjectionExample implements CommandLineRunner {

        @Autowired
        private AuthorRepository authorRepository;

        @Override
        @Transactional
        public void run(String... args) throws Exception {

                var author1 = Author.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .email("john@gmail.com")
                                .age(35)
                                .build();

                var author2 = Author.builder()
                                .firstName("Daniel")
                                .lastName("Penelva")
                                .email("daniel@gmail.com")
                                .age(31)
                                .build();

                var author3 = Author.builder()
                                .firstName("Fabiana")
                                .lastName("Silva")
                                .email("fabiana@gmail.com")
                                .age(29)
                                .build();

                var author4 = Author.builder()
                                .firstName("Daniel")
                                .lastName("Mota")
                                .email("daniel.mota@gmail.com")
                                .age(25)
                                .build();

                authorRepository.saveAll(List.of(author1, author2, author3, author4));

                System.out.println("\n ==== Projeção Dinâmica ==== ");

                // 1) Projeção Dinâmica com o tipo de retorno AuthorView
                List<AuthorView> views = authorRepository.findByAgeLessThan(30, AuthorView.class);

                System.out.println("\n ==== Projeção Dinâmica com o tipo de retorno AuthorView ====");

                if (views.isEmpty()) {
                        System.out.println("Nenhum autor encontrado com idade menor que 30 anos.");
                        return;
                } else {
                        System.out.println("Total de autores encontrados com idade menor que 30 anos: " + views.size());
                        views.forEach(a -> System.out.println(
                                        "Nome: " + a.getFirstName()
                                        + " | Idade: " + a.getAge()));
                }


                // 2) Projeção Dinâmica com o tipo de retorno AuthorDTO
                List<AuthorDTO> dtos = authorRepository.findByAgeLessThan(35, AuthorDTO.class);

                System.out.println("\n ==== Projeção Dinâmica com o tipo de retorno AuthorDTO ====");

                if (dtos.isEmpty()) {
                        System.out.println("Nenhum autor encontrado com idade menor que 35 anos.");
                } else {
                        System.out.println("Total de autores encontrados com idade menor que 35 anos: " + dtos.size());

                        dtos.forEach(dto -> System.out.println(
                                        "Nome: " + dto.firstName()
                                        + " | Idade: " + dto.age()));
                }
        }
}
```

## ❗ Cuidados:

* Não funciona com métodos de relacionamento como `getCourses()` se não estiverem no `fetch`.
* Projeções são **somente leitura**. Elas **não podem ser usadas para persistência**.

---
---

# Componente Mapper

## 📚 Introdução

  - Um mapper é um padrão de projeto usado para mapear objetos de um domínio para outro, funcionando como uma ponte entre diferentes representações de dados.

  - Ele abstrai a complexidade da conversão, facilita a reutilização de código, aumenta a flexibilidade para mudanças e melhora a testabilidade.

  - Componente `Mapper` de forma geral é responsável por fazer a conversão entre objetos de domínio e objetos de transferência de dados. Ou seja, ele é responsável por fazer a conversão entre objetos que são usados para a persistência de dados e objetos que são usados para a transferência de dados entre os componentes da aplicação.

  - O componente mapper é geralmente entendido como uma ferramenta ou padrão para mapear dados entre diferentes objetos ou camadas, facilitando a conversão entre entidades, DTOs (Data Transfer Objects) e outras representações de dados dentro de uma aplicação.

  - Por exemplo, em aplicações que interagem com bancos de dados, mappers são usados para converter objetos de domínio em objetos de transferência de dados (DTOs) e vice-versa.


## Implementações utilizadas

✅ Entidade `Author`
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class Author extends BaseEntity{

    @Column(name = "first_name", nullable = false, length = 35)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @JsonProperty("last_name")
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private int age;
}
```

✅ Repositório `AuthorRepository`

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{
}
```

✅ record DTO `AuthorDTO`

```java
public record AuthorDTO(String firstName, String lastName, String email, int age) {
}
```

## Duas abordagens para implementar o `Mapper`:
    - Inline
    - Classe Mapper Separada

🆚 Comparativo
| Abordagem                       | Vantagem                                 | Limitação                               |
| ------------------------------- | ---------------------------------------- | --------------------------------------- |
| **Inline (como o seu exemplo)** | Rápido e direto para casos simples       | Dificulta reuso e testes unitários      |
| **Classe Mapper separada**      | Mais organizado, testável e reutilizável | Leve aumento de complexidade estrutural |

### 1. Exemplo de Implementação de Mapper Inline:

✨ A ideia de `mapper manual inline` é simples: aqui, pode criar um método que projeta um objeto de domínio para um objeto de transferência de dados (DTO) ou outro objeto de domínio. Isso pode ser feito usando o método `map()` do `Stream` ou o método `map()` do `Optional`. 

✨ Este exemplo não é uma boa prática, mas é uma abordagem simples para casos simples.

🔧 Versão com Mapper usando `map()` do `Stream`:

✅ Classe `AuthorComponentMapperExemple` usando `CommandLineRunner`

```java
@Component
public class AuthorComponentMapperExemple implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Criando autores
        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(25)
                .build();

        var author3 = Author.builder()
                .firstName("Carlos")
                .lastName("Silva")
                .email("carlos@gmail.com")
                .age(28)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3));

        List<Author> authors = authorRepository.findAll();

        // Convertendo para AuthorDTO usando o componente Mapper
        // (aqui é usado uma abordagem manual, mas poderia ser feito com MapStruct ou ModelMapper)
        //Exemplo 1 - Não é uma boa prática, mas é para fins de demonstração 
        List<AuthorDTO> authorDTOs = authors.stream()
                .map(author -> new AuthorDTO(
                        author.getFirstName(),
                        author.getLastName(),
                        author.getEmail(),
                        author.getAge()))
                .toList();
        
    }

}
```

### 2. Classe Mapper Separado

✨ A ideia aqui é criar uma classe mapper separada para fazer a conversão entre as entidades e os DTOs. Isso é uma boa prática pois mantém a responsabilidade de conversão em uma classe específica e facilita a manutenção.

🔧 Versão com Mapper separado:

✅ Classe Mapper `AuthorMapper`

```java
@Component
public class AuthorMapper {

    // Converte um Author para AuthorDTO
    public AuthorDTO toDTO(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorDTO(
                author.getFirstName(),
                author.getLastName(),
                author.getEmail(),
                author.getAge());
    }


    // Converte uma lista de Author para uma lista de AuthorDTO
    public List<AuthorDTO> toDTOList(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return List.of();
        }
        return authors.stream()
                .map(this::toDTO)
                .toList();
    }
}
```

✅ Classe `AuthorComponentMapperExemple` usando `CommandLineRunner`

```java
@Component
public class AuthorComponentMapperExemple implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Criando autores
        var author1 = Author.builder()
                .firstName("Daniel")
                .lastName("Penelva")
                .email("daniel@gmail.com")
                .age(37)
                .build();

        var author2 = Author.builder()
                .firstName("Maria")
                .lastName("Nunes")
                .email("maria@gmail.com")
                .age(25)
                .build();

        var author3 = Author.builder()
                .firstName("Carlos")
                .lastName("Silva")
                .email("carlos@gmail.com")
                .age(28)
                .build();

        authorRepository.saveAll(List.of(author1, author2, author3));

        List<Author> authors = authorRepository.findAll();

        // Exemplo 2 - Usando o AuthorMapper - uma boa prática separa a lógica de conversão em um componente Mapper
        List<AuthorDTO> authorDTOs = authorMapper.toDTOList(authors);

        authorDTOs.forEach(dto -> System.out.println(
            "Nome: " + dto.firstName()
            + " | Sobrenome: " + dto.lastName()
            + " | Email: " + dto.email()
            + " | Idade: " + dto.age()
        ));
    }
}
```

---
---

# Mapper utilizando MapStruct

  - O MapStruct é uma biblioteca poderosa que automatiza o mapeamento entre entidades e DTOs com base em convenções — sem precisar escrever código manual de conversão.

## Instalação MapStruct

✨ Link de instalação do [MapStruct](https://mapstruct.org/documentation/installation/)

✅ Adicionando a dependência do MapStruct - no Maven, adicionando no `pom.xml`:

```xml
...
	<properties>
		<java.version>21</java.version>
		<org.mapstruct.version>1.6.3</org.mapstruct.version>
	</properties>
	<dependencies>
		...
		<dependency>
			<groupId>org.mapstruct</groupId>
			<artifactId>mapstruct</artifactId>
			<version>${org.mapstruct.version}</version>
    	</dependency>
		
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>3.4.6</source> <!-- depending on your project -->
                	<target>3.4.6</target> <!-- depending on your project -->
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</path>

						<path>
							<groupId>org.mapstruct</groupId>
							<artifactId>mapstruct-processor</artifactId>
							<version>${org.mapstruct.version}</version>
                    	</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```
## Implementações utilizadas 

✅ Entidade `Author`
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class Author extends BaseEntity{

    @Column(name = "first_name", nullable = false, length = 35)
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @JsonProperty("last_name")
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private int age;
}
```

✅ Repositório `AuthorRepository`

```java
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{
}
```

✅ Record DTO `AuthorDTO`

```java
public record AuthorDTO(String firstName, String lastName, String email, int age) {

}
```

## Exemplo de uso de Mapper 

### 1. Sem o mapeamento de campo

💡 Conversão de `Author` para `AuthorDTO` sem mapeamento de campo: **`ENTIDADE → DTO`**

✅ Interface do Mapper `AuthorMapper`

```java
@Mapper
public interface AuthorMapper {

    // Este é um exemplo de como criar um mapper com MapStruct
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    // 1) ENTIDADE -> DTO
    // Converte uma entidade Author em um DTO AuthorDTO
    AuthorDTO toDto(Author author);

    // Converte uma lista de entidades Author em uma lista de DTOs AuthorDTO
    List<AuthorDTO> toDtoList(List<Author> authors);
    
}
```

⚠️ O método toDto converte uma entidade Author em um DTO AuthorDTO.

⚠️ Nota: Por padrão, MapStruct procura por nomes de campos iguais entre a entidade e o DTO.

⚠️ Explicação:
  - **Author:** Representa a entidade do domínio, contendo os dados completos e possivelmente relacionamentos com outras entidades.

  - **AuthorDTO:** é um objeto simplificado usado para transferir dados, por exemplo, entre camadas da aplicação ou para a interface do usuário, contendo apenas os campos necessários.

✅ Utilizando o mapper - Classe `AuthorMapperExample`

```java
@Component
public class AuthorMapperExample implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        List<Author> authors = List.of(
                // Criando autores
                Author.builder()
                        .firstName("Daniel")
                        .lastName("Penelva")
                        .email("daniel@gmail.com")
                        .age(37)
                        .build(),

                Author.builder()
                        .firstName("Maria")
                        .lastName("Nunes")
                        .email("maria@gmail.com")
                        .age(25)
                        .build(),

                Author.builder()
                        .firstName("Carlos")
                        .lastName("Silva")
                        .email("carlos@gmail.com")
                        .age(28)
                        .build());

        authorRepository.saveAll(authors);

        // 1) Exemplo 1 - Usando Mapstruct para mapear de Author para AuthorDTO
        List<AuthorDTO> authorDTOs = AuthorMapper.INSTANCE.toDtoList(authors);

        authorDTOs.forEach(dto -> System.out.println(
                "Nome: " + dto.firstName() +
                " | Sobrenome: " + dto.lastName() +
                " | Email: " + dto.email() +
                " | Idade: " + dto.age()
        ));

    }
}
```

### 2. Usando @Mapping para campos com nomes diferentes

Imagine agora que o AuthorDTO tem o campo emailAddress ao invés de email. O MapStruct não sabe disso automaticamente, então é usado o @Mapping:

📦 DTO com nome de campo diferente:

```java
public record AuthorDTO(String firstName, String lastName, String emailAddress, int age) {

}
```

🛠️ Mapper com **`@Mapping`**:

```java
@Mapper
public interface AuthorMapper {

    // Este é um exemplo de como criar um mapper com MapStruct
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    // 1) ENTIDADE -> DTO
    // Converte uma entidade Author em um DTO AuthorDTO
    @Mapping(source = "email", target = "emailAddress")  // Mapeia o campo email (vem da entidade) para emailAddress (vai para o DTO).
    AuthorDTO toDto(Author author);

    // Converte uma lista de entidades Author em uma lista de DTOs AuthorDTO
    List<AuthorDTO> toDtoList(List<Author> authors);
    
}
```

**`source = "email"`** vem da entidade
**`target = "emailAddress"`** vai para o DTO

✅ Utilizando o mapper - Classe `AuthorMapperExample`

```java
@Component
public class AuthorMapperExample implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        List<Author> authors = List.of(
                // Criando autores
                Author.builder()
                        .firstName("Daniel")
                        .lastName("Penelva")
                        .email("daniel@gmail.com")
                        .age(37)
                        .build(),

                Author.builder()
                        .firstName("Maria")
                        .lastName("Nunes")
                        .email("maria@gmail.com")
                        .age(25)
                        .build(),

                Author.builder()
                        .firstName("Carlos")
                        .lastName("Silva")
                        .email("carlos@gmail.com")
                        .age(28)
                        .build());

        authorRepository.saveAll(authors);

        // 1) Exemplo 1 - Usando Mapstruct para mapear de Author para AuthorDTO
        List<AuthorDTO> authorDTOs = AuthorMapper.INSTANCE.toDtoList(authors);

        authorDTOs.forEach(dto -> System.out.println(
                "Nome: " + dto.firstName() +
                " | Sobrenome: " + dto.lastName() +
                " | Email: " + dto.emailAddress() +
                " | Idade: " + dto.age()
        ));

    }
}
```

### 3. Usando @Mappings para mapeamento de entidades compostas / aninhadas

Suponha que a entidade Author tem um campo Address address, e no DTO você quer planificar os campos do endereço.

📦 Classe `Address`

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    private String streetName;
    private String houseNumber;
    private String zipCode;
    
}
```

📦 Classe `Author`

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class Author extends BaseEntity {
    // ...
    @Embedded
    private Address address;
}
```

📦 Record DTO `AuthorDTO` (endereços planificados)

```java
public record AuthorDTO(
        String firstName,
        String lastName,
        String emailAddress,
        int age,
        String streetName,
        String houseNumber,
        String zipCode) {
}
```

🛠️ Mapper com mapeamento de objeto aninhado (`AuthorMapper`) - utilizando `@Mappings`:

```java
@Mapper
public interface AuthorMapper {

    // Este é um exemplo de como criar um mapper com MapStruct
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    // 1) ENTIDADE -> DTO
    // Converte uma entidade Author em um DTO AuthorDTO
    @Mappings({ 
        @Mapping(source = "email", target = "emailAddress"),
        @Mapping(source = "address.streetName", target = "streetName"),
        @Mapping(source = "address.houseNumber", target = "houseNumber"),
        @Mapping(source = "address.zipCode", target = "zipCode")
    })
    AuthorDTO toDto(Author author);

    // Converte uma lista de entidades Author em uma lista de DTOs AuthorDTO
    List<AuthorDTO> toDtoList(List<Author> authors);
    
}
```

💡 Pode usar **`.address.streetName`** para acessar subcampos.

✅ Utilizando o mapper - Classe `AuthorMapperExample`

```java
@Component
public class AuthorMapperExample implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        List<Author> authors = List.of(
                // Criando autores
                Author.builder()
                        .firstName("Daniel")
                        .lastName("Penelva")
                        .email("daniel@gmail.com")
                        .age(37)
                        .address(new Address("Rua das Flores", "123", "12345-678"))
                        .build(),

                Author.builder()
                        .firstName("Maria")
                        .lastName("Nunes")
                        .email("maria@gmail.com")
                        .age(25)
                        .address(new Address("Avenida Brasil", "456", "98765-432"))
                        .build(),

                Author.builder()
                        .firstName("Carlos")
                        .lastName("Silva")
                        .email("carlos@gmail.com")
                        .age(28)
                        .address(new Address("Travessa da Alegria", "789", "54321-098"))
                        .build());

        authorRepository.saveAll(authors);

        // 1) Exemplo 1 - Usando Mapstruct para mapear de Author para AuthorDTO
        List<AuthorDTO> authorDTOs = AuthorMapper.INSTANCE.toDtoList(authors);

        authorDTOs.forEach(dto -> System.out.println(
                "Nome: " + dto.firstName() +
                " | Sobrenome: " + dto.lastName() +
                " | Email: " + dto.emailAddress() +
                " | Idade: " + dto.age() +
                " | Rua: " + dto.streetName() +
                " | Número: " + dto.houseNumber() +
                " | CEP: " + dto.zipCode()
        ));

    }
}
```

### 4. Conversão personalizada com @Named + @Mapping

Imagine que você quer transformar o campo firstName todo em maiúsculo no DTO.

🛠️ No Mapper - `AuthorMapper`:

```java
@Mapper
public interface AuthorMapper {

    // Este é um exemplo de como criar um mapper com MapStruct
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    // 1) ENTIDADE -> DTO
    // Converte uma entidade Author em um DTO AuthorDTO
    @Mappings({ 
        @Mapping(source = "email", target = "emailAddress"),
        @Mapping(source = "address.streetName", target = "streetName"),
        @Mapping(source = "address.houseNumber", target = "houseNumber"),
        @Mapping(source = "address.zipCode", target = "zipCode"),
        @Mapping(source = "firstName", target = "firstName", qualifiedByName = "toUpper")
    })
    AuthorDTO toDto(Author author);

    // Converte uma lista de entidades Author em uma lista de DTOs AuthorDTO
    List<AuthorDTO> toDtoList(List<Author> authors);

    @Named("toUpper")
    static String toUpperCase(String value) {
        return value != null ? value.toUpperCase() : null;
    }
    
}
```

✅ Utilizando o mapper - Classe `AuthorMapperExample`

```java
@Component
public class AuthorMapperExample implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        List<Author> authors = List.of(
                // Criando autores
                Author.builder()
                        .firstName("Daniel")
                        .lastName("Penelva")
                        .email("daniel@gmail.com")
                        .age(37)
                        .address(new Address("Rua das Flores", "123", "12345-678"))
                        .build(),

                Author.builder()
                        .firstName("Maria")
                        .lastName("Nunes")
                        .email("maria@gmail.com")
                        .age(25)
                        .address(new Address("Avenida Brasil", "456", "98765-432"))
                        .build(),

                Author.builder()
                        .firstName("Carlos")
                        .lastName("Silva")
                        .email("carlos@gmail.com")
                        .age(28)
                        .address(new Address("Travessa da Alegria", "789", "54321-098"))
                        .build());

        authorRepository.saveAll(authors);

        // 1) Exemplo 1 - Usando Mapstruct para mapear de Author para AuthorDTO
        List<AuthorDTO> authorDTOs = AuthorMapper.INSTANCE.toDtoList(authors);

        authorDTOs.forEach(dto -> System.out.println(
                "Nome em formato UpperCase: " + dto.firstName() +
                " | Sobrenome: " + dto.lastName() +
                " | Email: " + dto.emailAddress() +
                " | Idade: " + dto.age() +
                " | Rua: " + dto.streetName() +
                " | Número: " + dto.houseNumber() +
                " | CEP: " + dto.zipCode()
        ));

    }
}
```

### 5. Mapeamento reverso (DTO → Entity)

  - Transforma os dados simplificados do DTO em uma entidade do domínio, que pode ser persistida no banco ou usada internamente na aplicação.

✅ Etapas da conversão reversa

🛠️ Atualizando o `AuthorMapper` com o método reverso:

```java
@Mapper
public interface AuthorMapper {

    // 2) DTO -> ENTIDADE
    // Converte um DTO AuthorDTO em uma entidade Author
    @InheritInverseConfiguration  // Inverte o mapeamento do método toDto 
    Author toEntity(AuthorDTO authorDTO);

    List<Author> toEntitList(List<AuthorDTO> authorDTOs);

}
```

🔁 @InheritInverseConfiguration reaproveita automaticamente o mapeamento reverso.

✅ Utilizando o mapper - Classe `AuthorMapperExample`

```java
@Component
public class AuthorMapperExample implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 2) Exemplo 2 - Usando Mapstruct para mapear de AuthorDTO para Author

        AuthorDTO dto1 = new AuthorDTO("Ana", "Souza", "ana@gmail.com", 30, "Rua das Palmeiras", "321", "98765-432");          

        AuthorDTO dto2 = new AuthorDTO("Pedro", "Oliveira", "pedro@gmail.com", 40, "Avenida Central", "654", "12345-678");

        AuthorDTO dto3 = new AuthorDTO("Flávia", "Nunes", "flavia@gmail.com", 45, "Avenida das Américas", "456", "98990-889");

        // Convrtendo AuthorDTO para Author (entidade)
        Author author1 = AuthorMapper.INSTANCE.toEntity(dto1);
        Author author2 = AuthorMapper.INSTANCE.toEntity(dto2);
        Author author3 = AuthorMapper.INSTANCE.toEntity(dto3);

        System.out.println("\n Exemplo 2 - Usando Mapstruct para mapear de AuthorDTO para Author");

        // Imprimindo os dados dos autores convertidos
        for (Author author : List.of(author1, author2, author3)) {
            
            authorRepository.save(author);  // Salvando os autores convertidos no banco de dados

            System.out.println("Nome: " + author.getFirstName()
                + " | Sobrenome: " + author.getLastName()
                + " | Idade: " + author.getAge()
                + " | Email: " + author.getEmail()
                + " | Rua: " + author.getAddress().getStreetName()
                + " | Número: " + author.getAddress().getHouseNumber()
                + " | CEP: " + author.getAddress().getZipCode());
        }
    }
}
```

🧠 Resumo
  - toDto() → transforma uma entidade em DTO (útil para envio via API).

  - toEntity() → transforma um DTO recebido em entidade (útil para salvar no banco).

  - Com @InheritInverseConfiguration, evitamos reescrever os mapeamentos manuais.

| Método                      | Parâmetro       | Retorno       | Função                                    |
|-----------------------------|-----------------|---------------|-------------------------------------------|
| `AuthorDTO toDto(Author a)` | `Author`        | `AuthorDTO`   | Converte entidade para DTO                |
| `Author toEntity(AuthorDTO d)` | `AuthorDTO`     | `Author`      | Converte DTO para entidade       

---

## Feito por: `Daniel Penelva de Andrade`