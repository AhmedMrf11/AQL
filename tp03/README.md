# TP03 – Tests d'intégration – Partie 1

## Structure du projet

```
tp03/
└── src/
    ├── main/java/org/example/
    │   └── partie01/
    │       ├── exercice1/   (User, UserRepository, UserService)
    │       ├── exercice2/   (Order, OrderDao, OrderService, OrderController)
    │       └── exercice3/   (Product, ProductApiClient, ProductApiException, ProductService)
    └── test/java/org/example/
        └── partie01/
            ├── exercice1/   (UserServiceTest)
            ├── exercice2/   (OrderControllerTest)
            └── exercice3/   (ProductServiceTest)
```

---

## Exercice 1 – Interaction Simple entre Modules

**Scénario :** `UserService` récupère un utilisateur depuis `UserRepository`.

### Q : Pourquoi utilise-t-on un mock pour `UserRepository` au lieu d'une vraie implémentation ?

**R :** Un mock permet d'isoler le comportement de `UserService` sans dépendre d'une vraie base de données ou d'une implémentation concrète. Le test vérifie uniquement la logique de `UserService` : qu'il appelle bien `findUserById` avec le bon argument et retourne le résultat qu'il reçoit. C'est le principe de l'isolation dans les tests d'intégration modulaire (test d'interaction entre deux modules).

### Q : Que vérifie `verify(mockRepo).findUserById(1L)` ?

**R :** Cette assertion vérifie que la méthode `findUserById` a été appelée exactement une fois avec l'argument `1L`. Si `UserService.getUserById` n'avait pas délégué l'appel au repository, le test échouerait, signalant un problème dans la chaîne d'interaction.

---

## Exercice 2 – Interaction avec une Base de Données avec des Mocks

**Scénario :** `OrderController` → `OrderService` → `OrderDao`.

### Q : Pourquoi mocker à la fois `OrderService` et `OrderDao` ?

**R :** Le test de `OrderController` doit uniquement vérifier que le contrôleur délègue correctement à `OrderService`. Mocker `OrderService` isole ce comportement. Pour vérifier la chaîne complète (service → DAO), on instancie un vrai `OrderService` avec un `OrderDao` mocké — ce qui teste l'intégration entre ces deux couches indépendamment du contrôleur.

### Q : Quel est le rôle d'un DAO (Data Access Object) ?

**R :** Le DAO encapsule la logique d'accès à la base de données. Il offre une interface abstraite entre la couche métier et le stockage persistant, ce qui facilite les tests (en le moquant) et la maintenance (en changeant la technologie de persistence sans toucher au reste du code).

---

## Exercice 3 – Intégration d'API avec Mocking

**Scénario :** `ProductService` récupère un produit via `ProductApiClient` (client d'API externe).

### Q : Quels sont les trois scénarios testés ?

| Scénario | Description | Exception attendue |
|---|---|---|
| Succès | L'API retourne un produit valide | Aucune |
| Format incompatible | La réponse de l'API ne peut pas être parsée | `IllegalArgumentException` |
| Échec d'appel | L'API est indisponible (ex. HTTP 503) | `ProductApiException` |

### Q : Pourquoi créer une exception personnalisée `ProductApiException` ?

**R :** Une exception métier spécifique (`ProductApiException`) permet de distinguer clairement les erreurs de l'API des autres erreurs runtime. Elle facilite la gestion différenciée des erreurs par les couches supérieures (ex. afficher un message "service indisponible" plutôt qu'une erreur générique), et améliore la lisibilité du code et des tests.

### Q : Comment Mockito simule-t-il les échecs d'API ?

**R :** On utilise `when(mockClient.getProduct("P99")).thenThrow(new ProductApiException(...))`. Cela instruit Mockito à lancer l'exception lorsque la méthode est appelée avec cet argument précis. `assertThrows` dans JUnit 5 capture ensuite cette exception et vérifie son type et son message.

---

## Lancer les tests

```bash
mvn test
```

## Dépendances

- **JUnit 5** (`junit-jupiter:5.10.0`) – framework de test
- **Mockito** (`mockito-core:5.5.0`) – création de mocks

---

# TP03 – Tests d'intégration – Partie 2

## Exercice 2: Tests d'intégration avec Testcontainers Java

**1. Analyse des tests existants:**
* **Tests d'intégration existants:** Le projet original `task-manager` contient principalement des tests unitaires (`TaskControllerTest`, `TaskServiceImplTest`) qui utilisent l'approche basée sur des objets simulés avec Mockito (`@Mock`, `@InjectMocks`). Il n'y a qu'un seul test d'intégration `TaskManagerApplicationTests.java` qui charge le contexte Spring Boot complet (`@SpringBootTest`), mais qui utilise une base de données en mémoire (H2) définie dans le `pom.xml`.
* **Approches utilisées:** Isolation logique avec Mockito pour les contrôleurs et les services; Environnement en mémoire (H2) pour tester le chargement de contexte de Spring.
* **Limitations des tests existants:** 
  - La base de données en mémoire (H2) ne simule pas fidèlement l'environnement de production (les syntaxes SQL spécifiques de MySQL, la gestion des contraintes et les triggers sont différents).
  - Ces tests peuvent produire des faux positifs (un test qui passe sur H2 mais échoue lors de l'exécution d'une vraie requête SQL sur la base de production).
  - La fiabilité sur la validation des connexions réelles et des transactions de la base de données est limitée.

**2. Analyse et Comparaison (Testcontainers vs Tests originaux):**
* **Couverture:** Testcontainers permet une couverture nettement supérieure en incluant le comportement réel de la base de données MySQL.
* **Fiabilité:** La fiabilité est grandement accrue grâce à la création d'environnements iso-production. S'il y a un défaut de dialecte MySQL ou d'ORM Hibernate, Testcontainers permettra de le détecter pendant les tests d'intégration.
* **Lisibilité et Maintenabilité:** La configuration des tests devient plus robuste (`@ClassRule MySQLContainer`) et supprime le besoin de maintenir des scripts H2 et MySQL séparément.
* **Avantages:** 
  - Similitude parfaite entre l'environnement de test et de production.
  - Le cycle de vie de la base de données de test est complétement géré depuis le code Java (démarrage/arrêt automatiques).
  - Aucun besoin d'installer MySQL sur la machine hôte des développeurs ou sur le pipeline CI/CD, Testcontainers s'occupe de tout via Docker.
* **Inconvénients:** 
  - Le temps d'exécution des tests est plus lent (latence au démarrage des conteneurs isolés Docker).
  - Testcontainers ajoute une dépendance lourde : il exige imperceptiblement que le démon Docker s'exécute correctement sur l'hôte, ce qui complique les configurations sur certaines machines (comme sous Windows sans WSL correctement configuré).
