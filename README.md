# Project_Scala
Le projet du S1

Etape 1: Executez  le fichier "Script/Main.scala" une première fois pour le compiler avec la commande Cli après avoir navigué dans son dossier avec la commande "cd":
	 scala-cli run .\Main.scala


TO DO LISTE:
- Expliquer dans le readme.md que la fonction update_pixel modifie le pixel avec des coordonnées commençant par 0 (donc  	modifie le pixel 2.2)

La méthode update_pixel prend en entrée une séquence de chaînes de caractères action et une instance de la case class Canvas. Elle vérifie d'abord que la longueur de l'action est suffisante pour mettre à jour un pixel, sinon elle renvoie une instance du case class Status avec un message d'erreur. Elle essaie ensuite de convertir les deux premiers éléments de l'action en entiers (x et y) et le troisième élément en caractère (la couleur du pixel à mettre à jour). Si la conversion réussit, elle appelle la méthode update de la case class Canvas avec un nouveau Pixel contenant les coordonnées et la couleur spécifiées, met à jour l'instance de la case class Canvas et renvoie une nouvelle instance de la case class Status sans message d'erreur. Si la conversion échoue ou si les coordonnées du pixel à mettre à jour sont hors des limites du canvas, elle renvoie une instance de la case class Status avec un message d'erreur.

New canvas:

Cette implémentation vérifie que la liste d'arguments passée en paramètre contient bien 3 éléments, sinon elle renvoie un statut d'erreur.
Ensuite, la largeur, la hauteur et le caractère par défaut pour les pixels sont extraits des arguments passés en paramètre.
On crée alors une nouvelle Vector de Pixels en utilisant la méthode fill de la classe Vector. Cette méthode permet de remplir une Vector avec des éléments générés par une fonction donnée. Ici, on génère une Vector de taille height, où chaque élément est une Vector de taille width, et où chaque élément est un Pixel initialisé avec les valeurs x=0, y=0 et le caractère par défaut.
On copie ensuite la Canvas passée en paramètre en modifiant sa largeur, hauteur et pixels avec les valeurs calculées précédemment.
Finalement, la nouvelle Canvas ainsi créée est renvoyée avec un Statut vide, qui indique que tout s'est bien passé.

Draw_line:

La fonction vérifie d'abord si la ligne est horizontale ou verticale. Si la ligne est horizontale (les coordonnées y sont égales), elle parcourt les coordonnées x entre le premier et le second point et met à jour la couleur de chaque pixel en utilisant la fonction update_pixel.

Si la ligne est verticale (les coordonnées x sont égales), elle parcourt les coordonnées y entre le premier et le second point et met également à jour la couleur de chaque pixel en utilisant la fonction update_pixel.

Si la ligne n'est ni horizontale ni verticale, la fonction renvoie un message d'erreur indiquant que seules les lignes horizontales et verticales sont prises en charge.