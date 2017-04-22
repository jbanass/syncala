# syncala
Syncala is a project file synchronization program written in 2 weeks with Scala.

# Overview

Syncala (Synchronize with Scala) is an Akka-based Scala program in which
an instance acts as a "Server" and "Processor" to accept incoming "Client"
connections via a special Send scala script to receive directory information,
so that the two "Clients" may compare directories to see which files need
to be synced between each other. The "Server" then reports back to the "Clients"
what files need to be synced in relation to size and date modified, then ends
their connection for another pair of clients to begin a session.

# Requirements
## Server Requirements
Syncala requires a system that hosts the "Server" and "Processor" actors to
have Java, Scala, and Internet capabilities.

## Client Requirements

Syncala requires a system that will act as the "Client" to have directory
information obtained by the following command :

`[ ls -a -l > *filename* ]`

where *filename* is a name of your choosing, as well as a special script
to surpass the abilities of netcat called Send, also programmed in Scala,
that is callable via terminal :

`[ ./send.scala *machinename* *port* *filename* ]`

where *machinename* is localhost or an IP address, *port* is the port in which
Syncala is listening (typically port 7000), and *filename* being the path
where the previous ls command sent its output.

# Examples
## Successful Comparisons

This area will show an example of a successful comparison divided into
two subsections, and each subsection will have a clients point of view, 
and a server point of view.

### Directories are the same

For this example, the clients will be using a file called test
which contains the following output:
```
drwxrwxr-x 6 jbanass jbanass      4096 Nov 13 15:37 csc376-940-banassjo
drwxrwxr-x 7 jbanass jbanass      4092 Nov 16 20:21 examples
-rw-rw-r-- 1 jbanass jbanass     48772 Oct 31 20:19 examples.zip
-rw-rw-r-- 1 jbanass jbanass       765 Sep 11 21:05 home.desktop
-rw-rw-r-- 1 jbanass jbanass 160872342 Sep 11 21:07 jdk-8u20-linux-x64.tar.gz
-rw-rw-r-- 1 jbanass jbanass   7812369 Nov 13 16:07 monster.jar
-rw-rw-r-- 1 jbanass jbanass       508 Sep 11 21:05 root.desktop
-rw------- 1 jbanass jbanass       310 Oct 19 21:08 rxvt-unicode.desktop
-rwxrwxr-x 1 jbanass jbanass      1023 Nov 13 21:52 send.scala
-rwxrwxr-x 1 jbanass jbanass      1121 Nov 13 10:32 send.scala~
-rw-rw-r-- 1 jbanass jbanass         0 Nov 15 13:15 test
-rw-rw-r-- 1 jbanass jbanass       934 Nov 15 13:04 #test#
-rw-rw-r-- 1 jbanass jbanass       922 Nov 15 13:12 test~
-rw-rw-r-- 1 jbanass jbanass       472 Sep 11 21:05 tmp.desktop
```
#### Server/Processor Point Of View

`jbanass@ubuntu:~/Desktop/csc376-0940-banassjo$ ./target/start csp.net.Syncala`
`Client connected from /127.0.0.1:37147`
` `
`Client connected from /127.0.0.1:37149`
` `
`Printing results now...`
` `
`Processing finished cleaning up...`
` `
`Ready for new clients...`

#### Client Point Of View

##### Source
jbanass@ubuntu:~/Desktop$ ./send.scala localhost 7000 test
You are client h-127.0.0.1-37147
ATTENTION SOURCE
---------------------------------------------------------

The following files do not exist in the target



The following files do not exist in the source



The following files exist, but have been altered in some way via size

FILES SMALLER THAN SOURCE



FILES LARGER THAN SOURCE

FILES NOT COMPARED : SOURCE :

FILES NOT COMPARED : TARGET :

jbanass@ubuntu:~/Desktop$ 


***** Target
jbanass@ubuntu:~/Desktop$ ./send.scala localhost 7000 test
You are client h-127.0.0.1-37149
ATTENTION TARGET
---------------------------------------------------------

The following files do not exist in the target



The following files do not exist in the source



The following files exist, but have been altered in some way via size

FILES SMALLER THAN SOURCE



FILES LARGER THAN SOURCE

FILES NOT COMPARED : SOURCE :

FILES NOT COMPARED : TARGET :

jbanass@ubuntu:~/Desktop$ 
*** Directories are not the same

This example is using two files : The source will use test...

