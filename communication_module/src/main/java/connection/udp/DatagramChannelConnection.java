package connection.udp;

import connection.ChannelConnection;
import connection.exception.ConnectionException;
import connection.exception.ReadingException;
import connection.exception.WritingException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class DatagramChannelConnection extends ChannelConnection {
    /**
     * Specify address to connect and bufferSize to bytes to be read by read method.
     */
    public DatagramChannelConnection(String address, int port, int bufferSize) throws ConnectionException {
        super(address, port, bufferSize);
        try {
            channel = DatagramChannel.open();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }

    public DatagramChannelConnection(DatagramChannel datagramChannel, int bufferSize) {
        super(datagramChannel, bufferSize);
    }


    @Override
    public void connect() throws ConnectionException {
        try {
            ((DatagramChannel) channel).connect(new InetSocketAddress(address, port));
        } catch (IOException e) {
            try {
                channel = DatagramChannel.open();
                ((DatagramChannel) channel).connect(new InetSocketAddress(address, port));
            } catch (IOException ex) {
                throw new ConnectionException(ex);
            }
        }
    }

    @Override
    public void write(byte[] bytes) throws WritingException {
        DatagramChannel socketChannel = (DatagramChannel) channel;
        try {
            socketChannel.write(ByteBuffer.wrap(bytes));
        } catch (IOException | NotYetConnectedException | NonWritableChannelException e) {
            try {
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            throw new WritingException(e);
        }
    }

    @Override
    public byte[] read() throws ReadingException {
        DatagramChannel socketChannel = (DatagramChannel) channel;
        int quantityOfReadBytes;
        try {
            quantityOfReadBytes = socketChannel.read(outBuffer);
        } catch (IOException | NotYetConnectedException | NonReadableChannelException e) {
            try {
                channel.close();
                outBuffer = ByteBuffer.allocate(bufferSize);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            throw new ReadingException(e);
        }

        if (quantityOfReadBytes == -1) {
            try {
                channel.close();
                outBuffer = ByteBuffer.allocate(bufferSize);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            throw new ReadingException("Reached end-of-stream of channel: ");
        }

        byte[] bytes = new byte[bufferSize];
        outBuffer.rewind();
        outBuffer.get(bytes, 0, quantityOfReadBytes);
        outBuffer.clear();

        return bytes;
    }

    @Override
    public boolean isConnected() {
        return ((DatagramChannel) channel).isConnected();
    }
}
