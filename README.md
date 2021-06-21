Microsoft Paint Robot Project
-----------------------------

# Running The Program  
Manually open paint and full screen it, ensuring the program begins at screen coordinate 0,0. Then close paint.  
MS Paint will remember size and position. There is not any other way to open paint in other positions/size  


Research Notes
--------------
*I'll probably delete this section of the ReadMe once the project is finished. Just notes to myself*

MSPaint.exe does not accept any command line arguments apart from the file. You can only specify the file to be opened.

I've noticed when re-opening paint, the last image size is still used. For example, 500x500 if the last image edited was 500x500. 
Wasted a bunch of time on ProcessMonitor but I cannot tell where/how MSPaint saves this information.

Ctrl+E is the shortcut to open image properties