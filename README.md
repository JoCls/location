Modèle C4

![image](https://github.com/user-attachments/assets/c3b97e4c-14bc-4309-8800-7f4e4e91144d)

Architecture Hexagonale

![image2](https://github.com/user-attachments/assets/e4fab2aa-5c12-433f-8dfc-4e68277c4073)

Schéma base de données

![image3](https://github.com/user-attachments/assets/9c520931-0290-4079-a544-d36b94342db0)

Liste problèmes rencontrés : 

  - Regarding MySQL & Flyway configuration :
    Fixed spring.properties for hibernate dialect that was not in the correction version. Had to use the MySQL8Dialect instead of MySQL5Dialect to match my MySQL DB version
    Added the baseline on migration config for Flyway to spring.properties that was missing and causing issue
    Realised that using Flyway version 10+ caused an issue with missing new dependencies required for using MySql. Had to add "implementation("org.flywaydb:flyway-mysql")" to my build.gradle file
  - Added Lombok anotation @builder for easier testing. It was causing an issue by forcing to use the AllArgs Constructor with the "id" field in our test cases for DB integration.
  - I had issue with Spring using the default UserDetailsService resulting in unexpected behaviour regarding user rights in my application.
    Issue was fixed by specifying a custom UserDetailsService in the http configuration of SecurityFilterChain
  - I encountered A LOT of issues with trying to make use of Spring Session mangement but there were too much conflicts and weird behaviour with the React part.
    I decided to swap to a JWT token implementation, easier and more reliable
  - Had a bug that would allow users to manually pass by the login page and still have access to everything.
    I Added a context handler that made sure user would be redirected to the login page if they were not authenticated
  - Removed incorrect anotation in Item with a @OneToMany that caused a recursive call on reservation, making my JSON body infinite, dumb mistake
  - Forgot to define default starting page of my application to /login, so user would start the app and wait on a full blank page on "/"
    I added an auto redirect in my ProtectedRoute and AuthContext logic to avoid this
  - Had issues trying to run the full app on the backend side with included frontend ressources under /static/ but didn't work out. Had to deal with lot of conflict access so I guess it was a bad design call.
    Went back to the back running on port :8080 and front on :3000, now everything is smooth
  - Had issues trying to delete users via the admin panel caused by the fact that the DELETE CASCADE was not configured. Making it impossible to delete users with existing reservation due to the use of the foreign key from table Reservation
  - Had conflict in my backend call with frontend because the JSON payload had an incorrect field "type" instead of "itemType"
    Corrected it by respecting my DTO/Entities definition

URL Dépôt Git

https://github.com/JoCls/location/tree/dev
