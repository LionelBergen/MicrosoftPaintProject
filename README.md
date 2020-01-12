Microsoft Paint Robot Project
-----------------------------


Errors / debugging
------------------
*Only do the below if you get the error described*

If you get a 'cannot find Python' error or similar, <a href="https://github.com/felixrieseberg/windows-build-tools/issues/56">read this</a>

Doing the above caused the Python install to hang indefinetly. So I followed a comment <a href="">here</a> and did: `npm uninstall Python`, `npm i --production --verbose windows-build-tools` which worked.