package cn.vitco.common;

import java.io.*;

/**
 * 流 工具类
 *
 * Created by Sterling on 2017/12/11.
 */
public abstract class Streams {
    private static final int BUF_SIZE = 8192;

    /**
     * 判断两个输入流是否严格相等
     */
    public static boolean equals(InputStream sA, InputStream sB) throws IOException {
        int dA;
        while ((dA = sA.read()) != -1) {
            int dB = sB.read();
            if (dA != dB)
                return false;
        }
        return sB.read() == -1;
    }

    /**
     * 将一段文本全部写入一个writer。
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param writer
     *
     * @param cs
     *            文本
     * @throws IOException
     */
    public static void write(Writer writer, CharSequence cs) throws IOException {
        if (null != cs && null != writer) {
            writer.write(cs.toString());
            writer.flush();
        }
    }

    /**
     * 将一段文本全部写入一个writer。
     * <p>
     * <b style=color:red>注意</b>，它会关闭输出流
     *
     * @param writer
     *            输出流
     * @param cs
     *            文本
     */
    public static void writeAndClose(Writer writer, CharSequence cs) {
        try {
            write(writer, cs);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            safeClose(writer);
        }
    }

    /**
     * 将输入流写入一个输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输入/出流
     *
     * @param ops
     *            输出流
     * @param ins
     *            输入流
     *
     * @return 写入的字节数
     * @throws IOException
     */
    public static long write(OutputStream ops, InputStream ins) throws IOException {
        return write(ops, ins, BUF_SIZE);
    }

    /**
     * 将输入流写入一个输出流。
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输入/出流
     *
     * @param ops
     *            输出流
     * @param ins
     *            输入流
     * @param bufferSize
     *            缓冲块大小
     *
     * @return 写入的字节数
     *
     * @throws IOException
     */
    public static long write(OutputStream ops, InputStream ins, int bufferSize) throws IOException {
        if (null == ops || null == ins)
            return 0;

        byte[] buf = new byte[bufferSize];
        int len;
        long bytesCount = 0;
        while (-1 != (len = ins.read(buf))) {
            bytesCount += len;
            ops.write(buf, 0, len);
        }
        // 啥都没写，强制触发一下写
        // 这是考虑到 walnut 的输出流实现，比如你写一个空文件
        // 那么输入流就是空的，但是 walnut 的包裹输出流并不知道你写过了
        // 它人你就是打开一个输出流，然后再关上，所以自然不会对内容做改动
        // 所以这里触发一个写，它就知道，喔你要写个空喔。
        if (0 == bytesCount) {
            ops.write(buf, 0, 0);
        }
        ops.flush();
        return bytesCount;
    }

    /**
     * 将输入流写入一个输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它会关闭输入/出流
     *
     * @param ops
     *            输出流
     * @param ins
     *            输入流
     * @return 写入的字节数
     */
    public static long writeAndClose(OutputStream ops, InputStream ins) {
        try {
            return write(ops, ins);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            safeClose(ops);
            safeClose(ins);
        }
    }

    /**
     * 将文本输入流写入一个文本输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输入/出流
     *
     * @param writer
     *            输出流
     * @param reader
     *            输入流
     * @throws IOException
     */
    public static long write(Writer writer, Reader reader) throws IOException {
        if (null == writer || null == reader)
            return 0;

        char[] cbuf = new char[BUF_SIZE];
        int len, count = 0;
        while (true) {
            len = reader.read(cbuf);
            if (len == -1)
                break;
            writer.write(cbuf, 0, len);
            count += len;
        }
        return count;
    }

    /**
     * 将文本输入流写入一个文本输出流。块大小为 8192
     * <p>
     * <b style=color:red>注意</b>，它会关闭输入/出流
     *
     * @param writer
     *            输出流
     * @param reader
     *            输入流
     */
    public static long writeAndClose(Writer writer, Reader reader) {
        try {
            return write(writer, reader);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            safeClose(writer);
            safeClose(reader);
        }
    }

