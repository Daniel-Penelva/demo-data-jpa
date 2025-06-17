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




