drwxrwxr-x 6 jbanass jbanass      4096 Nov 13 15:37 csc376-0940-banassjo
drwxrwxr-x 7 jbanass jbanass      4092 Nov 16 20:21 examples
-rw-rw-r-- 1 jbanass jbanass     48772 Oct 31 20:19 examples.zip
-rw-rw-r-- 1 jbanass jbanass       765 Sep 11 21:05 home.desktop
-rw-rw-r-- 1 jbanass jbanass 160872342 Sep 11 21:07 jdk-8u20-linux-x64.tar.gz
-rw-rw-r-- 1 jbanass jbanass   7812369 Nov 13 16:07 monster.jar
-rw-rw-r-- 1 jbanass jbanass       508 Sep 11 21:05 root.desktop
-rw------- 1 jbanass jbanass       310 Oct 19 21:08 rxvt-unicode.desktop
-rwxrwxr-x 1 jbanass jbanass      1023 Nov 13 21:52 send.scala
-rwxrwxr-x 1 jbanass jbanass      1121 Nov 13 10:32 send.scala~
-rw-rw-r-- 1 jbanass jbanass         0 Nov 15 13:15 test
-rw-rw-r-- 1 jbanass jbanass       934 Nov 15 13:04 #test#
-rw-rw-r-- 1 jbanass jbanass       922 Nov 15 13:12 test~
-rw-rw-r-- 1 jbanass jbanass       472 Sep 11 21:05 tmp.desktop

and the target will use test in the previous directory...

drwxr-xr-x 22 jbanass jbanass  4096 Nov 15 12:43 .
drwxr-xr-x  3 root    root     4096 Sep 11 14:24 ..
-rw-------  1 jbanass jbanass 22091 Nov 14 23:13 .bash_history
-rw-r--r--  1 jbanass jbanass   220 Sep 11 14:24 .bash_logout
-rw-r--r--  1 jbanass jbanass  3725 Sep 11 21:10 .bashrc
-rw-r--r--  1 jbanass jbanass  3637 Sep 11 14:24 .bashrc~
drwxrwxr-x  2 jbanass jbanass  4096 Sep 11 21:26 bin
drwx------ 10 jbanass jbanass  4096 Nov 15 09:48 .cache
drwx------ 10 jbanass jbanass  4096 Oct 11 19:30 .config
drwx------  3 jbanass jbanass  4096 Sep 11 21:04 .dbus
drwxr-xr-x  5 jbanass jbanass  4096 Nov 15 12:36 Desktop
drwx------  2 jbanass jbanass  4096 Nov 13 16:07 Downloads
drwxr-xr-x  3 jbanass jbanass  4096 Sep 11 21:04 .e
-rw-rw-r--  1 jbanass jbanass   606 Oct 11 20:44 .emacs
drwx------  3 jbanass jbanass  4096 Sep 11 21:15 .emacs.d
drwx------  2 jbanass jbanass  4096 Nov 15 09:57 .gconf
-rw-rw-r--  1 jbanass jbanass    53 Sep 14 00:58 .gitconfig
drwxrwxr-x  2 jbanass jbanass  4096 Sep 30 11:03 .gstreamer-0.10
dr-x------  2 jbanass jbanass     0 Nov 15 09:46 .gvfs
drwxrwxr-x  3 jbanass jbanass  4096 Sep 14 16:02 .ivy2
drwxr-xr-x  3 jbanass jbanass  4096 Sep 11 21:04 .local
drwx------  4 jbanass jbanass  4096 Sep 11 21:05 .mozilla
-rw-rw-r--  1 jbanass jbanass     0 Nov  8 19:36 nc
-rw-r--r--  1 jbanass jbanass   675 Sep 11 14:24 .profile
drwx------  4 jbanass jbanass  4096 Sep 30 11:21 .purple
drwxrwxr-x  4 jbanass jbanass  4096 Sep 11 21:28 .sbt
-rw-rw-r--  1 jbanass jbanass   120 Sep 16 13:38 .scala_history
drwx------  2 jbanass jbanass  4096 Sep 28 19:03 .ssh
drwxrwxr-x  2 jbanass jbanass  4096 Nov  2 11:23 target
drwxrwxr-x  2 jbanass jbanass  4096 Oct 11 19:24 Templates
-rw-rw-r--  1 jbanass jbanass     0 Nov 15 12:43 test
-rw-rw-r--  1 jbanass jbanass  2119 Nov  8 19:48 test~
drwx------  4 jbanass jbanass  4096 Oct 11 19:24 .thumbnails
-rw-------  1 jbanass jbanass  4522 Nov  9 12:28 .viminfo
-rw-------  1 jbanass jbanass    44 Nov 15 09:46 .Xauthority
-rw-------  1 jbanass jbanass   364 Nov 15 09:46 .xsession-errors

**** Server/Processor Point Of View

jbanass@ubuntu:~/Desktop/csc376-0940-banassjo$ ./target/start csp.net.Syncala
Client connected from /127.0.0.1:37151

