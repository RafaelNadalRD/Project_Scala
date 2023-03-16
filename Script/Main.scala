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
      

      case "update_pixel" => canvas.update_pixel
      case "load_image" => Canvas.load_image
      case "new_canvas" => canvas.new_canvas
      case "draw_line" => canvas.draw_line
      case "draw_line2" => canvas.draw_line2
      case "draw_line3" => canvas.draw_line3
      case "draw_triangle" => canvas.drawTriangle
      case "draw_polygon" => canvas.drawPolygon
      case _ => Canvas.default
    }

    execution(action.tail, canvas)
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
          (canvas, Status(error = true, message = s"Invalid arguments: $e"))
        }
      }
      case _ =>
      (canvas, Status(error = true, message = "Invalid number of arguments"))
    }
  }
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
  def draw_line2(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(p1Str, p2Str, color) =>
        try {
          val p1 = p1Str.split(",")
          val p2 = p2Str.split(",")
          val x1 = p1(0).toInt
          val y1 = p1(1).toInt
          val x2 = p2(0).toInt
          val y2 = p2(1).toInt

          val dx = x2 - x1
          val dy = y2 - y1
          val initialD = 2 * dy - dx

          val (_, newCanvas, _) = (x1 to x2).foldLeft((y1, canvas, initialD)) {
            case ((y, currentCanvas, d), x) =>
              val args = Seq(x.toString, y.toString, color)
              val (updatedCanvas, status) = currentCanvas.update_pixel(args, currentCanvas)
              if (status.error) return (updatedCanvas, status)

              val newY = if (d > 0) y + 1 else y
              val newD = if (d > 0) d - 2 * dx else d

              (newY, updatedCanvas, newD + 2 * dy)
          }
          (newCanvas, Status())
        } catch {
          case e: Exception =>
            (canvas, Status(error = true, message = s"Invalid arguments: $e"))
        }
      case _ =>
        (canvas, Status(error = true, message = "Invalid number of arguments"))
    }
  }
  def draw_line3(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    arguments match {
      case Seq(p1Str, p2Str, color) =>
        try {
          val p1 = p1Str.split(",")
          val p2 = p2Str.split(",")
          val x1 = p1(0).toInt
          val y1 = p1(1).toInt
          val x2 = p2(0).toInt
          val y2 = p2(1).toInt

          val steep = math.abs(y2 - y1) > math.abs(x2 - x1)
          val (startX, startY) = if (steep) (y1, x1) else (x1, y1)
          val (endX, endY) = if (steep) (y2, x2) else (x2, y2)

          val (minX, minY, maxX, maxY) = if (startX > endX) (endX, endY, startX, startY) else (startX, startY, endX, endY)

          val dx = maxX - minX
          val dy = math.abs(maxY - minY)
          val yStep = if (minY < maxY) 1 else -1

          val initialError = dx / 2

          val (_, newCanvas, _) = (minX to maxX).foldLeft((minY, canvas, initialError)) {
            case ((y, currentCanvas, error), x) =>
              val args = if (steep) Seq(y.toString, x.toString, color)
              else Seq(x.toString, y.toString, color)

              val (updatedCanvas, status) = currentCanvas.update_pixel(args, currentCanvas)
              if (status.error) return (updatedCanvas, status)

              val newError = error - dy
              val newY = if (newError < 0) y + yStep else y
              val updatedError = if (newError < 0) newError + dx else newError

              (newY, updatedCanvas, updatedError)
          }

          (newCanvas, Status())
        } catch {
          case e: Exception =>
            (canvas, Status(error = true, message = s"Invalid arguments: $e"))
        }
      case _ =>
        (canvas, Status(error = true, message = "Invalid number of arguments"))
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
            (canvas, Status(error = true, message = s"Invalid arguments: $e"))
        }
      }
      case _ =>
        (canvas, Status(error = true, message = "Invalid number of arguments"))
    }
  }
  def drawPolygon(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = {
    val color = arguments.last
    val points = arguments.dropRight(1)
    
    if (points.length < 3) {
      return (canvas, Status(error = true, message = "Invalid number of arguments, must have at least 3 points"))
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
        (canvas, Status(error = true, message = s"Invalid arguments: $e"))
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

  
}




