package connection.udp;

import connection.Connection;
import connection.exception.ConnectionException;
import connection.exception.ReadingException;
import connection.exception.WritingException;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;

public class SocketConnection extends Connection {
    private DatagramSocket datagramSocket;


    public SocketConnection(String address,
                            int port,
                            int bufferSize) throws ConnectionException {
        super(address, port, bufferSize);

        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new ConnectionException(e);
        }
    }

    public SocketConnection(DatagramSocket datagramSocket,
                            int bufferSize) {
        super(bufferSize);
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void connect() throws ConnectionException {
        try {
            datagramSocket.connect(new InetSocketAddress(address, port));
            datagramSocket.setSoTimeout(5000);
        } catch (IOException e) {
            try {
                datagramSocket = new DatagramSocket();
                datagramSocket.connect(new InetSocketAddress(address, port));
            } catch (IOException ex) {
                throw new ConnectionException(ex);
            }
        }
    }

    @Override
    public void write(byte[] bytes) throws WritingException {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, new InetSocketAddress(address, port));
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            datagramSocket.close();
            throw new WritingException(e);
        }
    }

    @Override
    public byte[] read() throws ReadingException {
        DatagramPacket datagramPacket = null;
        try {
            datagramPacket = new DatagramPacket(outBuffer.array(), outBuffer.array().length);
            datagramSocket.receive(datagramPacket);
        } catch (IOException e) {
            datagramSocket.close();
            outBuffer = ByteBuffer.allocate(bufferSize);
            throw new ReadingException(e);
        }

        byte[] bytes = new byte[bufferSize];
        outBuffer.rewind();
        outBuffer.get(bytes, 0, datagramPacket.getLength());
        outBuffer.clear();

        return bytes;
    }

    @Override
    public void close() throws ConnectionException {
        datagramSocket.close();
    }

    @Override
    public boolean isConnected() {
        return isOpen() && datagramSocket.isConnected();
    }

    @Override
    public boolean isOpen() {
        return !datagramSocket.isClosed();
    }
}
