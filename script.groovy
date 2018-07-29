@Grapes(
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7')
)
import groovyx.net.http.RESTClient
import org.apache.http.entity.*
import hudson.model.*
//import hudson.EnvVars
//EnvVars envVars = build.getEnvironment(listener);
def path = System.getenv('WORKSPACE');
def buildnumber = System.getenv('BUILD_NUMBER')
def repo = "maven-helloworld"
String[] parse_gav() {
        def pom = new XmlSlurper().parse(new File('/opt/jenkins/workspace/build_helloworld/pom.xml'))
        def gavs = []
        gavs.add(pom.groupId)
        gavs.add(pom.artifactId)
        gavs.add(pom.version)
        return gavs
}
def gav = parse_gav()
//new File("${path}").eachFileMatch(~/.*.war/) { myfile ->  
//        art = myfile.getName()
//         art_name = myfile.getName().split("\\-")[0]
//    }  
//println ("${path}/${art}")

def file = new File("${path}/artifact.tar.gz")
def rest = new RESTClient( "http://192.168.17.4/repository/${repo}")
	rest.auth.basic 'admin', 'admin123'
        rest.encoder.'application/war' = this.&encodeWarFile
        rest.put( path:  "http://192.168.17.4/repository/${repo}/${gav[0]}/${gav[1]}/${buildnumber}/${gav[1]}-${buildnumber}.tar.gz", body: file, requestContentType: 'application/war')


def encodeWarFile( Object data ) throws UnsupportedEncodingException {
        def entity = new FileEntity( (File) data, "application/war" );
        entity.setContentType( "application/war" );
        return entity
}
 
