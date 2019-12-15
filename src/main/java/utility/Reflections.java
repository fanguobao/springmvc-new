package utility;

import java.lang.reflect.*;
import java.util.Vector;

public class Reflections {
    Vector mVResult = new Vector();

    /**
     * 功能：构造函数显示
     *
     * @param c1 Class
     */
    public static void printConstructors(Class c1) {
        Constructor[] constructors = c1.getDeclaredConstructors();
        for (int i = 0; i < constructors.length; i++) {
            Constructor c = constructors[i];
            String name = c.getName();
            System.out.print("   " + name + "(");
            Class[] paramTypes = c.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if (j > 0)
                    System.out.print("Par, ");
            }
            System.out.println(");");
        }
    }

    /**
     * 功能：方法显示
     *
     * @param c1 Class
     */
    public static void printMethods(Class c1) {
        Method[] methods = c1.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            Class retType = m.getReturnType();
            String name = m.getName();

            System.out.print(Modifier.toString(m.getModifiers()));
            System.out.println(" | " + retType.getName() + " |" + name + "(");

            Class[] paramTypes = m.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if (j > 0)
                    System.out.print(", ");
                System.out.println(paramTypes[j].getName());
            }
            System.out.println("):");
        }
    }

    /**
     * SchemaSet的转换
     * @param a SchemaSet
     * @param b SchemaSet
     * @return boolean
     */
    public boolean transFields(SchemaSet a, SchemaSet b) {
        if (a != null) {
            int n = b.size();
            try {
                Class c1 = a.getObj(1).getClass();
                a.clear();
                for (int i = 1; i <= n; i++) {
                    Object c = c1.newInstance();
//                    transFields((Schema) c, (Schema) b.getObj(i));
                    a.add((Schema) c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * 把对象的值赋给对象
     *
     * @param a Schema
     * @param b Schema
     * @return Object
     */
    public Object transFields(Schema a, Schema b) {
        Class c1 = a.getClass();
        Class c2 = b.getClass();

        Field[] fieldsDest = c1.getDeclaredFields();
        Field[] fieldsOrg = c2.getDeclaredFields();

        AccessibleObject.setAccessible(fieldsDest, true);
        AccessibleObject.setAccessible(fieldsOrg, true);
        for (int i = 0; i < fieldsDest.length; i++) {
            Field f = fieldsDest[i];
            Class type = f.getType();
            String name = f.getName();
            String typeName = type.getName();

            if (name.equals("FIELDNUM") || name.equals("PK") || name.equals("mErrors") || name.equals("fDate")) {
                continue;
            }
            for (int j = 0; j < fieldsOrg.length; j++) {
                //得到数据源的数据
                Field f1 = fieldsOrg[j];
                String name1 = f1.getName();
                String typeName1 = type.getName();

                if (name.equals("FIELDNUM") || name.equals("PK") || name.equals("mErrors") || name.equals("fDate")) {
                    continue;
                }
                //赋值转换
                try {
                    if ((typeName.equals(typeName1)) && (name1.equals(name))) {
                        switch (transType(typeName)) {
                            case 3:
                                f.setDouble(a, f1.getDouble(b));
                                break;
                            case 5:
                                f.setInt(a, f1.getInt(b));
                                break;
                            case 93:
                                f.set(a, f1.get(b));
                                break;
                            default:
                                f.set(a, f1.get(b));
                                break
                                        ;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
        return a;
    }

    /**
     * 比较两个对象是否内容相同
     *
     * @param a Schema
     * @param b Schema
     * @return boolean
     */
    public boolean compareFields(Schema a, Schema b) {
        boolean aFlag = true;
        mVResult.clear();

        Class c1 = a.getClass();
        Class c2 = b.getClass();

        Field[] fieldsDest = c1.getDeclaredFields();
        Field[] fieldsOrg = c2.getDeclaredFields();

        AccessibleObject.setAccessible(fieldsDest, true);
        AccessibleObject.setAccessible(fieldsOrg, true);

        for (int i = 0; i < fieldsDest.length; i++) {
            Field f = fieldsDest[i];
            Class type = f.getType();
            String name = f.getName();
            String typeName = type.getName();

            if (name.equals("FIELDNUM") || name.equals("PK") || name.equals("mErrors") || name.equals("fDate")) {
                continue;
            }

            for (int j = 0; j < fieldsOrg.length; j++) {
                //得到数据源的数据
                Field f1 = fieldsOrg[j];
                String name1 = f1.getName();
                String typeName1 = type.getName();

                //取出冗余变量
                if (name.equals("FIELDNUM") || name.equals("PK") || name.equals("mErrors") || name.equals("fDate")) {
                    continue;
                }
                //赋值转换
                if ((typeName.equals(typeName1)) && (name1.equals(name))) {
                    try {
                        switch (transType(typeName)) {
                            case 3:
                                if (f.getDouble(a) != f1.getDouble(b)) {
                                    String tStr = name + "^" + f.getDouble(a) + "|" + f1.getDouble(b);
                                    mVResult.addElement(tStr);
                                    aFlag = false;
                                }
                                break;
                            case 5:
                                if (f.getInt(a) != f1.getInt(b)) {
                                    String tStr = name + "^" + f.getInt(a) + "|" + f1.getInt(b);
                                    mVResult.addElement(tStr);
                                    aFlag = false;
                                }
                                break;
                            case 93:
                                if (f.get(a) != f1.get(b)) {
                                    String tStr = name + "^" + f.get(a) + "|" + f1.get(b);
                                    mVResult.addElement(tStr);
                                    aFlag = false;
                                }
                                break;
                            default:
                                if (f.get(a) != f1.get(b)) {
                                    String tStr = name + "^" + f.get(a) + "|" + f1.get(b);
                                    mVResult.addElement(tStr);
                                    aFlag = false;
                                }
                                break;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            }
        }
        return aFlag;
    }

    /**
     * 同步两个对象
     *
     * @param a Schema
     * @param b Schema
     * @return Object
     */
    public Object synchronizeFields(Schema a, Schema b) {
        boolean aFlag = true;
        mVResult.clear();

        Class c1 = a.getClass();
        Class c2 = b.getClass();

        Field[] fieldsDest = c1.getDeclaredFields();
        Field[] fieldsOrg = c2.getDeclaredFields();

        AccessibleObject.setAccessible(fieldsDest, true);
        AccessibleObject.setAccessible(fieldsOrg, true);

//    System.out.println ("------------------comp print Fields ----------------");
        for (int i = 0; i < fieldsDest.length; i++) {
            Field f = fieldsDest[i];
            Class type = f.getType();
            String name = f.getName();
            String typeName = type.getName();

            if (name.equals("FIELDNUM") || name.equals("PK") ||
                    name.equals("mErrors") || name.equals("fDate")) {
                continue;
            }

            for (int j = 0; j < fieldsOrg.length; j++) {
                //得到数据源的数据
                Field f1 = fieldsOrg[j];
//                    Class type1 = f1.getType();
                String name1 = f1.getName();
                String typeName1 = type.getName();
                //取出冗余变量

                if (name.equals("FIELDNUM") || name.equals("PK") ||
                        name.equals("mErrors") || name.equals("fDate")) {
                    continue;
                }
                //赋值转换
                if ((typeName.equals(typeName1)) && (name1.equals(name))) {

                    switch (transType(typeName)) {
                        case 3:
                            try {
                                if (f.getDouble(a) != f1.getDouble(b)) {
                                    f.setDouble(a, f1.getDouble(b));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break
                                    ;
                        case 5:
                            try {
                                if (f.getInt(a) != f1.getInt(b)) {
                                    f.setInt(a, f1.getInt(b));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break
                                    ;
                        case 93:
                            try {
                                if (f.get(a) != f1.get(b)) {
                                    f.set(a, f1.get(b));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break
                                    ;
                        default:
                            try {
                                if (f.get(a) != f1.get(b)) {
                                    f.set(a, f1.get(b));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break
                                    ;
                    }
                }
            }
        }
//    System.out.println("---------------------------- compare end-----------------------");
        return a;
    }

    public void printFields(Object a) {
        Class c1 = a.getClass();
        Field[] fieldsDest = c1.getDeclaredFields();
        AccessibleObject.setAccessible(fieldsDest, true);

        System.out.println("------------------trans print Fields ----------------");
        System.out.println("----fieldDest.length:" + fieldsDest.length);
        for (int i = 0; i < fieldsDest.length; i++) {
            Field f = fieldsDest[i];
            Class type = f.getType();
            String name = f.getName();
            String typeName = type.getName();
            System.out.println("[Time]::" + i + "[colname]:" + name + "[Typename]:" + typeName);
            if (name.equals("FIELDNUM") || name.equals("PK") || name.equals("mErrors") || name.equals("fDate")) {
                continue;
            }
            System.out.println("----fieldOrg.length:" + fieldsDest.length);
            try {
                switch (transType(typeName)) {
                    case 3:
                        System.out.println("***************double::" + name + "---" + f.getDouble(a));
                        break;
                    case 5:
                        System.out.println("*************************Int::" + name + "---" + f.getInt(a));
                        break;
                    case 93:
                        System.out.println("*******************String::" + name + "---" + f.get(a));
                        break;
                    default:
                        System.out.println("------Default:" + f.get(a));
                        break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
            }

        }

        System.out.println("-------- print end-------");

    }

    public static void printFields(Class c1) {
        Field[] fields = c1.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Class type = f.getType();
            String name = f.getName();
            System.out.print(Modifier.toString(f.getModifiers()));
            System.out.println(" | " + type.getName() + "| " + name + ";");
        }
    }

    /**
     * 类型转换
     * @param type Object
     * @return int
     */
    private static int transType(Object type) {
        int typecode;
        typecode = 93;
        if (type.equals("java.lang.String")) {
            typecode = 93;
        }
        if (type.equals("double")) {
            typecode = 3;
        }
        if (type.equals("int")) {
            typecode = 5;
        }

        return typecode;
    }

    public boolean equals(Object otherobject) {
        if (this == otherobject) {
            return true;
        }

        if (otherobject == null) {
            return false;
        }

        if (getClass() != otherobject.getClass()) {
            return false;
        }

        Reflections other = (Reflections) otherobject;

        return true;
    }

    public String toString() {
        Class ref = this.getClass();
        return getClass().getName();
    }

    public Vector getVResult() {
        return mVResult;
    }

    public static void main(String[] args) {
        //Reflections aReflections = new Reflections();
        //Reflections.printFields(CourseSchema.class);
        Reflections.printConstructors(Schema.class);

    }
}
