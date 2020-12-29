package groovytest
//import groovy.json.JsonBuilder
class Looper {
    private Closure code
 
    static Looper loop( Closure code ) {
       new Looper(code:code)
    }
 
    void until( Closure test ) {
       code()
       while (!test()) {
          code()
       }
    }
 }
 int i = 0
 loop {
    println("Looping : "  + i)
    i += 1
 } until { i == 5 }
//def json = new JsonBuilder()
//def ip  = '127.0.0.1'
//json.callback="http://10.0.2.114:8080/cloudscale/api/v1/testServer/callBackConnectResult"
//json.execHosts{
//    hostIp ip
//}
//println json