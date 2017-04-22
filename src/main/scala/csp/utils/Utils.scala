// GENERATED

package csp.utils

import java.util.concurrent.Callable
import scala.language.reflectiveCalls
import scala.util.Random


object Utils {

  def time [X] (e : =>X) : (Long, X) = {
    val before : Long = System.currentTimeMillis
    val result : X = e
    val after : Long = System.currentTimeMillis
    ((after - before) / 1000, result)
  }


  def timePrint [X] (e : =>X, p : Long => String) : X = {
    val (tm, result) = time (e)
    println (p (tm))
    result
  }


  def tryfinally [X <: { def close () : Unit }, Y] (res:X) (f : X => Y) : Y = {
    try {
      f (res)
    } finally {
      if (res != null) {
        res.close
      }
    }
  }


  def trytrace [X] (comp : => X) : X = {
    try {
      comp
    } catch {
      case (e:Exception) => 
        e.printStackTrace
        throw new RuntimeException (e)
    }
  }


  def trytraceIgnore [X] (default : X) (comp : => X) : X = {
    try {
      comp
    } catch {
      case (e:Exception) =>
        e.printStackTrace
        default
    }
  }


  def tryIgnore [X] (default : X) (comp : => X) : X = {
    try {
      comp
    } catch {
      case (e:Exception) =>
        default
    }
  }


  def whileFirst (cond : => Boolean) (body : => Unit) (actionFirst : => Unit) (actionNext : => Unit) = {
    if (cond) {
      actionFirst
      body
      while (cond) {
        actionNext
        body
      }
    }
  }


  def chooseServerPort (start : Int, length : Int) : Int = {
    val serverPort : Int = start + ((Random.nextInt % length).abs)
    print ("serverPort = %d\n".format (serverPort))
    serverPort
  }


  def printThread (message : String) = println ("[thread id = %d] %s".format (Thread.currentThread.getId, message))

  def printProperty (key : String) = println ("%s = %s".format (key, System.getProperty (key)))


  // From http://qerub.se/scala-runnable-and-callable
  import scala.language.implicitConversions
  implicit def runnable (f: () => Unit) : Runnable = new Runnable () { def run () = f () }
  implicit def callable [T] (f : () => T) : Callable[T] = new Callable[T] () { def call () = f () }
}
