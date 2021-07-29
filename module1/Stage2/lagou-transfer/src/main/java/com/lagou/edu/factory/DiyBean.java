package com.lagou.edu.factory;

import com.lagou.edu.intf.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author hylu.Ivan
 * @date 2021/7/19 下午3:17
 * @description
 */
public class DiyBean {

    /**
     * 任务一：扫描指定包下的类是否包含注解，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */
    public static Map<String,Object> map = new HashMap<>();

    private static List<String> classPaths = new ArrayList<String>();

    private static List<Class> tranClass = new ArrayList<>();

    public static Object getBean(String id) {
        return map.get(id);
    }

    static {
        // 指定扫描包，对包下所有类进行遍历
        // 当类中存在注解时开始进行注解处理
        // 如果存在Component,serive注解时,去map中寻找实例，如果不存在，创建类的实例，并与注解中的value值一起存储到map中
        // 当扫描到@Autowired注解时，去map中寻找类或子类的实例，如果不存在，创建注解下的变量实例，并从map中取出当前类的实例，赋值之后再存入
        // 当扫描到transactional注解时，生成包含横切逻辑的代理对象封装到map中
        String basepackge = "com.lagou.edu";
        try {
           searchClass(basepackge);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    // 扫描类中存在组件注解
    public static void findAnno(String classpath) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            // Class<?> clazz1 = Class.forName("com.lagou.edu.intf.AnnoTest");
            Class<?> clazz = Class.forName(classpath);
            Annotation[] annotations = clazz.getAnnotations();
            // 遍历单个类中所有类上组件注解,判断该类是否需要交由ioc
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> aClass = annotation.annotationType();
                String key = null;
                if(aClass.equals(ComponentIntf.class)) {
                    ComponentIntf c = (ComponentIntf)annotation;
                    key = c.value();

                    if(key == null || "".equals(key)) key = lowerName(clazz.getSimpleName());
                    if(!map.containsKey(key)) {
                        map.put(key,clazz.getConstructor().newInstance());
                    }
                } else if(aClass.equals(RepositoryIntf.class)) {
                    RepositoryIntf r = (RepositoryIntf)annotation;
                    key = r.value();

                    if(key == null || "".equals(key)) key = lowerName(clazz.getSimpleName());
                    if(!map.containsKey(key)) {
                        map.put(key,clazz.getConstructor().newInstance());
                    }
                } else if(aClass.equals(ServiceIntf.class)) {
                    ServiceIntf s = (ServiceIntf)annotation;
                    key = s.value();
                    if(key == null || "".equals(key)) key = lowerName(clazz.getSimpleName());
                    if(!map.containsKey(key)) {
                        map.put(key,clazz.getConstructor().newInstance());
                    }
                } else if (aClass.equals(TransactionalIntf.class)) {
                    tranClass.add(clazz);
                }

            }
            // 遍历类中所有变量是否有注解
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Annotation[] annotatedType = field.getAnnotations();
                for (Annotation annotation : annotatedType) {
                    Class<? extends Annotation> aClass = annotation.annotationType();
                    if(aClass.equals(AutowiredIntf.class)) {
                        String key = field.getName();
                        // 如果容器中不存在key创建该对象
                        if(!map.containsKey(key)) {
                            map.put(key,((Class)field.getType()).getConstructor().newInstance());
                        }
                        // 从容器中找到当前类对象，并将根据autowire标记创建的对象赋值给成员变量再存回容器
                        Object o = getMapContains(clazz);
                        if(null != o) field.set(o,map.get(key));
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void findTran() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println(tranClass);
        for (Class aClass : tranClass) {
            // 通过生成动态代理对象，为标记该注解的类中所有方法添加事务控制的横切逻辑
            // 获取被标记的引用变量名，如果容器中不包含变量，根据变量名向容器中添加相关实例
            String key = lowerName(aClass.getSimpleName());
            if(!map.containsKey(key)) map.put(key,aClass.getConstructor().newInstance());

            // 从容器中获取被标记的实例
            Object o = map.get(key);
            // 从容器中获取代理对象类用于生成代理对象
            ProxyFactory proxyFactory = (ProxyFactory) map.get("proxyFactory");
            // 使用代理对象类，为被标记对象生成代理对象，并用代理对象替代原对象封装到容器中
            map.put(key,proxyFactory.getJdkProxy(o));
        }
    }

    /**
     * 根据传入类寻找map中是否存在该类或子类的实例，存在则返回，否则返回null
     * @param c
     * @return object
     */
    public static Object getMapContains(Class c) {
        Object res = null;
        for(Map.Entry<String,Object> entry : map.entrySet()) {
            Class tar = entry.getValue().getClass();
            // 如果传入类在map中类的子类
            if(c.isAssignableFrom(tar)) res = entry.getValue();
        }
        return res;
    }

    public static String lowerName(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     *
     * @param basePack
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void searchClass(String basePack) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //先把包名转换为路径,首先得到项目的classpath
        String classpath = DiyBean.class.getResource("/").getPath();
        //然后把我们的包名basPach转换为路径名
        basePack = basePack.replace(".", File.separator);
        //然后把classpath和basePack合并
        String searchPath = classpath + basePack;
        doPath(new File(searchPath));
        //这个时候我们已经得到了指定包下所有的类的绝对路径了。我们现在利用这些绝对路径和java的反射机制得到他们的类对象
        for (String s : classPaths) {
            //把 D:\work\code\20170401\search-class\target\classes\com\baibin\search\a\A.class 这样的绝对路径转换为全类名com.baibin.search.a.A
            // windows环境下
            // s = s.substring(s.indexOf("com")).replace("\\",".").replace(".class","");
            // macos环境下
            s = s.substring(s.indexOf("com")).
                    replace("/",".").
                    replace(".class","");
            if(!s.contains("$")) {
                // 定位到可被扫描的类时，进行注解扫描
                findAnno(s);
            }
        }
        for(Map.Entry<String,Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        findTran();
    }

    /**
     * 该方法会得到所有的类，将类的绝对路径写入到classPaths中
     * @param file
     */
    private static void doPath(File file) {
        if (file.isDirectory()) {//文件夹
            //文件夹我们就递归
            File[] files = file.listFiles();
            for (File f1 : files) {
                doPath(f1);
            }
        } else {//标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                classPaths.add(file.getPath());
            }
        }
    }
}
