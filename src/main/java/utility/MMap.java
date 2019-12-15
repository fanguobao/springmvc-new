package utility;

import java.util.HashMap;
import java.util.Set;

public class MMap
{
    /** 数据的容器 Map Vector */
    private HashMap mapV = null;

    /** 排序的容器 Map Order */
    private HashMap mapO = null;

    /**
     * 构造函数
     */
    public MMap()
    {
        mapV = new HashMap();
        mapO = new HashMap();
    }

    /**
     * 建立键－值对，序号从1开始
     * @param key Object
     * @param value Object
     */
    public void put(Object key, Object value)
    {
        if (key == null || value == null)
            return;
        mapV.put(key, value);
        mapO.put(String.valueOf(mapV.size()), key);
    }

    /**
     * 获取键－值Set
     * @return Set
     */
    public Set keySet()
    {
        return mapV.keySet();
    }

    /**
     * 根据键获取值
     * @param key Object
     * @return Object
     */
    public Object get(Object key)
    {
        return mapV.get(key);
    }

    /**
     * 获取排序Map
     * @return HashMap
     */
    public HashMap getOrder()
    {
        return mapO;
    }

    /**
     * 通过序号获取键，序号即插入顺序，从1开始
     * @param order String
     * @return Object
     */
    public Object getKeyByOrder(String order)
    {
        return mapO.get(order);
    }

    /**
     * 添加一个MMap
     * @param srcMap MMap
     */
    public void add(MMap srcMap)
    {
        if (srcMap == null)
            return;
        for (int i = 0; i < srcMap.keySet().size(); i++)
        {
            Object key = srcMap.getKeyByOrder(String.valueOf(i + 1));
            this.put(key, srcMap.get(key));
        }
    }

    public static void main(String[] args)
    {
        MMap amap = new MMap();
        amap.put("key1", "value1");
        amap.put("key2", "value2");
        MMap bmap = new MMap();
        bmap.put("keyb1", "valueb1");
        bmap.put("keyb2", "valueb2");
        amap.add(bmap);
        for (int i = 0; i < amap.keySet().size(); i++)
        {
            Object key = amap.getKeyByOrder(String.valueOf(i + 1));
            System.out.println(amap.get(key).toString());
        }
    }
}
