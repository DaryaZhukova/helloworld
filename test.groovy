@Grapes(
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7')
)
import groovyx.net.http.RESTClient
import org.apache.http.entity.*
import hudson.model.*
def encodeZipFile( Object data ) throws UnsupportedEncodingException {
    if ( data instanceof File ) {
        def entity = new FileEntity( (File) data, "application/zip" );
        entity.setContentType( "application/zip" );
        return entity
    } else {
        throw new IllegalArgumentException( 
            "Don't know how to encode ${data.class.name} as a zip file" );
    }
}

def upload() {
   def repo = "maven-helloworld"
   def gav = parse_gav()
   def file = new File("pipeline-dzhukova-${BUILD_NUMBER}.tar.gz")
   def rest = new RESTClient( "http://192.168.17.4/nexus/repository/${repo}")
        rest.auth.basic 'admin', 'admin'
        rest.encoder.'application/zip' = this.&encodeZipFile
        println "http://192.168.17.4/nexus/repository/${repo}/${gav[1]}/${BUILD_NUMBER}/${gav[1]}-${BUILD_NUMBER}.tar.gz"
        rest.put(path: "http://192.168.17.4/nexus/repository/${repo}/${gav[1]}/${BUILD_NUMBER}/${gav[1]}-${BUILD_NUMBER}.tar.gz", body: file, requestContentType: 'application/zip')

}
String[] parse_gav() {
        def pom = new XmlSlurper().parse(new File("${WORKSPACE}/helloworld-ws/pom.xml"))
        def gavs = []
        gavs.add(pom.parent.groupId)
        gavs.add(pom.artifactId)
        gavs.add(pom.parent.version)
        return gavs
}
upload()

