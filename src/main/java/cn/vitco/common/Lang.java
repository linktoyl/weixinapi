package cn.vitco.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Lang 工具类
 *
 *
 * Created by Sterling on 2017/12/11.
 */
public class Lang {

    /**
     * 将抛出对象包裹成运行时异常，并增加自己的描述
     *
     * @param e
     *            抛出对象
     * @param fmt
     *            格式
     * @param args
     *            参数
     * @return 运行时异常
     */
    public static RuntimeException wrapThrow(Throwable e, String fmt, Object... args) {
        return new RuntimeException(String.format(fmt, args), e);
    }

    /**
     * 用运行时异常包裹抛出对象，如果抛出对象本身就是运行时异常，则直接返回。
     * <p>
     * 如果是 InvocationTargetException，那么将其剥离，只包裹其 TargetException
     *
     * @param e
     *            抛出对象
     * @return 运行时异常
     */
    public static RuntimeException wrapThrow(Throwable e) {
        if (e instanceof RuntimeException)
            return (RuntimeException) e;
        if (e instanceof InvocationTargetException)
            return wrapThrow(((InvocationTargetException) e).getTargetException());
        return new RuntimeException(e);
    }

    /**
     * 将一个集合转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param c
     *            分隔符
     * @param coll
     *            集合
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concat(Object c, Collection<T> coll) {
        StringBuilder sb = new StringBuilder();
        if (null == coll || coll.isEmpty())
            return sb;
        return concat(c, coll.iterator());
    }

    /**
     * 将一个迭代器转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     *
     * @param c
     *            分隔符
     * @param it
     *            集合
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder concat(Object c, Iterator<T> it) {
        StringBuilder sb = new StringBuilder();
        if (it == null || !it.hasNext())
            return sb;
        sb.append(it.next());
        while (it.hasNext())
            sb.append(c).append(it.next());
        return sb;
    }

    /**
     * 获取指定字符串的 SHA1 值
     *
     * @param cs
     *            字符串
     * @return 指定字符串的 SHA1 值
     * @see #digest(String, CharSequence)
     */
    public static String sha1(CharSequence cs) {
        return digest("SHA1", cs);
    }

    /**
     * 获取指定字符串的 MD5 值
     *
     * @param cs
     *            字符串
     * @return 指定字符串的 MD5 值
     * @see #digest(String, CharSequence)
     */
    public static String md5(CharSequence cs) {
        return digest("MD5", cs);
    }


    /**
     * 从字符串计算出数字签名
     *
     * @param algorithm
     *            算法，比如 "SHA1" 或者 "MD5" 等
     * @param cs
     *            字符串
     * @return 数字签名
     */
    public static String digest(String algorithm, CharSequence cs) {
        return digest(algorithm, Strings.getBytesUTF8(null == cs ? "" : cs), null, 1);
    }

    /**
     * 从字节数组计算出数字签名
     *
     * @param algorithm
     *            算法，比如 "SHA1" 或者 "MD5" 等
     * @param bytes
     *            字节数组
     * @param salt
     *            随机字节数组
     * @param iterations
     *            迭代次数
     * @return 数字签名
     */
    public static String digest(String algorithm, byte[] bytes, byte[] salt, int iterations) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                md.update(salt);
            }

            byte[] hashBytes = md.digest(bytes);

            for (int i = 1; i < iterations; i++) {
                md.reset();
                hashBytes = md.digest(hashBytes);
            }

            return fixedHexString(hashBytes);
        }
        catch (NoSuchAlgorithmException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 根据byte数组生成16进制字符串
     * @param hashBytes
     * @return
     */
    public static String fixedHexString(byte[] hashBytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    /**
     * POJO 转化为Map
     * @param obj
     * @return
     */
    public static Map<String, Object> objectToMap(Object obj){
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            if(obj == null){return null;}
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields){
                field.setAccessible(true);
                if(field.get(obj) instanceof String){
                    if(field.get(obj)!=null&&!field.get(obj).equals("")){
                        map.put(field.getName(), field.get(obj));
                    }
                }else if(field.get(obj) instanceof Long){
                    if(field.get(obj)!=null){
                        map.put(field.getName(), String.valueOf(field.get(obj)));
                    }
                }else if(field.get(obj) instanceof Integer){
                    if(field.get(obj)!=null){
                        map.put(field.getName(), String.valueOf(field.get(obj)));
                    }
                }else if(field.get(obj) instanceof Date){
                    if(field.get(obj)!=null){
                        map.put(field.getName(), (Date)field.get(obj));
                    }
                }else if(field.get(obj) instanceof Double){
                    if(field.get(obj)!=null){
                        map.put(field.getName(), String.valueOf(field.get(obj)));
                    }
                }else if(field.get(obj) instanceof Float){
                    if(field.get(obj)!=null){
                        map.put(field.getName(), String.valueOf(field.get(obj)));
                    }
                }else if(field.get(obj) instanceof BigInteger){
                    if(field.get(obj)!=null){
                        map.put(field.getName(), String.valueOf(field.get(obj)));
                    }
                }else if(field.get(obj) instanceof Short){
                    if(field.get(obj)!=null){
                        map.put(field.getName(), String.valueOf(field.get(obj)));
                    }
                }else if(field.get(obj) instanceof BigInteger){
                    if(field.get(obj)!=null){
                        map.put(field.getName(), String.valueOf(field.get(obj)));
                    }
                }else{
                    if(field.get(obj)!=null){
                        map.put(field.getName(), field.get(obj));
                    }
                }

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return map;
    }


    /**
     *
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch (Throwable e) {
            return Class.forName(className);
        }
    }

}
