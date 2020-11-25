package groovytest
// 读取文件
//new File('/Users/mac/eclipse-workspace/GroovyTest/', 'pom.xml').eachLine{ line ->
//    println line
//}
//new File('/Users/mac/eclipse-workspace/GroovyTest/', 'pom.xml').eachLine{ line, nb ->
//	println "Line $nb: $line"
//}

def count = 0, MAXSIZE = 27
new File('/Users/mac/eclipse-workspace/GroovyTest/', 'pom.xml').withReader { reader ->
	while (reader.readLine()) {
		if (++count > MAXSIZE) {
			throw new RuntimeException('pom.xml should only have 3 verses')
		}
	}
}

// 将该文件每行收集起来
def list = new File('/Users/mac/eclipse-workspace/GroovyTest/', 'pom.xml').collect {it}
println list

