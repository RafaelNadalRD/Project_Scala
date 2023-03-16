import scala.collection.immutable.ArraySeq
import scala.io.Source

/**
 * Main app containg program loop
 */
object Main extends App {

  println("Starting application")

  val status = run()

  println("\nExiting application")
  println(s"Final status: ${status.message}")

  /**
   * Read action from Stdin and execute it
   * Exit if action is 'exit' or if an error occured (status > 0)
   * DO NOT MODIFY THIS FUNCTION
   */
  def run(canvas: Canvas = Canvas()): Status = {
    println("\n======\nCanvas:")
    canvas.display
    
    print("\n'help' for more info")
  
    print("\nAction: ")

    

    val action = scala.io.StdIn.readLine()

    //Effacer les anciens textes
    // print("\u001b[H\u001b[2J")
    print("\u001b[2J\u001b[;H")


    

    val (newCanvas, status) = execute(ArraySeq.unsafeWrapArray(action.split(' ')), canvas)

    if (status.error) {
      println(s"ERROR: ${status.message}")
    }

    if (status.exit) {
      status 
    } else {
      run(newCanvas)  
    }
  }

  /**
   * Execute various actions depending on an action command and optionnaly a Canvas
   */
  def execute(action: Seq[String], canvas: Canvas): (Canvas, Status) = {
    val execution: (Seq[String], Canvas) => (Canvas, Status) = action.head match {
      case "exit" => Canvas.exit
      case "dummy" => Canvas.dummy
      case "dummy2" => Canvas.dummy2
      // TODO: Add command here
      case "help" => help
      

      case "update_pixel" => canvas.update_pixel
      case "load_image" => canvas.load_image
      case "new_canvas" => canvas.new_canvas
      case "draw_line" => canvas.draw_line
      case "draw_rectangle" => canvas.draw_rectangle
      case "fill" => canvas.fill
      case "draw_line2" => canvas.draw_line2
      case "draw_line3" => canvas.draw_line3
      case "draw_triangle" => canvas.drawTriangle
      case "draw_polygon" => canvas.drawPolygon
      case _ => Canvas.default
    }

    execution(action.tail, canvas)
  }

    /**
   * List all the available commands
   */
    def help(args: Seq[String], canvas: Canvas): (Canvas, Status) = {
    val commands = Seq(
      "exit" -> "Exit the application",
      "dummy" -> "Dummy command for testing purposes",
      "dummy2" -> "Dummy command for testing purposes",
      "update_pixel" -> "Updates the color of a pixel based on his coordinates\nSyntaxe: 'update_pixel x y newColor' ",
      "load_image" -> "Load an image from a file into the canvas\nSyntaxe: 'load_image imageName'\nHere, are the availables images:\ntriforce\nscala",
      "new_canvas" -> "Create a new canvas\nSyntaxe: 'new_canvas width height character'",
      "draw_line" -> "Draw a vertical or horizontal line between two pixels\nSyntaxe: 'draw_line x1,y1 x2,y2 color'",
      "draw_line2" -> "Draw a line decreasing to the right between two pixels\nSyntaxe: 'draw_line2 x1,y1 x2,y2 color'",
      "draw_line3" -> "Draw a line between two pixels\nSyntaxe: 'draw_line3 x1,y1 x2,y2 color'",
      "draw_triangle" -> "Draw a triangle between three pixels\nSyntaxe: 'draw_triangle x1,y1 x2,y2 x3,y3 color'",
      "draw_polygon" -> "Draw a polygon between multiple pixels\nSyntaxe: 'draw_polygon x1,y1 x2,y2 x3,y3 ... xn,yn color'"
    )

    if (args.length == 2 && args(1) == "--info") {
      commands.find(_._1 == args(0)) match {
        case Some((command, info)) => println(s"\n${command.capitalize}: $info")
        case None => println(s"\nUnknown command: ${args(0)}")
      }
    } else {
      println("\n======\nHere is the list of all available commands:\n")
      commands.foreach { case (command, _) => println(command) }
      println("\nPlease, if you want more information about a command, write the help commande followed by the name of the command '--info' \nFor example: 'help exit --info'")
    }

    (canvas, Status())
  }


}

