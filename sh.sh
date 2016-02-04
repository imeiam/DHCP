ifconfig wlan0 | grep 'inet addr' | awk -F ':' '{print $2}'
