import os
import getOS
import getpass

os.environ['GRADLEW'] = './gradlew'

platfrom = getOS.getPlatform()
if platfrom == 'Linux':
	os.environ['ANDROID_HOME'] = '/home/' + getpass.getuser() + '/android-sdk-linux'
elif platfrom == 'Darwin':
	os.environ['ANDROID_HOME'] = '/Users/' + getpass.getuser() + '/android-sdk-macosx'
elif platfrom == 'Windows':
	os.environ['GRADLEW'] = 'gradlew'
	os.environ['ANDROID_HOME'] = 'Y:/android-sdk-windows/'

os.environ['ANDROID_TOOLS'] = os.environ['ANDROID_HOME'] + '/tools/'
os.environ['PLATFORM_TOOLS'] = os.environ['ANDROID_HOME'] + '/platform-tools/'

build_tool = '23.0.3'
os.environ['ANDROID_BUILD'] = os.environ['ANDROID_HOME'] + '/build-tools/' + build_tool + '/'