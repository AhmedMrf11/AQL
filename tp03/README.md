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
