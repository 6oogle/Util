package oogle.util.byteable;

import java.nio.charset.StandardCharsets;

public class FastDecoder implements Decoder{
    private final byte array[];
    private int i = 0;

    public FastDecoder(byte array[], int offset) {
        this.array = array;
        i = offset;
    }

    public FastDecoder(byte array[]) {
        this.array = array;
    }

    @Override
    public byte[] readRaw(byte[] array, int offset, int size) {
        System.arraycopy(this.array, this.i, array, offset, size);
        return array;
    }

    @Override
    public byte[] readBytes() {
        int read = readInt();
        byte array[] = new byte[read];
        System.arraycopy(this.array, this.i, array, 0, read);
        this.i = this.i + read;
        return array;
    }

    @Override
    public boolean readBoolean() {
        return array[i++] == 1;
    }

    @Override
    public byte readByte() {
        return (byte)(array[i++] & 0xFF);
    }

    @Override
    public short readShort() {
        return (short) (((array[i++] & 0xFF) << 8) | (array[i++] & 0xFF));
    }

    @Override
    public int readInt() {
        return ((array[i++] & 0xFF) << 24) | ((array[i++] & 0xFF) << 16) | ((array[i++] & 0xFF) << 8) | (array[i++] & 0xFF);
    }

    @Override
    public long readLong() {
        return (((long)array[i++] & 0xFF) << 56) | (((long)array[i++] & 0xFF) << 48) | (((long)array[i++] & 0xFF) << 40) |
               (((long)array[i++] & 0xFF) << 32) | (((long)array[i++] & 0xFF) << 24) | (((long)array[i++] & 0xFF) << 16) |
               (((long)array[i++] & 0xFF) << 8) | ((long)array[i++] & 0xFF);
    }

    @Override
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readString() {
        return new String(readBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public boolean hasNext() {
        return array.length - 1 == i;
    }

    public static <T> T wrap(byte[] array, Deserializer<T> deserializer) {
        return new FastDecoder(array).read(deserializer);
    }
}
