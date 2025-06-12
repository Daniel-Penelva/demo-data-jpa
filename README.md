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

# Relacioanmento Unidirecional de Um-Para-Um (OneToOne)

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

