📜 README.md

# 🌳 Mapeamento de Áreas Verdes de Joinville 🌿

## 📌 Sobre o Projeto

O **Mapeamento de Áreas Verdes de Joinville** é um sistema interativo que permite **visualizar, avaliar e cadastrar** áreas verdes na cidade de Joinville.

🌱 **Objetivo:** Incentivar a conscientização ambiental e facilitar o acesso a informações sobre espaços naturais disponíveis para a comunidade.

---

## 🚀 Funcionalidades

✅ **Listar** todas as áreas verdes cadastradas

✅ **Avaliar** áreas verdes com critérios ambientais

✅ **Visualizar detalhes** de uma área verde específica

✅ **Cadastrar novas áreas verdes** com localização e horário de funcionamento

✅ **Listar todas as localizações** das áreas verdes

---

## 🛠️ Tecnologias Utilizadas

-   **Java** (Linguagem principal)

-   **Scanner** (Entrada de dados interativa no terminal)

-   **Collections API** (`List<>` para armazenar dados)

---

## 📂 Estrutura do Projeto

```
MapeamentoAreasVerdes/
│── src/main/java/br/com/joinville/mapa/
│   ├── MainTerminal.java                  # Classe principal do sistema (versão rodando no terminal)
│   ├── Main.java                  # Classe principal do sistema (versão rodando na web)
│   ├── AreaVerde.java              # Modelo para uma área verde
│   ├── Localizacao.java            # Modelo para localização geográfica
│   ├── Avaliacao.java               # Modelo para avaliações das áreas verdes
│   ├── AreaVerdeRepository.java    # Repositório de áreas verdes
│   ├── LocalizacaoRepository.java  # Repositório de localizações
│   ├── AvaliacaoRepository.java     # Repositório de avaliações
│
├──  pom.xml                         # Arquivo de configuração do Maven
├── target                           # Pasta onde os arquivos compilados são armazenados
└── README.md                        # Documentação do projeto

```

---

# 🖥️ **Rodando a versão via terminal**

## ▶️ Como Executar o Projeto

### 🔹 **Pré-requisitos**

✔️ **Java 8 ou superior** instalado

✔️ Editor de código (**IntelliJ IDEA, Eclipse, VS Code**)

✔️ Terminal ou Console para rodar o programa

### 🏗️ **Passos para executar**

1️⃣ **Clone o repositório**

```
git clone git@github.com:AndressaDaCosta/MapeamentoAreasVerdes.git
```

2️⃣ **Acesse o diretório do projeto**

```
cd MapeamentoAreasVerdes
```

3️⃣ **Remove todos os arquivos .class gerados anteriormente**

```
rm -rf src/main/java/br/com/joinville/mapa/*.class
```

4️⃣ **Compile o projeto**

```
 javac src/main/java/br/com/joinville/mapa/*.java
```

5️⃣ **Execute o projeto**

```
java -cp src/main/java br.com.joinville.mapa.MainTerminal
```

---

## 📜 Exemplo de Uso

### **📌 Menu Principal**

Após executar o programa, o usuário verá um menu interativo no terminal:

```
🌳 Bem-vindo ao Mapeamento de Áreas Verdes de Joinville! 🌿
Digite a opção que deseja acessar:
1- Listar Áreas Verdes
2- Avaliar Área Verde
3- Ver detalhe de uma Área Verde
4- Cadastrar nova Área Verde
5- Listar Localizações
0- Sair
Opção:
```

---

### **📍 Exemplo de Cadastro de Nova Área Verde**

```
📍 Cadastro de Nova Área Verde:
Nome da área verde: Parque Central
Tipo de vegetação (Árvores, Arbustos, Gramado, etc.): Árvores
Horário de funcionamento (exemplo: 08:00 - 20:00): 08:00 - 18:00

🌍 Exemplo de coordenadas válidas:
Joinville: -26.3044, -48.8489
São Paulo: -23.5505, -46.6333

Latitude (-90 a 90): -26.3044
Longitude (-180 a 180): -48.8489

✅ Área verde cadastrada com sucesso!
```

---

## 📌 Validações Implementadas

🔹 **Entrada de IDs**: Apenas números inteiros positivos são aceitos.

🔹 **Notas de Avaliação**: Apenas valores de `1 a 5` são permitidos.

🔹 **Formato de Horário**: `HH:MM - HH:MM` (exemplo: `08:00 - 18:00`).

🔹 **Latitude e Longitude**: Devem estar dentro dos intervalos geográficos corretos.

---

## 🚀 **Rodando na versão WEB**

Se deseja rodar o **servidor web** e acessar a aplicação pelo navegador:

### **1️⃣ Clone o repositório**

```
git clone git@github.com:AndressaDaCosta/MapeamentoAreasVerdes.git
```

### **2️⃣ Acesse o diretório do projeto**

```
cd MapeamentoAreasVerdes
```

### **3️⃣ Compile o projeto**

```
mvn clean compile
```

### **4️⃣ Execute o projeto na versão web**

```
mvn exec:java -Dexec.mainClass="br.com.joinville.mapa.Main"
```

Após isso, acesse no navegador:

➡️ [**http://localhost:8080**](http://localhost:8080/)

---

---

## 📌 Possíveis Melhorias Futuras

⏳ **Persistência de dados** (atualmente, os dados são armazenados apenas em memória)

⏳ **Interface gráfica** para interação mais amigável

⏳ **Banco de dados** para armazenamento permanente das áreas verdes

---

## 👩🏻‍💻 Desenvolvido por

👩‍💻 [**Andressa Costa**](https://github.com/AndressaDaCosta) 🚀
