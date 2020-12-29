package javatest;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * 执行groovy工具
 *
 * @author Wanghongli
 * @version GroovyRunUtil.java, v 0.1 2020/5/25 09:42 Wanghongli Exp $
 */
public class GroovyRunUtil {

    private static final ScriptEngineManager SEM = new ScriptEngineManager();

    /**
     * 获取默认脚本引擎实例(Groovy engine)
     *
     * @return  脚本引擎实例 （Groovy）
     */
    public static ScriptEngine getScriptEngine() {
        return SEM.getEngineByName("groovy");
    }

    /**
     * groovy脚本执行
     * @param runContent
     * @param params
     * @return
     */
    public static Object run(String runContent, Map<String, Object> params) throws ScriptException {
        ScriptEngine scriptEngine = getScriptEngine();
        scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).clear();
        scriptEngine.put("params", params);
        return scriptEngine.eval(runContent);
    }
    public static void main(String[] args) throws ScriptException {
        Object run = GroovyRunUtil.run("package groovytest\n"
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
                + "return 1", null);
    }
}