/**
 * Define the status of the previous execution
 */
case class Status(
  exit: Boolean = false,
  error: Boolean = false,
  message: String = ""
)

/**
 * A pixel is defined by its coordinates along with its color as a char
 */
case class Pixel(x: Int, y: Int, color: Char = ' ') {
  override def toString(): String = {
    color.toString
  }
}

/**
 * Companion object of Pixel case class
 */
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

/**
 * A Canvas is defined by its width and height, and a matrix of Pixel
 */
case class Canvas(width: Int = 0, height: Int = 0, pixels: Vector[Vector[Pixel]] = Vector()) {

  /**
   * Print the canvas in the console
   */
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

  /**
   * Takes a pixel in argument and put it in the canvas
   * in the right position with its color
   */
  def update(pixel: Pixel): Canvas = {
    
    val newPixels = pixels // TODO - Update pixels

    this.copy(pixels = newPixels)
  }

  /**
   * Return a Canvas containing all modifications
   */
  def updates(pixels: Seq[Pixel]): Canvas = {
    pixels.foldLeft(this)((f, p) => f.update(p))
  }

  // TODO: Add any useful method

  /////OUR CODE////////////////
  
  /**
  * Create a new canvas
  */

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

  /**
   * Load image from file to create a canvas
   */
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


  /**
  * Update a pixel based on his coordinates 
  */

  def update_pixel(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(xStr, yStr, color) => {
        try {
          val x = xStr.toInt
          val y = yStr.toInt
          val newPixels = pixels.updated(y, pixels(y).updated(x, Pixel(x, y, color.head)))
          val newCanvas = this.copy(pixels = newPixels)
          (newCanvas, Status())
        } catch {
          case e: Exception =>
          (canvas, Status(error = true, message = s"Invalid arguments: $e\nDesired syntax is: update_pixel x y newColor"))
        }
      }
      case _ =>
      (canvas, Status(error = true, message = "Invalid number of arguments\nDesired syntax is: update_pixel x y newColor"))
    }
  }

  def draw_rectangle(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
  arguments match {
    case Seq(x1y1Str, x2y2Str, color) =>
      try {
        val x1y1 = x1y1Str.split(",").map(_.toInt)
        val x2y2 = x2y2Str.split(",").map(_.toInt)
        val x1 = x1y1(0).toString()
        val y1 = x1y1(1).toString()
        val x2 = x2y2(0).toString()
        val y2 = x2y2(1).toString()
        val (newCanvas1, status1) = draw_line(Seq(x1.concat(",").concat(y1),x2.concat(",").concat(y1), color), canvas)
        val (newCanvas2, status2) = draw_line(Seq(x2.concat(",").concat(y1),x2.concat(",").concat(y2), color), newCanvas1)
        val (newCanvas3, status3) = draw_line(Seq(x2.concat(",").concat(y2),x1.concat(",").concat(y2), color), newCanvas2)
        val (newCanvas4, status4) = draw_line(Seq(x1.concat(",").concat(y2),x1.concat(",").concat(y1), color), newCanvas3)
        (newCanvas4, Status())
      } catch {
        case e: Exception =>
          (canvas, Status(error = true, message = s"Invalid arguments: $e"))
      }
    case _ =>
      (canvas, Status(error = true, message = "Invalid number of arguments"))
  }
}


  def draw_line(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(p1Str, p2Str, color) => {
        try {
          val p1 = p1Str.split(",")
          val p2 = p2Str.split(",")
          val x1 = p1(0).toInt
          val y1 = p1(1).toInt
          val x2 = p2(0).toInt
          val y2 = p2(1).toInt
          var newCanvas = canvas
          if (x1 == x2) {
            // Vertical line
            val yStart = Math.min(y1, y2)
            val yEnd = Math.max(y1, y2)
            for (y <- yStart to yEnd) {
              val args = Seq(x1.toString, y.toString, color)
              val (updatedCanvas, status) = newCanvas.update_pixel(args, newCanvas)
              if (status.error) {
                return (updatedCanvas, status)
              }
              newCanvas = updatedCanvas
            }
          } else if (y1 == y2) {
            // Horizontal line
            val xStart = Math.min(x1, x2)
            val xEnd = Math.max(x1, x2)
            for (x <- xStart to xEnd) {
              val args = Seq(x.toString, y1.toString, color)
              val (updatedCanvas, status) = newCanvas.update_pixel(args, newCanvas)
              if (status.error) {
                return (updatedCanvas, status)
              }
              newCanvas = updatedCanvas
            }
          } else {
            return (canvas, Status(error = true, message = "Only horizontal and vertical lines are supported."))
          }
          (newCanvas, Status())
        } catch {
          case e: Exception =>
            (canvas, Status(error = true, message = s"Invalid arguments: $e\nDesired syntax is: draw_line x1,y1 x2,y2 color"))
        }
      }
      case _ =>
        (canvas, Status(error = true, message = "Invalid number of arguments\nDesired syntax is: draw_line x1,y1 x2,y2 color"))
    }
  }
  def draw_line2(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(p1Str, p2Str, color) => {
        try {
          val p1 = p1Str.split(",")
          val p2 = p2Str.split(",")
          val x1 = p1(0).toInt
          val y1 = p1(1).toInt
          val x2 = p2(0).toInt
          val y2 = p2(1).toInt

          val dx = x2 - x1
          val dy = y2 - y1
          var D = 2 * dy - dx
          var y = y1

          var newCanvas = canvas

          for (x <- x1 to x2) {
            val args = Seq(x.toString, y.toString, color)
            val (updatedCanvas, status) = newCanvas.update_pixel(args, newCanvas)
            if (status.error) {
              return (updatedCanvas, status)
            } else {
              newCanvas = updatedCanvas
            }

            if (D > 0) {
              y = y + 1
              D = D - 2 * dx
            }
            D = D + 2 * dy
          }
          
          (newCanvas, Status())
        } catch {
          case e: Exception =>
            (canvas, Status(error = true, message = s"Invalid arguments: $e\nDesired syntax is: draw_line2 x1,y1 x2,y2 color"))
        }
      }
      case _ =>
        (canvas, Status(error = true, message = "Invalid number of arguments\nDesired syntax is: draw_line2 x1,y1 x2,y2 color"))
    }
  }
  def draw_line3(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(p1Str, p2Str, color) => {
        try {
          val p1 = p1Str.split(",")
          val p2 = p2Str.split(",")
          var x1 = p1(0).toInt
          var y1 = p1(1).toInt
          var x2 = p2(0).toInt
          var y2 = p2(1).toInt

          val steep = math.abs(y2 - y1) > math.abs(x2 - x1)
          if (steep) {
            var tmp = x1
            x1 = y1
            y1 = tmp
            tmp = x2
            x2 = y2
            y2 = tmp
          }

          if (x1 > x2) {
            var tmp = x1
            x1 = x2
            x2 = tmp
            tmp = y1
            y1 = y2
            y2 = tmp
          }
          val dx = x2 - x1
          val dy = math.abs(y2 - y1)
          var error = dx / 2
          val yStep = if (y1 < y2) 1 else -1
          var y = y1

          var newCanvas = canvas

          for (x <- x1 to x2) {
            val args = if (steep) Seq(y.toString, x.toString, color)
                      else Seq(x.toString, y.toString, color)

            val (updatedCanvas, status) = newCanvas.update_pixel(args, newCanvas)
            if (status.error) {
              return (updatedCanvas, status)
            } else {
              newCanvas = updatedCanvas
            }

            error -= dy
            if (error < 0) {
              y += yStep
              error += dx
            }
          }

          (newCanvas, Status())
        } catch {
          case e: Exception =>
            (canvas, Status(error = true, message = s"Invalid arguments: $e\nDesired syntax is: draw_line3 x1,y1 x2,y2 color"))
        }
      }
      case _ =>
        (canvas, Status(error = true, message = "Invalid number of arguments\nDesired syntax is: draw_line3 x1,y1 x2,y2 color"))
    }
  }
  def drawTriangle(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(p1Str, p2Str, p3Str, color) => {
        try {
          val p1 = p1Str.split(",")
          val p2 = p2Str.split(",")
          val p3 = p3Str.split(",")

          val (canvas1, status1) = draw_line3(Seq(p1(0), p1(1), p2(0), p2(1), color), canvas)
          if (status1.error) {
            return (canvas1, status1)
          }

          val (canvas2, status2) = draw_line3(Seq(p2(0), p2(1), p3(0), p3(1), color), canvas1)
          if (status2.error) {
            return (canvas2, status2)
          }

          val (canvas3, status3) = draw_line3(Seq(p3(0), p3(1), p1(0), p1(1), color), canvas2)
          if (status3.error) {
            return (canvas3, status3)
          }

          (canvas3, Status())
        } catch {
          case e: Exception =>
            (canvas, Status(error = true, message = s"Invalid arguments: $e\nDesired syntax is: draw_triangle x1,y1 x2,y2 x3,y3 color"))
        }
      }
      case _ =>
        (canvas, Status(error = true, message = "Invalid number of arguments\nDesired syntax is: draw_triangle x1,y1 x2,y2 x3,y3 color"))
    }
  }
  def drawPolygon(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    val color = arguments.last
    val points = arguments.dropRight(1)
    
    if (points.length < 3) {
      return (canvas, Status(error = true, message = "Invalid number of arguments, must have at least 3 points\nDesired syntax is: draw_polygon x1,y1 x2,y2 x3,y3 ... xn,yn color"))
    }
    
    try {
      def drawEdges(p1Str: String, p2Str: String, canvas: Canvas): (Canvas, Status) = {
        val p1 = p1Str.split(",")
        val p2 = p2Str.split(",")

        draw_line3(Seq(p1(0), p1(1), p2(0), p2(1), color), canvas)
      }

      val initialCanvasStatus: (Canvas, Status) = (canvas, Status())
      val (finalCanvas, finalStatus) = points.zip(points.tail :+ points.head).foldLeft(initialCanvasStatus) {
        case ((currentCanvas, currentStatus), (p1, p2)) =>
          if (currentStatus.error) (currentCanvas, currentStatus)
          else drawEdges(p1, p2, currentCanvas)
      }

      (finalCanvas, finalStatus)
    } catch {
      case e: Exception =>
        (canvas, Status(error = true, message = s"Invalid arguments: $e\nDesired syntax is: draw_polygon x1,y1 x2,y2 x3,y3 ... xn,yn color"))
    }
  }



}

