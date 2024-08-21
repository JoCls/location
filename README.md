Modèle C4

![image](https://github.com/user-attachments/assets/c3b97e4c-14bc-4309-8800-7f4e4e91144d)

Architecture Hexagonale

![image2](https://github.com/user-attachments/assets/e4fab2aa-5c12-433f-8dfc-4e68277c4073)

Schéma base de données

![image3](https://github.com/user-attachments/assets/9c520931-0290-4079-a544-d36b94342db0)

Liste problèmes rencontrés : 

  - En ce qui concerne la configuration de MySQL et Flyway :
    Correction de spring.properties pour le dialecte hibernate qui n'était pas dans la bonne version. J'ai dû utiliser le dialecte MySQL8 au lieu du dialecte MySQL5 pour correspondre à la version de ma base de données MySQL.
    Ajout de la ligne de base de la configuration de migration pour Flyway dans spring.properties qui était manquante et causait des problèmes.
    J'ai réalisé que l'utilisation de la version 10+ de Flyway causait un problème avec l'absence de nouvelles dépendances nécessaires à l'utilisation de MySql. J'ai dû ajouter « implementation( »org.flywaydb:flyway-mysql« ) » à mon fichier build.gradle.
  - Ajout de l'anotation Lombok @builder pour faciliter les tests. Elle causait un problème en forçant l'utilisation du constructeur AllArgs avec le champ « id » dans nos cas de test pour l'intégration de la base de données.
  - J'avais un problème avec Spring qui utilisait le UserDetailsService par défaut, ce qui entraînait un comportement inattendu concernant les droits des utilisateurs dans mon application.
    Le problème a été résolu en spécifiant un UserDetailsService personnalisé dans la configuration http de SecurityFilterChain.
  - J'ai rencontré BEAUCOUP de problèmes en essayant d'utiliser la gestion des sessions Spring, mais il y avait trop de conflits et de comportements bizarres avec la partie React.
    J'ai décidé de passer à une implémentation de jetons JWT, plus simple et plus fiable.
  - Il y avait un bug qui permettait aux utilisateurs de passer manuellement la page de connexion tout en ayant accès à tout.
    J'ai ajouté un gestionnaire de contexte qui s'assure que l'utilisateur est redirigé vers la page de connexion s'il n'est pas authentifié.
  - Suppression d'une anotation incorrecte dans Item avec un @OneToMany qui provoquait un appel récursif sur la réservation, rendant mon corps JSON infini, erreur stupide.
  - J'ai oublié de définir la page de démarrage par défaut de mon application à /login, ce qui fait que l'utilisateur démarre l'application et attend une page vide sur « / »
    J'ai ajouté une redirection automatique dans ma logique ProtectedRoute et AuthContext pour éviter cela.
  - J'ai eu des problèmes en essayant de faire tourner l'application complète du côté du backend avec les ressources du frontend incluses dans /static/ mais cela n'a pas fonctionné. J'ai dû faire face à de nombreux conflits d'accès et je pense que c'était un mauvais choix de conception.
    Le Back fonctionne sur le port :8080 et le Front sur :3000, maintenant tout va bien.
  - Problèmes lors de la suppression d'utilisateurs via le panneau d'administration, dus au fait que la fonction DELETE CASCADE n'était pas configurée. Impossibilité de supprimer des utilisateurs ayant une réservation existante en raison de l'utilisation de la clé étrangère de la table Réservation.
  - J'ai eu un conflit dans mon appel backend avec le frontend parce que la charge utile JSON avait un champ incorrect « type » au lieu de « itemType »
    Je l'ai corrigé en respectant la définition de mes DTO/Entités.

URL Dépôt Git

https://github.com/JoCls/location/tree/dev
