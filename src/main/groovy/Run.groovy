import groovy.json.JsonBuilder

class Run{
    void run(){
        def m = new Repo101()
        def fpConcepts = m.examineConcepts('http://101companies.org/resources/concepts/Functional_programming_language')
        def ooConcepts = m.examineConcepts('http://101companies.org/resources/concepts/OO_programming_language')
        
        //def overlappingConcepts = (fpConcepts << ooConcepts).flatten().unique()    
        def overlappingConcepts = fpConcepts.intersect(ooConcepts)
        println "overlapping concepts: "
        println overlappingConcepts
        println "# overlapping concepts: " + overlappingConcepts.size()
  
        m.statistics()
        /*def json = new JsonBuilder()
        json.wiki{
            pageCount(allPages.size())
            baseUrl('http://101companies.org/wiki')
            pages(allPages.collect { page ->
                ["page": page.getProperties()]
            })
        }

        new File("dump.json").withWriter {it << json.toPrettyString() } */
    }
}



