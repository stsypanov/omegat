#!/usr/bin/env bash

# installation script 2012-06-03

# read version number from changes.txt

omtversion=$(grep -o -m1 "OmegaT\s*[\.0-9]*[0-9 a-z]*" ./changes.txt)

# substitute underlines for spaces in version number

omtversionul=$(echo $omtversion | sed 's/ /_/g')

# check whether /opt/omegat/<OmegaT version> exists
# exit if it does

if  [ -d /opt/omegat/$omtversionul ] ; then

   echo "$omtversion is already installed"

   exit

else

   # create /opt/omegat and
   # /opt/omegat/<OmegaT version>

   sudo mkdir -p /opt/omegat/$omtversionul

   # copy OmegaT files and folders
   # to /opt/omegat/<OmegaT version>

   sudo cp -r ./* /opt/omegat/$omtversionul

   cd /opt/omegat/$omtversionul

fi


# handling plugins folder

if  [ -d /opt/omegat/plugins ] ; then

   # /opt/omegat/plugins exists,
   # delete /opt/omegat/<OmegaT version>/plugins

   sudo rm -d -f -r /opt/omegat/$omtversionul/plugins

else

   # /opt/omegat/plugins does not exist,
   # move plugins folder from within application

   sudo mv /opt/omegat/$omtversionul/plugins /opt/omegat

fi

# symlink from /opt/omegat/plugins to plugins folder within OmegaT

sudo ln -s /opt/omegat/plugins /opt/omegat/$omtversionul/plugins

# handling scripts folder

if  [ -d /opt/omegat/scripts ] ; then

   # /opt/omegat/scripts exists,
   # delete /opt/omegat/<OmegaT version>/scripts

   sudo rm -d -f -r /opt/omegat/$omtversionul/scripts

else

   # /opt/omegat/scripts does not exist,
   # move scripts folder from within application

   sudo mv /opt/omegat/$omtversionul/scripts /opt/omegat

fi

# symlink from /opt/omegat/scripts to scripts folder within OmegaT

sudo ln -s /opt/omegat/scripts /opt/omegat/$omtversionul/scripts

# handling jre folder

if  [ -d /opt/omegat/$omtversionul/jre ] ; then

   # user is installing OmegaT with JRE
   # deletes old local JRE, if present
   # move jre folder from within application
   # symlink from /opt/omegat/jre to jre folder within OmegaT

   sudo rm -d -f -r /opt/omegat/jre

   sudo mv /opt/omegat/$omtversionul/jre /opt/omegat

   sudo ln -s /opt/omegat/jre /opt/omegat/$omtversionul/jre

else

   # user is installing OmegaT without JRE
   # check whether /opt/omegat/jre exists

   if  [ -d /opt/omegat/jre ] ; then

      # /opt/omegat/jre exists,
      # symlink from /opt/omegat/jre to jre folder within OmegaT

      sudo ln -s /opt/omegat/jre /opt/omegat/$omtversionul/jre

   else

      # /opt/omegat/jre does not exist,
      # do nothing
      echo

   fi

fi

## symlink just installed version to /opt/omegat/OmegaT-default

sudo ln -s -b -T /opt/omegat/$omtversionul /opt/omegat/OmegaT-default

# symlink bash OmegaT launch script
# from <OmegaT version> to /usr/local/bin

sudo ln -s -b /opt/omegat/OmegaT-default/OmegaT /usr/local/bin/omegat

# symlink Kaptain OmegaT launch script
# from <OmegaT version> to /usr/local/bin

sudo ln -s -b /opt/omegat/OmegaT-default/omegat.kaptn /usr/local/bin/omegat.kaptn

sudo chmod +x /usr/local/bin/omegat /usr/local/bin/omegat.kaptn

exit
