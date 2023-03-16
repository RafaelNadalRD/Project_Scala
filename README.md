# Project_Scala
Le projet du S1


## Compilation du projet:
- Déplacez vous dans le répertoire Script à l'aide de la commande "cd"
- Compilez le fichier Main.scala à l'aide de la commande "scalac Main.scala"
- Executez le script à l'aide de la commande "scala Main"


## Explication du code:
Dans la partie suivante nous allons expliquer les différentes briques de codes qui ont été ajoutés.


### Exercice 1-B

```scala
def display: Unit = {
    if (pixels.size == 0) {
      println("Empty Canvas")
    } else {
      println(s"Size: $width x $height")
      
      // OUR CODE//////////////////////
      for (row <- pixels) {
        for (pixel <- row) {
          print(pixel)
        }
        println()
      }
      //////////////////////////////////
    }
  }
```
Pour réaliser cette exercice, nous avons ajouté un code qui parcourt les éléments du tableau en utilisant une boucle "for" imbriquée. Le premier "for" parcourt chaque ligne "row" du tableau "pixels" et le deuxième "for" parcourt chaque élément "pixel" de chaque ligne "row". Pour chaque élément "pixel", la méthode "print" est utilisée pour l'imprimer sur la même ligne sans saut de ligne. Après avoir parcouru tous les éléments d'une ligne, la méthode "println" est utilisée pour effectuer un saut de ligne et passer à la ligne suivante. Ce processus se répète pour chaque ligne du tableau, produisant ainsi une représentation visuelle de la matrice de pixels.

### Exercice 1-C

```scala
object Pixel {
  /**
   * Create a Pixel from a string "x,y"
   */
  def apply(s: String): Pixel = {
    // OUR CODE /////////////////////
    val Array(x, y) = s.split(",")
    Pixel(x.toInt, y.toInt)
    ////////////////////////////////
  }

  /**
   * Create a Pixel from a string "x,y" and a color 
   */
  def apply(s: String, color: Char): Pixel = {
    // OUR CODE /////////////////////
    val Array(x, y) = s.split(",")
    Pixel(x.toInt, y.toInt, color)
  }
}

```

Pour cet exercice nous avons amélioré la méthode "apply" quipeux maitenant extraient les valeurs de coordonnées x et y de la chaîne de caractères en utilisant la méthode "split" de Scala pour diviser la chaîne en deux parties. Puis convertit les chaînes de caractères résultantes en entiers en utilisant la méthode "toInt". Les valeurs de coordonnées x et y ainsi obtenues sont ensuite utilisées pour créer un objet "Pixel" valide.


### Exercice 1-D

A l'occasion de cet exercice nous avons implémenté la première action qui vise à créer une canvas à l'aide de sa largeur, longueur et de son caractère par défault.

```scala
def new_canvas(arguments: Seq[String], canvas: Canvas): (Canvas, Status) =
  arguments match {
    case Seq(widthStr, heightStr, char) => {
      try {
        val width = widthStr.toInt
        val height = heightStr.toInt
        if (width < 0 || height < 0) {
          throw new IllegalArgumentException("Width and height must be positive")
        }
        val pixels = Vector.fill(height, width)(Pixel(0, 0, char.head))
        val newCanvas = Canvas(width, height, pixels)
        (newCanvas, Status())
      } catch {
        case e: Exception =>
          (canvas, Status(error = true, message = s"Invalid arguments: $e\nDesired syntax is: new_canvas width height character"))
      }
    }
    case _ =>
      (canvas, Status(error = true, message = "Invalid number of arguments\nDesired syntax is: new_canvas width height character"))
  }
```
La méthode Scala "new_canvas" suivante prend en entrée deux arguments qui sont: une séquence de chaînes de caractères contenant les arguments nécessaires pour créer un nouveau canvas, et "canvas" qui est l'objet "Canvas" existant. La méthode retourne un tuple contenant deux valeurs: le nouveau canvas créé et un objet "Status".

La méthode présente utilise une expression "match" pour déterminer la façon de créer le nouveau canvas en fonction des arguments passés. Si les arguments sont une séquence de trois chaînes de caractères, la méthode tente de convertir les deux premières chaînes de caractères en entiers pour obtenir la largeur et la hauteur du nouveau canvas. Si ces valeurs ne sont pas positives, la méthode lance une exception. Ensuite, la méthode crée un vecteur de pixels en utilisant la troisième chaîne de caractères comme couleur de tous les pixels, puis crée un nouveau canvas avec les dimensions et le vecteur de pixels créés. Enfin, la méthode retourne le nouveau canvas et un objet "Status" sans erreur.

