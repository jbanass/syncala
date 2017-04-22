#!/bin/sh
exec scala -deprecation "$0" "$@"
!#

import java.io.{InputStream,OutputStream, File, FileInputStream}
import java.net.{Socket}

var line : String = args(2)
var file : File = new File(line)

if (!file.exists)
{
    println("Sorry, file does not exist\n")
    System.exit(-1)
}

try {
    val hostname : String = args (0)
    val serverPort : Int = args (1).toInt
    val s : Socket = new Socket (hostname, serverPort)

    line = ""

    var fs : FileInputStream = new FileInputStream(file)

    while (fs.available() > 0) {
	line += fs.read().asInstanceOf[Char]
    }

    val os : OutputStream = s.getOutputStream

    os.write(line.getBytes("us-ascii"))

    os.flush
    System.out.flush
    (new Thread (new Runnable {
		override def run () {
			try{
			    val is : InputStream = s.getInputStream
			    val buffer : Array[Byte] = new Array[Byte] (1024)
			    var numRead = -1
			    while ( { numRead = is.read (buffer); numRead != -1 } ) {
				System.out.write (buffer, 0, numRead)
				System.out.flush
			    }
			    System.out.flush
			} catch {
			    case (e: Exception) =>
	   
			}
		    }
		    })).start
} catch {
    case (e: Exception) =>
	println("Sorry, something went wrong. Please try again! Perhaps the server isn't up?")
	System.exit(-1)
       }