Client connected from /127.0.0.1:37153

DID NOT MATCH REGEX: -rw-rw-r-- 1 jbanass jbanass    0 Nov 8 19:36 nc
DID NOT MATCH REGEX: drwxrwxr-x 2 jbanass jbanass 4096 Nov 2 11:23 target
DID NOT MATCH REGEX: -rw-rw-r-- 1 jbanass jbanass 2119 Nov 8 19:48 test~
DID NOT MATCH REGEX: -rw------- 1 jbanass jbanass 4522 Nov 9 12:28 .viminfo
Printing results now...

Processing finished cleaning up...

Ready for new clients...


#### Client Point Of View

##### Source

jbanass@ubuntu:~/Desktop$ ./send.scala localhost 7000 test
You are client h-127.0.0.1-37151
ATTENTION SOURCE
---------------------------------------------------------

The following files do not exist in the target

tmp.desktop
test~
#test#
send.scala~
send.scala
rxvt-univode.desktop
root.desktop
monster.jar
jdk-8u20-linux-x64.tar.gz
home.desktop
examples.zip
examples
csc376-0940-banassjo


The following files do not exist in the source

.xsession-errors
.Xauthority
.thumbnails
Templates
.ssh
.scala_history
.sbt
.purple
.profile
.mozilla
.local
.ivy2
.gvfs
.gstreamer-0.10
.gitconfig
.gconf
.emacs.d
.e
Downloads
Desktop
.dbus
.config
.cache
bin
.bashrc~
.bashrc
.bash_logout
.bash_history
..
.


The following files exist, but have been altered in some way via size

FILES SMALLER THAN SOURCE



FILES LARGER THAN SOURCE

FILES NOT COMPARED : SOURCE :

FILES NOT COMPARED : TARGET :

-rw-rw-r-- 1 jbanass jbanass    0 Nov 8 19:36 nc
drwxrwxr-x 2 jbanass jbanass 4096 Nov 2 11:23 target
-rw-rw-r-- 1 jbanass jbanass 2119 Nov 8 19:48 test~
-rw------- 1 jbanass jbanass 4522 Nov 9 12:28 .viminfo
jbanass@ubuntu:~/Desktop$ 
##### Target

jbanass@ubuntu:~/Desktop$ ./send.scala localhost 7000 ../test
You are client h-127.0.0.1-37153
ATTENTION TARGET
---------------------------------------------------------

The following files do not exist in the target

tmp.desktop
test~
#test#
send.scala~
send.scala
rxvt-univode.desktop
root.desktop
monster.jar
jdk-8u20-linux-x64.tar.gz
home.desktop
examples.zip
examples
csc376-0940-banassjo


The following files do not exist in the source

.xsession-errors
.Xauthority
.thumbnails
Templates
.ssh
.scala_history
.sbt
.purple
.profile
.mozilla
.local
.ivy2
.gvfs
.gstreamer-0.10
.gitconfig
.gconf
.emacs.d
.e
Downloads
Desktop
.dbus
.config
.cache
bin
.bashrc~
.bashrc
.bash_logout
.bash_history
..
.


The following files exist, but have been altered in some way via size

FILES SMALLER THAN SOURCE



FILES LARGER THAN SOURCE

FILES NOT COMPARED : SOURCE :

FILES NOT COMPARED : TARGET :

-rw-rw-r-- 1 jbanass jbanass    0 Nov 8 19:36 nc
drwxrwxr-x 2 jbanass jbanass 4096 Nov 2 11:23 target
-rw-rw-r-- 1 jbanass jbanass 2119 Nov 8 19:48 test~
-rw------- 1 jbanass jbanass 4522 Nov 9 12:28 .viminfo
jbanass@ubuntu:~/Desktop$ 
*** Sizes are different

This example will use two files, test...

drwxrwxr-x 6 jbanass jbanass      4096 Nov 13 15:37 csc376-0940-banassjo
drwxrwxr-x 7 jbanass jbanass      4092 Nov 16 20:21 examples
-rw-rw-r-- 1 jbanass jbanass     48772 Oct 31 20:19 examples.zip
-rw-rw-r-- 1 jbanass jbanass       765 Sep 11 21:05 home.desktop
-rw-rw-r-- 1 jbanass jbanass 160872342 Sep 11 21:07 jdk-8u20-linux-x64.tar.gz
-rw-rw-r-- 1 jbanass jbanass   7812369 Nov 13 16:07 monster.jar
-rw-rw-r-- 1 jbanass jbanass       508 Sep 11 21:05 root.desktop
-rw------- 1 jbanass jbanass       310 Oct 19 21:08 rxvt-unicode.desktop
-rwxrwxr-x 1 jbanass jbanass      1023 Nov 13 21:52 send.scala
-rwxrwxr-x 1 jbanass jbanass      1121 Nov 13 10:32 send.scala~
-rw-rw-r-- 1 jbanass jbanass         0 Nov 15 13:15 test
-rw-rw-r-- 1 jbanass jbanass       934 Nov 15 13:04 #test#
-rw-rw-r-- 1 jbanass jbanass       922 Nov 15 13:12 test~
-rw-rw-r-- 1 jbanass jbanass       472 Sep 11 21:05 tmp.desktop

