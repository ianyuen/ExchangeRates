import os
import sys
import setEnv
import config

projectPath = '../app'

gradleVersion = '2.1.0'

def startDDMS():
	command = os.environ['ANDROID_TOOLS'] + 'ddms'
	os.system(command)

def createProject():
	command = os.environ['ANDROID_TOOLS'] + 'android create project --target android-' + config.TARGET_API + ' --name ' + config.APP_NAME + ' --path ' + projectPath + ' --activity Splash --package ' + config.APP_PACKAGE + ' --gradle --gradle-version ' + gradleVersion
	os.system(command)

def installAPK():
	if config.RELEASE == 0:
		build = 'debug'
	else:
		build = 'release'
	command = os.environ['PLATFORM_TOOLS'] + 'adb install -r ' + projectPath + '/build/outputs/apk/app-' + build + '.apk'
	os.system(command)

def makeAPK():
	if config.RELEASE == 0:
		build = 'debug'
	else:
		build = 'release'

	os.chdir(os.getcwd() + '/' + projectPath)
	command = os.environ['GRADLEW'] + ' assemble' + build
	os.system(command)

if len(sys.argv) == 1:
	print 'copy config.py from template folder to root trunk'
	print 'edit OS environment in setEnv.py'
	print 'edit config of project in config.py'
	print ''
	print 'call android.py create to create project'
	print 'call android.py make to make apk'
	print 'call android.py ddms to start DDMS'
	print 'call android.py install to install apk'

else:
	argument = str(sys.argv[1])
	if argument == 'ddms':
		startDDMS()
	elif argument == 'make':
		makeAPK()
	elif argument == 'create':
		createProject()
	elif argument == 'install':
		installAPK()