/**
 * Companion object for Canvas case class
 */
object Canvas {
  /**
   * Exit execution
   */
  def exit(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    (canvas, Status(exit = true, message="Received exit signal"))

  /**
   * Default execution for unknown action
   */
  def default(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    (canvas, Status(error = true, message = s"Unknown command"))

  /**
   * Create a static Canvas
   */
  def dummy(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    if (arguments.size > 0) 
      (canvas, Status(error = true, message = "dummy action does not excpect arguments"))
    else  {
      val dummyCanvas = Canvas(
        width = 3,
        height = 4,
        pixels = Vector(
          Vector(Pixel(0, 0, '#'), Pixel(1, 0, '.'), Pixel(2, 0, '#')),
          Vector(Pixel(0, 1, '#'), Pixel(1, 1, '.'), Pixel(2, 1, '#')),
          Vector(Pixel(0, 2, '#'), Pixel(1, 2, '.'), Pixel(2, 2, '#')),
          Vector(Pixel(0, 3, '#'), Pixel(1, 3, '.'), Pixel(2, 3, '#'))
        )
      )
      
      (dummyCanvas, Status())
    }

  /**
   * Create a static canvas using the Pixel companion object
   */
  def dummy2(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    if (arguments.size > 0) 
      (canvas, Status(error = true, message = "dummy action does not excpect arguments"))
    else  {
      val dummyCanvas = Canvas(
        width = 3,
        height = 1,
        pixels = Vector(
          Vector(Pixel("0,0", '#'), Pixel("1,0",'.'), Pixel("2,0", '#')),
        )
      )
      
      (dummyCanvas, Status())
    }

  // TODO: Add any useful method

  /////OUR CODE//////
  
}




