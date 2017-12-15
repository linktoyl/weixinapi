package cn.vitco.wx.util;

import org.apache.commons.beanutils.ConvertUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * 微信属性对象实体类
 *
 * 主要形式为 键值对
 *
 * Created by Sterling on 2017/12/11.
 */
public class WxMap extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = -330721950926030819L;

    public static WxMap WRAP(Map<String, Object> map) {
        if (null == map)
            return null;
        if (map instanceof WxMap)
            return (WxMap) map;
        return new WxMap(map);
    }

    public WxMap() {
        super();
    }

    public WxMap(Map<String, Object> map) {
        super();
        this.putAll(map);
    }


    public WxMap(String key, Object value) {
        super();
        put(key, value);
    }

    /**
     * 设置一个字段，如果值为 null 则表示移除
     *
     * @param key
     *            键
     * @param v
     *            值
     */
    public void setOrRemove(String key, Object v) {
        if (null == v) {
            this.remove(key);
        } else {
            this.put(key, v);
        }
    }

    public static WxMap NEW() {
        return new WxMap();
    }


    public boolean has(String key) {
        return null != get(key);
    }

    public boolean is(String key, Object val) {
        Object obj = this.get(key);
        if (null == obj && null == val)
            return true;
        if (null == obj || null == val)
            return false;
        return obj.equals(val);
    }

    public WxMap duplicate() {
        WxMap map = new WxMap();
        map.putAll(this);
        return map;
    }


    /**
     * 从 Map 里挑选一些键生成一个新的 Map，自己同时删除这些键
     *
     * @param keys
     *            键
     * @return 新 Map
     */
    public WxMap pickAndRemove(String... keys) {
        if (keys.length == 0)
            return new WxMap();
        WxMap re = new WxMap();
        for (String key : keys) {
            Object val = this.remove(key);
            re.put(key, val);
        }
        return re;
    }



    /**
     * 如果一个键的值无效（has(key) 返回 false)，那么为其设置默认值
     *
     * @param key
     *            键
     * @param dft
     *            值
     * @return 自身以便链式赋值
     */
    public WxMap putDefault(String key, Object dft) {
        if (!this.has(key)) {
            this.put(key, dft);
        }
        return this;
    }

    @Override
    public boolean containsValue(Object value) {
        if (null == _map)
            return super.containsValue(value);
        return super.containsValue(value) || _map.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        if (null == _map)
            return super.containsKey(key);
        return super.containsKey(key) || _map.containsKey(key);
    }

    public Set<String> keySet() {
        if (null == _map)
            return super.keySet();
        HashSet<String> keys = new HashSet<String>();
        keys.addAll(super.keySet());
        keys.addAll(_map.keySet());
        return keys;
    }

    public Collection<Object> values() {
        if (null == _map)
            return super.values();
        List<Object> vals = new LinkedList<Object>();
        for (String key : this.keySet()) {
            vals.add(this.get(key));
        }
        return vals;
    }

    public Set<Entry<String, Object>> entrySet() {
        if (null == _map)
            return super.entrySet();
        HashSet<Entry<String, Object>> vals = new HashSet<Entry<String, Object>>();
        vals.addAll(_map.entrySet());
        vals.addAll(super.entrySet());
        return vals;
    }

    public void clear() {
        super.clear();
        if (null != _map)
            _map.clear();
    }

    private WxMap _map;

    public WxMap attach(WxMap map) {
        _map = map;
        return this;
    }

    public WxMap detach() {
        WxMap re = _map;
        _map = null;
        return re;
    }

    @Override
    public Object get(Object key) {
        if (_map == null)
            return super.get(key);

        if (super.containsKey(key)) {
            return super.get(key);
        }

        return _map.get(key);
    }

    public Object get(String key, Object dft) {
        Object v = get(key);
        return null == v ? dft : v;
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int dft) {
        Object v = get(key);
        return null == v ? dft : (Integer)ConvertUtils.convert(v, int.class);
    }

    public float getFloat(String key) {
        return getFloat(key, Float.NaN);
    }

    public float getFloat(String key, float dft) {
        Object v = get(key);
        return null == v ? dft : (Float)ConvertUtils.convert(v, float.class);
    }

    public long getLong(String key) {
        return getLong(key, -1);
    }

    public long getLong(String key, long dft) {
        Object v = get(key);
        return null == v ? dft : (Long)ConvertUtils.convert(v, long.class);
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    public double getDouble(String key, double dft) {
        Object v = get(key);
        return null == v ? dft : (Double)ConvertUtils.convert(v, double.class);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean dft) {
        Object v = get(key);
        return null == v ? dft : (Boolean)ConvertUtils.convert(v, boolean.class);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    @SuppressWarnings("rawtypes")
    public String getString(String key, String dft) {
        Object v = get(key);
        if (v == null)
            return dft;
        if (v instanceof CharSequence)
            return v.toString();
        if (v instanceof List) {
            v = ((List) v).iterator().next();
        }
        // by wendal : 这还有必要castTo么?
        // zozoh: 当然有啦，比如日期对象，要变成字符串的话 ...
        return (String)ConvertUtils.convert(v, String.class);
    }

    public Date getTime(String key) {
        return getTime(key, null);
    }

    public Date getTime(String key, Date dft) {
        Object v = get(key);
        return null == v ? dft : (Date)ConvertUtils.convert(v, Date.class);
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> classOfEnum) {
        String s = getString(key);
        if (s.isEmpty())
            return null;
        return Enum.valueOf(classOfEnum, s);
    }

    @SuppressWarnings("unchecked")
    public boolean isEnum(String key, Enum<?>... eus) {
        if (null == eus || eus.length == 0)
            return false;
        try {
            Enum<?> v = getEnum(key, eus[0].getClass());
            for (Enum<?> eu : eus)
                if (!v.equals(eu))
                    return false;
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public <T> T getAs(String key, Class<T> classOfT) {
        return getAs(key, classOfT, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAs(String key, Class<T> classOfT, T dft) {
        Object v = get(key);
        return null == v ? dft : (T) ConvertUtils.convert(v, classOfT);
    }



    /**
     * 为 Map 增加一个名值对。如果同名已经有值了，那么会将两个值合并成一个列表
     *
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public WxMap addv(String key, Object value) {
        Object obj = get(key);
        if (null == obj) {
            put(key, value);
        } else if (obj instanceof List<?>)
            ((List<Object>) obj).add(value);
        else {
            List<Object> list = new LinkedList<Object>();
            list.add(obj);
            list.add(value);
            put(key, list);
        }
        return this;
    }

    /**
     * 向某个键增加一组值，如果原来就有值，是集合的话，会被合并，否则原来的值用列表包裹后再加入新值
     *
     * @param key
     *            键
     * @param values
     *            值列表
     * @return 自身
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> WxMap pushTo(String key, T... values) {
        if (null != values && values.length > 0) {
            Object v = get(key);
            // 不存在的话，增加列表
            if (null == v) {
                List<Object> list = new LinkedList<Object>();
                for (Object val : values)
                    list.add(val);
                this.put(key, list);
            }
            // 如果是集合的话，就增加
            else if (v instanceof Collection) {
                for (Object val : values)
                    ((Collection) v).add(val);
            }
            // 否则将原来的值变成列表再增加
            else {
                List<Object> list = new LinkedList<Object>();
                list.add(v);
                for (Object val : values)
                    list.add(val);
                this.put(key, list);
            }
        }
        // 返回自身以便链式赋值
        return this;
    }

    /**
     * 是 pushTo 函数的另一个变种（可以接受集合）
     *
     * @param key
     *            键
     * @param values
     *            值列表
     * @return 自身
     *
     * @see #pushTo(String, Collection)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public WxMap pushTo(String key, Collection<?> values) {
        if (null != values && values.size() > 0) {
            Object v = get(key);
            // 不存在的话，增加列表
            if (null == v) {
                List<Object> list = new LinkedList<Object>();
                list.addAll(values);
                this.put(key, list);
            }
            // 如果是集合的话，就增加
            else if (v instanceof Collection) {
                ((Collection) v).addAll(values);
            }
            // 否则将原来的值变成列表再增加
            else {
                List<Object> list = new LinkedList<Object>();
                list.add(v);
                list.addAll(values);
                this.put(key, list);
            }
        }
        // 返回自身以便链式赋值
        return this;
    }

    /**
     * @deprecated 本函数意义容易发生混淆，已经改名成 addv，下个版将被删除
     * @since 1.b.51
     */
    public WxMap putv(String key, Object value) {
        return addv(key, value);
    }

    public WxMap setv(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public void unset(String key) {
        this.remove(key);
    }



    public WxMap setMap(Map<?, ?> map, boolean ignoreNullValue) {
        for (Map.Entry<?, ?> en : map.entrySet()) {
            Object key = en.getKey();
            Object val = en.getValue();

            if (null == key)
                continue;

            if (null == val && ignoreNullValue)
                continue;

            this.put(key.toString(), val);
        }
        return this;
    }

    /**
     * 相当于 mergeWith(map, false)
     *
     * @see #mergeWith(Map, boolean)
     */
    public WxMap mergeWith(Map<String, Object> map) {
        return this.mergeWith(map, false);
    }

    /**
     * 与一个给定的 Map 融合，如果有子 Map 递归
     *
     * @param map
     *            要合并进来的 Map
     * @param onlyAbsent
     *            true 表示只有没有 key 才设置值
     * @return 自身以便链式赋值
     */
    @SuppressWarnings("unchecked")
    public WxMap mergeWith(Map<String, Object> map, boolean onlyAbsent) {
        for (Map.Entry<String, Object> en : map.entrySet()) {
            String key = en.getKey();
            Object val = en.getValue();

            if (null == key || null == val)
                continue;

            Object myVal = this.get(key);

            // 如果两边都是 Map ，则融合
            if (null != myVal && myVal instanceof Map && val instanceof Map) {
                Map<String, Object> m0 = (Map<String, Object>) myVal;
                Map<String, Object> m1 = (Map<String, Object>) val;
                WxMap m2 = WxMap.WRAP(m0).mergeWith(m1, onlyAbsent);
                // 搞出了新 Map，设置一下
                if (m2 != m0)
                    this.put(key, m2);
            }
            // 只有没有的时候才设置
            else if (onlyAbsent) {
                this.setnx(key, val);
            }
            // 否则直接替换
            else {
                this.put(key, val);
            }
        }

        return this;
    }

    /**
     * 与JDK8+的 putIfAbsent(key, val)一致, 当且仅当值不存在时设置进去,但与putIfAbsent返回值有不一样
     *
     * @param key
     *            键
     * @param val
     *            值
     * @return 当前的WxMap实例
     */
    public WxMap setnx(String key, Object val) {
        if (!containsKey(key))
            setv(key, val);
        return this;
    }

    /**
     * 将一个集合与自己补充（相当于针对每个 key 调用 setnx)
     *
     * @param map
     *            集合
     * @return 自身
     *
     * @see #setnx(String, Object)
     */
    public WxMap setnxAll(Map<String, Object> map) {
        if (null != map && map.size() > 0) {
            for (Map.Entry<String, Object> en : map.entrySet()) {
                this.setnx(en.getKey(), en.getValue());
            }
        }
        return this;
    }
}
