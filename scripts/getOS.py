import os
import platform

def getOS():
	return os.name

def getPlatform():
	#return Linux for Linux OS
	#return Darwin for Macintosh OS
	#return Windows for Windows OS
	return platform.system()