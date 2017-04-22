package csp.net

import akka.actor.{Actor,ActorContext,ActorLogging,ActorRef,ActorSystem,Inbox,Props,Terminated}
import akka.io.{IO,Tcp}
import akka.io.Tcp.{Bind,Bound,Close,Closed,CommandFailed,Connected,PeerClosed,Received,Register,Write}
import akka.util.{ByteString}
import java.net.{InetSocketAddress, Socket}
import java.util.{HashMap, ArrayList}
import java.util.regex.{Matcher,Pattern}
import csp.utils.Utils.{runnable}
import scala.collection.JavaConversions._

object Syncala {
       case class ReportToClient(data : ByteString)
       case class Results(data : ByteString)
       case class SubmitDirectory(data : ByteString, out : ActorRef)

       var clientCount : Int = 0
       var processor : ActorRef = null


       def mkHandlerName (addr : InetSocketAddress) : String =
       	   "h-" + addr.getAddress.getHostAddress.replaceAll(":", "-") + "-" + addr.getPort

       class Server extends Actor
       {
	   val manager : ActorRef = IO (Tcp) (context.system)
	   processor = context.actorOf(Props(classOf[Processor]))
	   manager ! Bind (self, new InetSocketAddress(7000))
	   def receive = {
	       case m @ Connected(remote, local) =>
	       	    if (clientCount < 2)
		    {
			clientCount += 1
			var client : ActorRef = context.actorOf(Props(classOf[Client], sender), mkHandlerName(remote))
			println("Client connected from %s\n".format(remote))
			sender ! Register(client)
			client ! ReportToClient(ByteString("You are client " + mkHandlerName(remote) + "\n"))
		    }
		   
               }
       }

        class Client(out : ActorRef) extends Actor
       {
		def receive = {
       	   	    case m @ Received(data) =>
	   	    	 processor ! SubmitDirectory(data, self)

		    case m @ ReportToClient(data) =>
		    	 out ! Write(data)

	            case m @ Results(data) =>
		    	 out ! Write(data)
		}
		    
       }
       