    /**
     * 将一个字节数组写入一个输出流。
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param ops
     *            输出流
     * @param bytes
     *            字节数组
     * @throws IOException
     */
    public static void write(OutputStream ops, byte[] bytes) throws IOException {
        if (null == ops || null == bytes || bytes.length == 0)
            return;
        ops.write(bytes);
    }

    /**
     * 将一个字节数组写入一个输出流。
     * <p>
     * <b style=color:red>注意</b>，它会关闭输出流
     *
     * @param ops
     *            输出流
     * @param bytes
     *            字节数组
     */
    public static void writeAndClose(OutputStream ops, byte[] bytes) {
        try {
            write(ops, bytes);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            safeClose(ops);
        }
    }

    /**
     * 从一个文本流中读取全部内容并返回
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param reader
     *            文本输出流
     * @return 文本内容
     * @throws IOException
     */
    public static StringBuilder read(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        read(reader, sb);
        return sb;
    }

    /**
     * 从一个文本流中读取全部内容并返回
     * <p>
     * <b style=color:red>注意</b>，它会关闭输入流
     *
     * @param reader
     *            文本输入流
     * @return 文本内容
     * @throws IOException
     */
    public static String readAndClose(Reader reader) {
        try {
            return read(reader).toString();
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            safeClose(reader);
        }
    }

    /**
     * 从一个文本流中读取全部内容并写入缓冲
     * <p>
     * <b style=color:red>注意</b>，它并不会关闭输出流
     *
     * @param reader
     *            文本输出流
     * @param sb
     *            输出的文本缓冲
     * @return 读取的字符数量
     * @throws IOException
     */
    public static int read(Reader reader, StringBuilder sb) throws IOException {
        char[] cbuf = new char[BUF_SIZE];
        int count = 0;
        int len;
        while (-1 != (len = reader.read(cbuf))) {
            sb.append(cbuf, 0, len);
            count += len;
        }
        return count;
    }

    /**
     * 从一个文本流中读取全部内容并写入缓冲
     * <p>
     * <b style=color:red>注意</b>，它会关闭输出流
     *
     * @param reader
     *            文本输出流
     * @param sb
     *            输出的文本缓冲
     * @return 读取的字符数量
     */
    public static int readAndClose(InputStreamReader reader, StringBuilder sb) {
        try {
            return read(reader, sb);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            safeClose(reader);
        }
    }

