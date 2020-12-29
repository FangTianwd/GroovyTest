package javatest;

import java.util.Date;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.jupiter.api.Test;

public class sadz {
    static ScriptEngineManager factory = new ScriptEngineManager();
    static ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");
    
    public Object run(String script) throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");
        return engine.eval(script).toString();
    }
    
    public static void main(String[] args) throws ScriptException {
        new sadz().run("package groovytest\n"
                + "\n"
                + "//class TestListAssert {}\n"
                + "// 集合操作\n"
                + "def list = [5,6,7];\n"
                + "println list[0];\n"
                + "println list.getAt(1);\n"
                + "println list.get(1);\n"
                + "println list.size()\n"
                + "list.add(8);\n"
                + "println list.size()\n"
                + "list.putAt(0, 4);\n"
                + "println list.get(0);\n"
                + "// list.set(0, 3);\n"
                + "// set方法 是赋值并返回原值\n"
                + "assert list.set(0, 3) == 4\n"
                + "println list.get(0);\n"
                + "\n"
                + "\n"
                + "\n"
                + "def list1 = ['a','b','c'];\n"
                + "def list2 = new ArrayList<String>(list1);\n"
                + "// 检测每个对应的item。判断是否相同\n"
                + "assert list2 == list1;\n"
                + "\n"
                + "// 浅克隆\n"
                + "def list3 = list1.clone();\n"
                + "//list1.add('d');\n"
                + "assert list3 == list1;\n"
                + ""
                + "return 1");
    }
    
    public void name() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engineByName = manager.getEngineByName("groovy");
        engineByName.getBindings(ScriptContext.ENGINE_SCOPE).clear();
        Object eval = engineByName.eval("package groovytest\n"
                + "\n"
                + "//class TestListAssert {}\n"
                + "// 集合操作\n"
                + "def list = [5,6,7];\n"
                + "println list[0];\n"
                + "println list.getAt(1);\n"
                + "println list.get(1);\n"
                + "println list.size()\n"
                + "list.add(8);\n"
                + "println list.size()\n"
                + "list.putAt(0, 4);\n"
                + "println list.get(0);\n"
                + "// list.set(0, 3);\n"
                + "// set方法 是赋值并返回原值\n"
                + "assert list.set(0, 3) == 4\n"
                + "println list.get(0);\n"
                + "\n"
                + "\n"
                + "\n"
                + "def list1 = ['a','b','c'];\n"
                + "def list2 = new ArrayList<String>(list1);\n"
                + "// 检测每个对应的item。判断是否相同\n"
                + "assert list2 == list1;\n"
                + "\n"
                + "// 浅克隆\n"
                + "def list3 = list1.clone();\n"
                + "//list1.add('d');\n"
                + "assert list3 == list1;\n"
                + ""
                + "return 1");
        System.out.println(eval);
    }
}
