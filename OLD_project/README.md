Microsoft Paint Robot Project
-----------------------------


Errors / debugging
------------------
*Only do the below if you get the error described*

If you get a 'cannot find Python' error or similar, <a href="https://github.com/felixrieseberg/windows-build-tools/issues/56">read this</a>

Doing the above caused the Python install to hang indefinetly. So I followed a comment <a href="https://github.com/felixrieseberg/windows-build-tools/issues/147">here</a> and did: `npm uninstall Python`, `npm i --production --verbose windows-build-tools` which worked.


Research Notes
--------------
*I'll probably delete this section of the ReadMe once the project is finished. Just notes to myself*

MSPaint.exe does not have any useful command line arguments. You can only specify the file to be opened.