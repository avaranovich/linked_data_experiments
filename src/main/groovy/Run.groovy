import groovy.json.JsonBuilder

class Run{
    void run(){
        def m = new Repo101()
        //m.getAllPages()
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



