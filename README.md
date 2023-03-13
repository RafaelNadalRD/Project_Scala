# Project_Scala
Le projet du S1

Etape 1: Executez  le fichier "Script/Main.scala" une première fois pour le compiler avec la commande Cli après avoir navigué dans son dossier avec la commande "cd":
	 scala-cli run .\Main.scala


TO DO LISTE:
- Expliquer dans le readme.md que la fonction update_pixel modifie le pixel avec des coordonnées commençant par 0 (donc  	modifie le pixel 2.2)

New canvas:

Cette implémentation vérifie que la liste d'arguments passée en paramètre contient bien 3 éléments, sinon elle renvoie un statut d'erreur.
Ensuite, la largeur, la hauteur et le caractère par défaut pour les pixels sont extraits des arguments passés en paramètre.
On crée alors une nouvelle Vector de Pixels en utilisant la méthode fill de la classe Vector. Cette méthode permet de remplir une Vector avec des éléments générés par une fonction donnée. Ici, on génère une Vector de taille height, où chaque élément est une Vector de taille width, et où chaque élément est un Pixel initialisé avec les valeurs x=0, y=0 et le caractère par défaut.
On copie ensuite la Canvas passée en paramètre en modifiant sa largeur, hauteur et pixels avec les valeurs calculées précédemment.
Finalement, la nouvelle Canvas ainsi créée est renvoyée avec un Statut vide, qui indique que tout s'est bien passé.
