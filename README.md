runelite-patched-client-deobfuscator
========

Runelite's injected client jar contains `@Named` annotations which can break deobfuscators, this will remove them.  
The patched jar can be found in `USERHOME\.runelite\cache\patched.cache`. Rename it to `.jar` it's a jar file anyway.

Example gradle arguments to run: `run --args="path/to/patched.jar"`

## Credits
* All the asm-related helper classes come from or are heavily influenced by those from https://github.com/runebox/runebox
