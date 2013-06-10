import com.tinkerpop.blueprints.Direction
import com.tinkerpop.blueprints.impls.sail.SailGraph
import com.tinkerpop.blueprints.impls.sail.SailVertex
import com.tinkerpop.blueprints.impls.sail.impls.LinkedDataSailGraph
import com.tinkerpop.gremlin.groovy.Gremlin
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonDelegate
import net.fortytwo.sesametools.reposail.RepositorySail
import org.openrdf.query.resultio.TupleQueryResultFormat
import org.openrdf.repository.Repository
import org.openrdf.repository.http.HTTPRepository

import static Repo101.Properties.*

class Repo101 {
    private static final String repoURI = 'http://triples.101companies.org/openrdf-sesame/repositories/wiki2/';
    static Repository repo;
    static SailGraph graph;

    public static class Properties {
        public static DEPENDS_ON = 'http://101companies.org/property/dependsOn'
        public static IDENTIFIES = 'http://101companies.org/property/identifies'
        public static LINKS_TO = 'http://101companies.org/property/linksTo'
        public static CITES = 'http://101companies.org/property/cites'
        public static USES = 'http://101companies.org/property/uses'
        public static IMPLEMENTS = 'http://101companies.org/property/implements'
        public static INSTANCE_OF = 'http://101companies.org/property/instanceOf'
        public static IS_A = 'http://101companies.org/property/isA'
        public static DEVELOPED_BY = 'http://101companies.org/property/developedBy'
        public static REVIEWED_BY = 'http://101companies.org/property/reviewedBy'
        public static RELATES_TO = 'http://101companies.org/property/relatesTo'
        public static LABEL = 'http://www.w3.org/2000/01/rdf-schema#label'
        public static PAGE = 'http://semantic-mediawiki.org/swivt/1.0#page'
        public static TYPE = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#type'

        private static createProperty(ns, name){
            if (ns == 'wiki'){
                return Repo101.graph.uri('wiki', 'Property-3A:' + name)
            }
            def prop =  Repo101.graph.uri(ns + ':' + name)
            return prop
        }
    }

    static {
        Gremlin.load()
        repo = new HTTPRepository(repoURI);
        repo.setPreferredTupleQueryResultFormat(TupleQueryResultFormat.JSON)

        repo.initialize();

        graph = new LinkedDataSailGraph(new SailGraph(new RepositorySail(repo)));
        graph.addDefaultNamespaces()
        graph.addNamespace('v101','http://101companies.org/properties/')
        graph.addNamespace('wiki', 'http://101companies.org/resource/')
        graph.addNamespace('swivt', 'http://semantic-mediawiki.org/swivt/1.0')
        graph.addNamespace('rdf', 'http://www.w3.org/1999/02/22-rdf-syntax-ns#')
    }

    public void getAllPages() {
        // getting all pages
        graph.addNamespace('wiki', 'http://101companies.org/resource/')
        //def labels = [graph.uri('wiki:instanceOf')]

        //g.addNamespace(concept, ‘http://101companies.org/resources/concepts’)
        def nsLanguage = getResource('http://101companies.org/resources/concepts/Functional_programming_language')
        println(nsLanguage)

        nsLanguage.inE('http://101companies.org/property/instanceOf').toList().collect{
            def lang = it.rawEdge.subject
            print lang
            getResource(lang).inE('http://101companies.org/property/uses').toList().collect{
                //contribution
                def contrib = it.rawEdge.subject
                getResource(contrib).outE('http://101companies.org/property/mentions').toList().collect{
                     
                getResource(it.rawEdge.object).outE('http://101companies.org/property/instanceOf').filter{
                        print it.inV.outE('rdf:type').inV.toList().collect{it.label.toString()}
                        return true 
                    }.toList()
                    print '\n'
                }
            }
        }

        //def allPages = graph.E.has('label', PAGE).inV[0..11].toList()//

        /*def inV = allPages.in.toList()

        def r = inV[0]
        println('resource: ' + r.getId())

        return  [new Resource(r)]  */

        // TODO: have a look at http://www.tinkerpop.com/docs/javadocs/blueprints/2.1.0/index.html?com/tinkerpop/blueprints/oupls/sail/Matcher.html
        // perhaps we don't need to traverse graph, but rather apply the matcher directly

        /*allPages.toList().collect {
          def inV = it.in.toList()

          def r = inV[0]
          println('resource: ' + r.getId())
        }*/
    }

    //'http://101companies.org/resource/Namespace-3ANamespace'
    public SailVertex getResource(url){
        return graph.v(url)
    }
}