    /**
     * 读取一个输入流中所有的字节
     *
     * @param ins
     *            输入流，必须支持 available()
     * @return 一个字节数组
     * @throws IOException
     */
    public static byte[] readBytes(InputStream ins) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        write(out, ins);
        return out.toByteArray();
    }

    /**
     * 读取一个输入流中所有的字节，并关闭输入流
     *
     * @param ins
     *            输入流，必须支持 available()
     * @return 一个字节数组
     * @throws IOException
     */
    public static byte[] readBytesAndClose(InputStream ins) {
        byte[] bytes = null;
        try {
            bytes = readBytes(ins);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            Streams.safeClose(ins);
        }
        return bytes;
    }

    /**
     * 关闭一个可关闭对象，可以接受 null。如果成功关闭，返回 true，发生异常 返回 false
     *
     * @param cb
     *            可关闭对象
     * @return 是否成功关闭
     */
    public static boolean safeClose(Closeable cb) {
        if (null != cb)
            try {
                cb.close();
            }
            catch (IOException e) {
                return false;
            }
        return true;
    }

    /**
     * 安全刷新一个可刷新的对象，可接受 null
     *
     * @param fa
     *            可刷新对象
     */
    public static void safeFlush(Flushable fa) {
        if (null != fa)
            try {
                fa.flush();
            }
            catch (IOException e) {}
    }

    /**
     * 为一个输入流包裹一个缓冲流。如果这个输入流本身就是缓冲流，则直接返回
     *
     * @param ins
     *            输入流。
     * @return 缓冲输入流
     */
    public static BufferedInputStream buff(InputStream ins) {
        if (ins == null)
            throw new NullPointerException("ins is null!");
        if (ins instanceof BufferedInputStream)
            return (BufferedInputStream) ins;
        // BufferedInputStream的构造方法,竟然是允许null参数的!! 我&$#^$&%
        return new BufferedInputStream(ins);
    }

    /**
     * 为一个输出流包裹一个缓冲流。如果这个输出流本身就是缓冲流，则直接返回
     *
     * @param ops
     *            输出流。
     * @return 缓冲输出流
     */
    public static BufferedOutputStream buff(OutputStream ops) {
        if (ops == null)
            throw new NullPointerException("ops is null!");
        if (ops instanceof BufferedOutputStream)
            return (BufferedOutputStream) ops;
        return new BufferedOutputStream(ops);
    }

    /**
     * 为一个文本输入流包裹一个缓冲流。如果这个输入流本身就是缓冲流，则直接返回
     *
     * @param reader
     *            文本输入流。
     * @return 缓冲文本输入流
     */
    public static BufferedReader buffr(Reader reader) {
        if (reader instanceof BufferedReader)
            return (BufferedReader) reader;
        return new BufferedReader(reader);
    }

    /**
     * 为一个文本输出流包裹一个缓冲流。如果这个文本输出流本身就是缓冲流，则直接返回
     *
     * @param ops
     *            文本输出流。
     * @return 缓冲文本输出流
     */
    public static BufferedWriter buffw(Writer ops) {
        if (ops instanceof BufferedWriter)
            return (BufferedWriter) ops;
        return new BufferedWriter(ops);
    }




    private static final byte[] UTF_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    /**
     * 判断并移除UTF-8的BOM头
     */
    public static InputStream utf8filte(InputStream in) {
        try {
            if (in.available() == -1)
                return in;
            PushbackInputStream pis = new PushbackInputStream(in, 3);
            byte[] header = new byte[3];
            int len = pis.read(header, 0, 3);
            if (len < 1)
                return in;
            if (header[0] != UTF_BOM[0] || header[1] != UTF_BOM[1] || header[2] != UTF_BOM[2]) {
                pis.unread(header, 0, len);
            }
            return pis;
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }



    /**
     * 根据一个文件建立一个输出流
     *
     * @param file
     *            文件
     * @return 输出流
     */
    public static OutputStream fileOut(File file) {
        try {
            return buff(new FileOutputStream(file));
        }
        catch (FileNotFoundException e) {
            throw Lang.wrapThrow(e);
        }
    }



    /**
     * 根据一个文件建立一个 UTF-8 文本输出流
     *
     * @param file
     *            文件
     * @return 输出流
     */
    public static Writer fileOutw(File file) {
        return utf8w(fileOut(file));
    }

    public static Reader utf8r(InputStream is) {
        return new InputStreamReader(utf8filte(is), Encoding.CHARSET_UTF8);
    }

    public static Writer utf8w(OutputStream os) {
        return new OutputStreamWriter(os, Encoding.CHARSET_UTF8);
    }



    public static InputStream wrap(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }


    public static void appendWriteAndClose(File f, String text) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
            fw.write(text);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            safeClose(fw);
        }

    }

    public static String nextLineTrim(BufferedReader br) throws IOException {
        String line = null;
        while (br.ready()) {
            line = br.readLine();
            if (line == null)
                break;
            if (Strings.isBlank(line))
                continue;
            return line.trim();
        }
        return line;
    }

    public static long writeAndClose(OutputStream ops, InputStream ins, int buf) {
        try {
            return write(ops, ins, buf);
        }
        catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
        finally {
            safeClose(ops);
            safeClose(ins);
        }
    }
}