       class Processor extends Actor
       {
		var actorRefList : ArrayList[ActorRef] = new ArrayList[ActorRef]()
		var sourceFileList : List[String] = List[String]()
		var targetFileList : List[String] = List[String]()
		var sourceUnsuccessfulFile : String = ""
		var targetUnsuccessfulFile : String = ""
		var actorDirMap : HashMap[ActorRef, HashMap[String, String]] = new HashMap[ActorRef, HashMap[String,String]]
		def receive = {
		    case m @ SubmitDirectory(data : ByteString, out : ActorRef) =>
			if (actorRefList.size < 2)
			{
				var initString : Array[String] = data.decodeString("utf-8").split("\n")
				var currentFile : String = ""
				var fileHashMap : HashMap[String, String] = new HashMap[String, String]
				//each row is 10 members long
				var ctr : Int = 0
				var curString : String = ""

				var pLine = Pattern.compile("^[-|d|w|r|x]* [0-9]* .* .*[ *]([0-9]*) ([A-Za-z]*) ([0-9]*) ([0-9]+:[0-9]+) (.*)$")
				
				

				while (ctr < initString.size)
				{
					var tempArr : String = initString(ctr)
					
					val mLine : Matcher = pLine.matcher(tempArr)

					if (mLine.matches)
					{

						curString += mLine.group(5) + " " + mLine.group(1) + " " + mLine.group(2) + " " + mLine.group(3) + " " + mLine.group(4)
							  //reminder : 5 is file, 1 is size, 2 is month, 3 is day, 4 is time
						currentFile = mLine.group(5)
						
						if (actorRefList.size == 0)
						{
							sourceFileList = sourceFileList.::(currentFile)
						}
						else if (actorRefList.size == 1)
						{
							targetFileList = targetFileList.::(currentFile)
						}

						fileHashMap.put(currentFile, curString)
						
					}
					else
					{
						println("DID NOT MATCH REGEX: " + tempArr)
						if (actorRefList.size == 0)
						{
							sourceUnsuccessfulFile += tempArr + "\n"
						}
						else if (actorRefList.size == 1)
						{   
							targetUnsuccessfulFile += tempArr + "\n"
						}
					}
					
					curString = ""
					currentFile = ""
					ctr += 1
				}

				actorDirMap.put(out, fileHashMap)
				actorRefList.add(out)

				if (actorRefList.size == 2)
				{
					var resultsString : String = ""
					var sourceSizeString : String = ""
					var targetSizeString : String = ""
					var sourceDateString : String = ""
					var targetDateString : String = ""
					var actorSourceHashMap : HashMap[String, String] = actorDirMap.get(actorRefList(0))
					var actorTargetHashMap : HashMap[String, String] = actorDirMap.get(actorRefList(1))
					
					
					//let's see which files exist first
					resultsString += "The following files do not exist in the target\n\n"
					for (file <- sourceFileList)
					{
						if (actorTargetHashMap.get(file) == null)
						{
							resultsString += file + "\n"
						}
						else
						{
							var targetFile : Array[String] = actorTargetHashMap.get(file).split(" ")
							var sourceFile : Array[String] = actorSourceHashMap.get(file).split(" ")
							
							if (Integer.parseInt(sourceFile(1)) > Integer.parseInt(targetFile(1)))
							{
								sourceSizeString += file + ": " + sourceFile(1) + " > " + targetFile(1) + "[Date Modified of Source: " + sourceFile(2) + " " + sourceFile(3) + " " + sourceFile(4) + "]\n"
							}
							
							
							if (Integer.parseInt(sourceFile(1)) < Integer.parseInt(targetFile(1)))
							{
								targetSizeString += file + ": " + sourceFile(1) + " < " + targetFile(1) + "[Date Modified of Target: " + targetFile(2) + " " + targetFile(3) + " " + targetFile(4) + "]\n"
							}
						}
					}
					
					resultsString += "\n\nThe following files do not exist in the source\n\n"
					
					for (file <- targetFileList)
					{
						if (actorSourceHashMap.get(file) == null)
						{
							resultsString += file + "\n"
						}
					}

					resultsString += "\n\nThe following files exist, but have been altered in some way via size\n\n"

					resultsString += "FILES SMALLER THAN SOURCE\n\n"

					resultsString += sourceSizeString + "\n\n"

					resultsString += "FILES LARGER THAN SOURCE \n\n"
					
					resultsString += targetSizeString

					resultsString += "FILES NOT COMPARED : SOURCE : \n\n"
					
					resultsString += sourceUnsuccessfulFile

					resultsString += "FILES NOT COMPARED : TARGET : \n\n"
				
					resultsString += targetUnsuccessfulFile

					println("Printing results now...\n")
					var actorSource : ActorRef = actorRefList(0)
					var actorTarget : ActorRef = actorRefList(1)

					actorSource ! Results(ByteString("ATTENTION SOURCE\n---------------------------------------------------------\n\n" + resultsString))
					actorTarget ! Results(ByteString("ATTENTION TARGET\n---------------------------------------------------------\n\n" + resultsString))
					
					context.stop(actorSource)
					context.stop(actorTarget)
					resultsString = ""
					sourceSizeString = ""
					targetSizeString = ""
					sourceUnsuccessfulFile = ""
					targetUnsuccessfulFile = ""
					actorRefList.clear()
					sourceFileList = List[String]()
					targetFileList = List[String]()
					actorTargetHashMap.clear()
					actorSourceHashMap.clear()
					actorDirMap.clear()
					clientCount = 0
					println("Processing finished cleaning up...\n")
					println("Ready for new clients...\n")
				}
			}
		}


       }

	def main (args : Array[String]) : Unit = {
	    val system : ActorSystem = ActorSystem("Syncala")
	    system.actorOf(Props[Server], "server")
	}
       
}