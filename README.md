# Mouser
Keep the machine awake by touching the mouse every minute.

Run it as jar (adjust the path):
> java -jar ~/Downloads/mouser/Mouser.jar

Could be usefull to add an alias to .zshrc:
```
alias mouser="java -jar ~/Downloads/mouser/Mouser.jar"
```

To stop it from running at specific time, AT command could be used. 
Create a simple kill script first:

killmouser.sh:
```
#!/bin/sh
process=`pgrep -f Mouser.jar`
kill ${process}
```

Then run AT command, like this:
```
at 6:08 pm -f killmouser.sh
```

It will run at specified time (here - 6:08pm) and execute command (killmouser.sh)