De plus, si la séquence "arguments" ne contient pas trois chaînes de caractères, la méthode retourne l'objet "Canvas" existant et un objet "Status" avec une erreur et un message indiquant que le nombre d'arguments est incorrect.


### Exercice 2-A


```scala
  def load_image(action: Seq[String], canvas: Canvas): (Canvas, Status) = {
    val fileName = action.head
    
    val path = s"../Utilities/$fileName"
    
    val status = Status()
    val fileContent = try {
      Source.fromFile(path).getLines().toVector
    } catch {
      case e: Exception =>
        return (canvas, status.copy(error = true, message = s"Failed to read file '$fileName'"))
    }
    
    if (fileContent.isEmpty) {
      return (canvas, status.copy(error = true, message = s"File '$fileName' is empty"))
    }

    // Create a new canvas from the file content
    val pixels = fileContent.map(_.toCharArray).map(_.toVector).map(row => row.map(c => Pixel(0, 0, c)))
    val newCanvas = canvas.copy(width = pixels(0).length, height = pixels.length, pixels = pixels)
    println(s"Loaded image from file '$fileName'")
    (newCanvas, status)
  }
```
La méthode scala "load_image" prend en entrée deux arguments: une séquence de chaînes de caractères et une instance de la classe "Canvas". Cette méthode retourne un tuple contenant l'instance de la classe "Canvas" et une instance de la classe "Status".

La méthode commence par extraire le nom de fichier à partir de la séquence d'actions "action.head". Ensuite, le chemin d'accès complet du fichier est construit en utilisant le nom de fichier.

Pour préciser, nous avons pris la liberté d'ajouter une autre image que la triforce intiale de manière à vérifier que la méthode pouvait fonctionner avec d'autres fichiers d'images.

Ensuite, la méthode crée une instance de la classe "Status" pour enregistrer les informations sur le chargement du fichier.

La méthode essaie par la suite d'ouvrir le fichier à l'aide de la méthode "Source.fromFile(path).getLines().toVector". Si l'ouverture du fichier échoue, la méthode retourne une paire contenant l'instance de la classe "Canvas" passée en entrée et une instance de la classe "Status" modifiée pour signaler une erreur de lecture de fichier.

Si le contenu du fichier est vide, la méthode retourne une paire contenant l'instance de la classe "Canvas" passée en entrée et une instance de la classe "Status" modifiée pour signaler que le fichier est vide.

Enfin, si le contenu du fichier n'est pas vide, la méthode crée une nouvelle instance de la classe "Canvas" à partir du contenu du fichier. Elle construit ensuite les pixels de la nouvelle instance de "Canvas" à partir des caractères de chaque ligne du fichier. Chaque caractère est transformé en un objet "Pixel" ayant une valeur d'opacité de 0 et une valeur de couleur extraite du caractère.

Pour finir, la méthode retourne une paire contenant la nouvelle instance de "Canvas" et une instance de la classe "Status" qui indique que l'image a été chargée avec succès.


### Exercice 2-B

```scala
  def update_pixel(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(coords, color) => {
        try {
          val coordsArray = coords.split(",").map(_.toInt)
          val x = coordsArray(0)
          val y = coordsArray(1)
          val newPixels = pixels.updated(y, pixels(y).updated(x, Pixel(x, y, color.head)))
          val newCanvas = this.copy(pixels = newPixels)
          (newCanvas, Status())
        } catch {
          case e: Exception =>
          (canvas, Status(error = true, message = s"Invalid arguments: $e\nDesired syntax is: update_pixel x,y newColor"))
        }
      }
      case _ =>
      (canvas, Status(error = true, message = "Invalid number of arguments\nDesired syntax is: update_pixel x,y newColor"))
    }
  }
```
La méthode "update_pixel" ci-dessus prend en entrée deux arguments. Un de type Seq[String] et "canvas" de type Canvas. La méthode retourne ensuite un tuple contenant une nouvelle instance de Canvas et une instance de Status.

La méthode commence par vérifier si les arguments ont la forme attendue en utilisant une expression "match". Si les arguments sont valides, la méthode extrait les coordonnées "x" et "y" à partir de la chaîne de caractères "coords" en utilisant la méthode "split" pour diviser la chaîne en deux sous-chaînes, en utilisant la virgule comme délimiteur. Les sous-chaînes sont ensuite converties en entiers en utilisant la méthode "toInt". La méthode utilise ensuite ces coordonnées pour mettre à jour la couleur du pixel à la position correspondante dans la matrice "pixels" du canvas. La méthode retourne ensuite une nouvelle instance de Canvas avec la matrice de pixels mise à jour et une instance de Status vide.

