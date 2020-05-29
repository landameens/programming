package connectionWorker;

import connection.Connection;
import connection.exception.ConnectionException;
import connection.exception.NotYetConnectedException;
import manager.LogManager;
import message.Message;
import serializer.ISerializer;
import serializer.JavaSerializer;
import serializer.exception.DeserializationException;
import serializer.exception.SerializationException;
import сhunker.Chunker;
import сhunker.IChunker;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ConnectionWorker {
    private final Connection connection;

    private final ISerializer serializer;
    private final IChunker chunker;

    private final LogManager logManager;


    private ConnectionWorker(Connection connection,
                             ISerializer serializer,
                             IChunker chunker) {
        logManager = LogManager.createDefault(ConnectionWorker.class);

        this.connection = connection;

        this.serializer = serializer;
        this.chunker = chunker;
    }

    public boolean isConnected() {
        return connection.isConnected() && connection.isOpen();
    }

    public void connect() throws ConnectionException {
        connection.connect();
    }

    public static ConnectionWorker createDefault(Connection connection) {
        return createBasedOnJavaObjectStream(connection);
    }

    public static ConnectionWorker createBasedOnJavaObjectStream(Connection connection) {
        return new ConnectionWorker(connection,
                                    new JavaSerializer(),
                                    new Chunker(connection.getBufferSize()));
    }


    public void send(Message message) throws ConnectionException, SerializationException {
        if (!connection.isConnected() || !connection.isOpen()) {
            throw new NotYetConnectedException();
        }

        byte[] bytes = serializer.toByteArray(message);
        logManager.debug("Got bytes from Serializer, size: " + bytes.length);
        logManager.debug("Got bytes from Serializer: " + Arrays.toString(bytes));

        List<byte[]> chunks = chunker.split(bytes);
        logManager.debug("Got chunks form Chunker, list size: " + chunks.size());

        for (byte[] chunk : chunks) {
            logManager.debug("Chunk: " + Arrays.toString(chunk));
            connection.write(chunk);
        }

        logManager.info("SUCCESSFULLY sent message.");
    }

    public Message read() throws ConnectionException, DeserializationException {
        if (!connection.isConnected() || !connection.isOpen()) {
            throw new NotYetConnectedException();
        }

        List<byte[]> chunks = new ArrayList<>();

        byte[] chunk = connection.read();

        while (!Arrays.equals(chunk, chunker.getKeyWord())) {
            logManager.info((new String(chunk, StandardCharsets.UTF_8)).trim());
            logManager.debug("Chunk: " + Arrays.toString(chunk));
            chunks.add(chunk);
            chunk = connection.read();
        }

        logManager.debug("Got chunks from client, size: " + chunks.size());
        logManager.debug("First chuck: " + Arrays.toString(chunks.get(0)));

        byte[] bytes = chunker.join(chunks);
        logManager.debug((new String(bytes, StandardCharsets.UTF_8)).trim());
        logManager.debug("Join chunks, bytes size: " + bytes.length);

        Message message = serializer.fromByteArray(bytes, Message.class);
        logManager.info("SUCCESSFULLY read message.");

        return message;
    }
}
