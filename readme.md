# Optimod'Lyon - Application de Gestion de Tournées

## 📋 Description du Projet

Optimod'Lyon est une application de planification de tournées de livraison à vélo développée dans le cadre du Projet Longue Durée (PLD) de l'INSA Lyon (4IF). L'application permet aux sociétés de livraison de préparer et optimiser leurs tournées en tenant compte de contraintes de précédence (Pickup & Delivery).

### Contexte

Ce projet s'inscrit dans le cadre d'**Optimod'Lyon**, un projet financé par l'ADEME (2012-2015) porté par le Grand Lyon avec 13 partenaires, visant à optimiser la mobilité urbaine durable.

## 🎯 Fonctionnalités Principales

L'application permet de :

- **Charger un plan** : Import d'une carte de la ville depuis un fichier XML contenant les intersections (latitude/longitude) et les tronçons (longueur, nom de rue)
- **Charger des demandes de livraison** : Import d'un programme de Pickup & Delivery depuis un fichier XML
- **Calculer une tournée optimale** : Calcul automatique de l'itinéraire minimisant la distance totale tout en respectant les contraintes de précédence
- **Modifier interactivement** : Ajout/suppression de livraisons, modification de l'ordre de passage, calcul des horaires de passage
- **Visualiser la tournée** : Affichage graphique du parcours sur la carte avec les points de pickup (carrés) et delivery (ronds)

## 🚴 Spécificités du Service Pickup & Delivery

Chaque demande de livraison comprend :
- Un **point d'enlèvement (pickup)** avec une durée d'opération
- Un **point de livraison (delivery)** avec une durée d'opération
- **Contrainte de précédence** : le pickup doit obligatoirement être visité avant le delivery correspondant

L'algorithme calcule automatiquement les heures de passage pour prévenir les clients.

## 🛠️ Architecture Technique

### Technologies Utilisées

- **Langage** : Java
- **Interface Graphique** : JavaFX
- **Build Tool** : Maven
- **Tests** : JUnit
- **Couverture de tests** : IntelliJ IDEA
- **Gestion de versions** : Git

### Architecture MVC

```
┌─────────────────────────────────────────────────┐
│                   MODÈLE                        │
│  - Classes métier                               │
│  - Algorithmes (TSP)                            │
│  - Plan, Intersections, Tronçons                │
│  - Demandes de Pickup & Delivery                │
└─────────────────────────────────────────────────┘
                      ↕
┌─────────────────────────────────────────────────┐
│                CONTRÔLEUR                       │
│  - Gestion des interactions utilisateur         │
│  - Coordination Modèle-Vue                      │
│  - Algorithmes (A-star)                         │
└─────────────────────────────────────────────────┘
                      ↕
┌─────────────────────────────────────────────────┐
│                     VUE                         │
│  - JavaFX                                       │
│  - Affichage du plan                            │
│  - Visualisation de la tournée                  │
└─────────────────────────────────────────────────┘
```

## 🧮 Algorithmes de Calcul de Tournée

Le calcul d'une tournée s'effectue en deux étapes :

### Étape 1 : Calcul du Graphe des Plus Courts Chemins
- **Entrée** : Ensemble de n points Pickup et Delivery + plan de la ville
- **Sortie** :  Graphe complet orienté avec 1 sommet par point
- **Algorithme** : A* (avec heuristique euclidienne)

### Étape 2 :  Résolution du TSP (Traveling Salesman Problem)
- **Entrée** :  Graphe complet orienté
- **Sortie** :  Ordre de visite optimal respectant les contraintes de précédence
- **Approche** : Programmation dynamique (algorithme de Held-Karp)
    - Utilisation de bitmasks pour représenter les ensembles de sommets
    - Mémorisation des sous-problèmes avec HashMap
    - Contrainte :  le pickup doit être visité avant son delivery correspondant
    - Complexité : $O(n^2 \times 2^n)$


## 📦 Installation et Exécution

Il est également possible de télécharger une version précompilée (Windows, Linux, MacOS) dans la section "Releases" du dépôt GitHub.

### Prérequis
- Java JDK 11 ou supérieur
- Maven 3.6+

### Compilation et Exécution

```bash
# Cloner le repository
git clone https://github.com/alex-ddr/optimod-lyon.git
cd optimod-lyon

# Compiler le projet
mvn clean compile

# Exécuter l'application
mvn javafx:run

# Créer un JAR exécutable
mvn package
java -jar target/optimod-lyon. jar
```

### Tests

Vous pouvez trouver les tests unitaires dans le dossier `src/test/java`.
La couverture des tests peut être trouvée dans le dossier `docs/`

## 📁 Structure du Projet

```
optimod-lyon/
├── src/
│   ├── main/
│   │   ├── java/          # Code source Java
│   │   └── resources/     # Fichiers de ressources
│   └── test/
│       └── java/          # Tests unitaires
├── public/                # Fichiers XML de test (plans, demandes)
├── out/                   # Fichiers de sortie
├── docs/                  # Dossier de conception, burnup chart, glossaire, guide utilisateur, couverture de tests...
├── pom.xml                # Configuration Maven
└── README. md
```

## 📝 Conventions de Nommage

- **Langue** : Français
- **Classes** : PascalCase (ex: `AccueilControleur`)
- **Méthodes** : camelCase (ex: `preparerPlanTournee()`)
- **Variables** : camelCase (ex: `pointPickup`)
- **Fichiers** : kebab-case (ex: `item-point.fxml`)

## 🔄 Méthodologie Agile

Le projet est développé selon une méthodologie Agile avec :
- **Itérations** : Sprints de développement successifs
- **Principes** : Transparence, Inspection, Adaptation
- **Pratiques** : Rétrospectives, burnup charts
- **Livrables** : User stories, diagrammes UML, JAR exécutable, guide utilisateur

## 🎓 Équipe et Contexte Académique

- **Établissement** : INSA Lyon
- **Formation** : 4IF
- **Encadrants** : Frédérique Laforest

## 📄 Documentation

- **Diagrammes UML** : Disponibles dans le dossier de conception

## 🚀 Simplifications par Rapport au Problème Réel

Pour des raisons pédagogiques, le projet intègre certaines simplifications :
- Vitesse constante (pas de variation selon le tronçon ou l'heure)
- Seules les contraintes de précédence sont considérées
- Tournée statique (pas d'adaptation en temps réel)
- Un seul livreur (pas de VRP multi-véhicules)
- Pas de contraintes de capacité ou de fenêtres horaires

---

*Projet développé dans le cadre du cursus ingénieur INSA Lyon - Département Informatique*