and test2...

total : 1437
drwxrwxr-x 6 jbanass jbanass      4096 Nov 13 15:37 csc376-0940-banassjo
drwxrwxr-x 7 jbanass jbanass      4096 Oct 31 20:21 examples
-rw-rw-r-- 1 jbanass jbanass     48772 Oct 31 20:19 examples.zip
-rw-rw-r-- 1 jbanass jbanass       765 Sep 11 21:05 home.desktop
-rw-rw-r-- 1 jbanass jbanass 160872342 Sep 11 21:07 jdk-8u20-linux-x64.tar.gz
-rw-rw-r-- 1 jbanass jbanass     12343 Nov 13 16:07 monster.jar
-rw-rw-r-- 1 jbanass jbanass       508 Sep 11 21:05 root.desktop
-rw------- 1 jbanass jbanass       310 Oct 19 21:08 rxvt-unicode.desktop
-rwxrwxr-x 1 jbanass jbanass      1023 Nov 13 21:52 send.scala
-rwxrwxr-x 1 jbanass jbanass      1121 Nov 13 10:32 send.scala~
-rw-rw-r-- 1 jbanass jbanass         0 Nov 15 13:15 test
-rw-rw-r-- 1 jbanass jbanass       934 Nov 15 13:04 TestTarget
-rw-rw-r-- 1 jbanass jbanass       922 Nov 15 13:12 test~
-rw-rw-r-- 1 jbanass jbanass       472 Sep 11 21:05 tmp.desktop

### Server/Processor Point Of View

jbanass@ubuntu:~/Desktop/csc376-0940-banassjo$ ./target/start csp.net.Syncala
Client connected from /127.0.0.1:37159

Client connected from /127.0.0.1:37161

DID NOT MATCH REGEX: total : 1437
Printing results now...

Processing finished cleaning up...

Ready for new clients...

### Client Point Of View

#### Source
jbanass@ubuntu:~/Desktop$ ./send.scala localhost 7000 test
You are client h-127.0.0.1-37159
ATTENTION SOURCE
---------------------------------------------------------

The following files do not exist in the target

#test#


The following files do not exist in the source

TestTarget


The following files exist, but have been altered in some way via size

FILES SMALLER THAN SOURCE

monster.jar: 7812369 > 12343[Date Modified of Source: Nov 13 16:07]


FILES LARGER THAN SOURCE

examples: 4092 < 4096[Date Modified of Target: Oct 31 20:21]
FILES NOT COMPARED : SOURCE :

FILES NOT COMPARED : TARGET :

total : 1437
jbanass@ubuntu:~/Desktop$

**** Target

jbanass@ubuntu:~/Desktop$ ./send.scala localhost 7000 test
You are client h-127.0.0.1-37161
ATTENTION TARGET
---------------------------------------------------------

The following files do not exist in the target

#test#


The following files do not exist in the source

TestTarget


The following files exist, but have been altered in some way via size

FILES SMALLER THAN SOURCE

monster.jar: 7812369 > 12343[Date Modified of Source: Nov 13 16:07]


FILES LARGER THAN SOURCE

examples: 4092 < 4096[Date Modified of Target: Oct 31 20:21]
FILES NOT COMPARED : SOURCE :

FILES NOT COMPARED : TARGET :

total : 1437
jbanass@ubuntu:~/Desktop$

## Unsuccessful Comparisons

This section will specifically show a client error (like mistyping a file name
or trying to connect when a server does not exist)
### Client Errors
#### Server Not Up Error
##### Server/Processor Point Of View
      No output available, since server is not up
##### Client Point Of View
jbanass@ubuntu:~/Desktop$ ./send.scala localhost 7000 test
Sorry, something went wrong. Please try again! Perhaps the server isn't up?
jbanass@ubuntu:~/Desktop$ 
#### Incorrect filename Error
##### Server/Processor Point Of View
jbanass@ubuntu:~/Desktop/csc376-0940-banassjo$ ./target/start csp.net.Syncala

##### Client Point Of View
Only one Point Of View available for this test:

###### Source
jbanass@ubuntu:~/Desktop$ ./send.scala localhost 7000 test123
Sorry, file does not exist

jbanass@ubuntu:~/Desktop$