Si les arguments ne sont pas valides, la méthode retourne une instance de Status avec un message d'erreur approprié.

En résumé, cette méthode met à jour la couleur d'un pixel dans un canvas en utilisant des coordonnées de la forme "x,y" et retourne une nouvelle instance de Canvas avec la matrice de pixels mise à jour et une instance de Status pour signaler des erreurs éventuelles.

Il est cependant important de noter que le système de coordonnées commence à compter à partir de 0. Ainsi, il ne sera pas possible de modifier un pixel de coordonnées 10,10 pour une canvas de largeur et hauteur de 10 car l'abscisse et l'ordonnée du canvas iront de 0 à 9.
Nous aurions pu faire le choix d'incrémenter de 1 les coordonnées pour se rapprocher d'un résonnement plus simple pour l'utilisateur, à savoir "premier pixel de coordonné 1,1". Pour autant cette approche reste moins "mathématique" et ne semblait pas être celle que vous avez adopté dans la photo d'exmple de l'énnoncé.

### Exercice 2-C

```scala
def draw_line(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(p1Str, p2Str, color) =>
        try {
          val p1 = p1Str.split(",")
          val p2 = p2Str.split(",")
          val x1 = p1(0).toInt
          val y1 = p1(1).toInt
          val x2 = p2(0).toInt
          val y2 = p2(1).toInt

          if (x1 == x2) {
            // Vertical line
            val yStart = Math.min(y1, y2)
            val yEnd = Math.max(y1, y2)
            val newCanvas = (yStart to yEnd).foldLeft(canvas) { (currentCanvas, y) =>
              val args = Seq(x1.toString, y.toString, color)
              val (updatedCanvas, status) = currentCanvas.update_pixel(args, currentCanvas)
              if (status.error) return (updatedCanvas, status)
              updatedCanvas
            }
            (newCanvas, Status())
          } else if (y1 == y2) {
            // Horizontal line
            val xStart = Math.min(x1, x2)
            val xEnd = Math.max(x1, x2)
            val newCanvas = (xStart to xEnd).foldLeft(canvas) { (currentCanvas, x) =>
              val args = Seq(x.toString, y1.toString, color)
              val (updatedCanvas, status) = currentCanvas.update_pixel(args, currentCanvas)
              if (status.error) return (updatedCanvas, status)
              updatedCanvas
            }
            (newCanvas, Status())
          } else {
            (canvas, Status(error = true, message = "Only horizontal and vertical lines are supported."))
          }
        } catch {
          case e: Exception =>
            (canvas, Status(error = true, message = s"Invalid arguments: $e"))
        }
      case _ =>
        (canvas, Status(error = true, message = "Invalid number of arguments"))
    }
  }
```
La méthode draw_line permet de dessiner une ligne sur la Canvas en utilisant la méthode update_pixel. Les arguments passés à la méthode sont une séquence de trois chaînes de caractères : les coordonnées de deux points (p1 et p2) et la couleur de la ligne. La méthode commence par extraire les coordonnées des deux points et les convertir en entiers.

Ensuite, elle vérifie si la ligne est verticale ou horizontale en comparant les valeurs x et y des deux points. Si la ligne est verticale, elle parcourt les pixels un par un dans la colonne x et entre les deux valeurs y de p1 et p2 en appelant la méthode update_pixel pour mettre à jour la couleur de chaque pixel. Si la ligne est horizontale, elle parcourt les pixels un par un dans la ligne y et entre les deux valeurs x de p1 et p2. Dans les deux cas, si une erreur se produit lors de l'appel de update_pixel, la méthode retourne une erreur.

Si la ligne n'est ni horizontale ni verticale, la méthode retourne une erreur. Enfin, si le nombre d'arguments est incorrect, elle retourne également une erreur. Enfin, la méthode renvoie le nouveau Canvas et l'état (Status) de l'opération.

De manière générale, cette méthode est la première version du dessin de ligne et n'est volontairement pas capable de faire plus que dessiner des lignes verticales et horizontales


### Exercice 2-D

```scala

```

### Exercice 2-E

```scala

```

### Exercice 2-F

```scala

```

### Exercice 2-G

```scala

```

### Exercice 2-H

```scala

```














TO DO LISTE:
- Expliquer dans le readme.md que la fonction update_pixel modifie le pixel avec des coordonnées commençant par 0 (donc  	modifie le pixel 2.2)
-ajouter une methode "Help" qui explique toutes les commandes possibles
- Au lieu de mettre "invalide argument" plutot ecrire explicitement la syntaxe attendu par l'utilisateur 

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