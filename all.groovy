@Grapes(
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7')
)
import groovyx.net.http.RESTClient
import org.apache.http.entity.*
import hudson.model.*
def encodeTarFile( Object data ) throws UnsupportedEncodingException {
   def entity = new FileEntity( (File) data, "application/tar.gz" );
   entity.setContentType( "application/tar.gz" );
   return entity
   }
String[] parse_gav(path) {
        def pom = new XmlSlurper().parse(new File("${path}/pom.xml"))
        def gavs = []
        gavs.add(pom.groupId)
        gavs.add(pom.artifactId)
        gavs.add(pom.version)
        return gavs
}
def upload() {
   def path = System.getenv('WORKSPACE');
   def buildnumber = System.getenv('BUILD_NUMBER')
   def repo = "maven-helloworld"
   def gav = parse_gav(path)
   def file = new File("${path}/artifact.tar.gz")
   def rest = new RESTClient( "http://192.168.17.4/nexus/repository/${repo}")
        rest.auth.basic 'user', 'user'
        rest.encoder.'application/tar.gz' = this.&encodeTarFile
        rest.put( path:  "http://192.168.17.4/nexus/repository/${repo}/${gav[0]}/${gav[1]}/${buildnumber}/${gav[1]}-${buildnumber}.tar.gz", body: file, requestContentType: 'application/tar.gz')

}


def download(def path) {
   name = path.split("/")[6]
   def repo = "maven-helloworld"
   def rest = new RESTClient( "http://192.168.17.4/nexus/repository/${repo}")
        rest.auth.basic 'user', 'user'
        def resp = rest.get( path:  "${path}")
   new File("./${name}.tar.gz") << resp.data
}

def cli = new CliBuilder(usage:'ls')
import org.apache.commons.cli.Option
cli.with
{
   h(longOpt: 'help', 'Usage Information', required: false)
   p(longOpt: 'path', 'ARTIFACT_NAME', args: 1, required: false)
   c(longOpt: 'option', 'push/pull', args: 1, required: true)
}

def options = cli.parse(args)
println options.p
println options.c
if (options.c=='pull') {   
        download(options.p)
}
if (options.c=='push') {
        upload()
}


