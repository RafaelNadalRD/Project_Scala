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

    print("\nAction: ")

    val action = scala.io.StdIn.readLine()

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
      
      case "load_image" => load_image
      case "new_canvas" => canvas.new_canvas
      case _ => Canvas.default
    }

    execution(action.tail, canvas)
  }

  /**
   * Load image from file to create a canvas
   */
  def load_image(action: Seq[String], canvas: Canvas): (Canvas, Status) = {
    val fileName = action.head
    val path = s"D:/Cours/ESGI/Scala/Projet scala github/Project_Scala/Utilities/$fileName"
    
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
        val pixels = Vector.fill(height, width)(Pixel(0, 0, char.head))
        val newCanvas = Canvas(width, height, pixels)
        (newCanvas, Status())
      } catch {
        case e: Exception =>
          (canvas, Status(error = true, message = s"Invalid arguments: $e"))
      }
    }
    case _ =>
      (canvas, Status(error = true, message = "Invalid number of arguments"))
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

}




