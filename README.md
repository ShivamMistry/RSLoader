#RSLoader

###General Information

RSLoader is a very lightweight loader for the RuneScape game.

It currently does _NOT_ display advertisements, so __do NOT use this to play on an account without a membership subscription__.

###Dependencies

This project requires that you have the following software installed:
* A Java 7 compiler (if you wish to build the loader from source)
* A Java interpreter or virtual machine _(use of Oracle's JRE is recommended)_
* A graphical window manager for your operating system

This project optionally requires the following:
* libgnome for opening URLs on some Unix-like operating systems (e.g. Linux), if not installed, URLs are printed to the console anyway
* Apache Ant, for quick and easy building of the project

###Building

To build RSLoader (with Ant installed), simply use the command `ant` in the main project directory containing the build.xml file.
Otherwise, use `javac` and specify the source files to compile the project.

To run RSLoader from the built class files, use the command:

`java -cp out/production/RSLoader com.speed.RSLoader`

Alternatively, if using the Ant build, you can run it from the redistributable jar file:

`java -jar out/rsloader.jar`

If you downloaded the RSLoader binary, to run it, execute the command

`java -jar rsloader.jar`

###Usage

To use a different language, pass one of these numbers with the -l or --lang argument:

* 0 - English
* 1 - Deutsch
* 2 - Français
* 3 - Português
* 6 - Español latinoamericano

For example, to load the French game with the downloaded binary

`java -jar rsloader.jar -l 2`

The nolimits option will disable max/min sizes of the game applet, allowing it to be resized to any amount.
To enable this, use the switch -nl or --nolimits

`java -jar rsloader.jar --nolimits`


###License

This project is licensed under the LGPL, see license.txt for more information.
