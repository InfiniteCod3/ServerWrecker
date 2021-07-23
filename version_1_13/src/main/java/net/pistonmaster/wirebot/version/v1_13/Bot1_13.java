package net.pistonmaster.wirebot.version.v1_13;

import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import net.pistonmaster.wirebot.common.*;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.logging.Logger;

public class Bot1_13 extends AbstractBot {
    private final Options options;
    private final Proxy proxyInfo;
    private final Logger logger;
    private final IPacketWrapper account;

    private Session session;
    private final ServiceServer serviceServer;

    public Bot1_13(Options options, IPacketWrapper account, InetSocketAddress address, Logger log, ServiceServer serviceServer) {
        this.options = options;
        this.account = account;
        if (address == null) {
            this.proxyInfo = null;
        } else {
            this.proxyInfo = new Proxy(Proxy.Type.SOCKS, address);
        }

        this.serviceServer = serviceServer;

        this.logger = Logger.getLogger(account.getProfileName());
        this.logger.setParent(log);
    }

    public void connect(String host, int port) {
        Client client;
        if (proxyInfo == null) {
            client = new Client(host, port, (PacketProtocol) account, new TcpSessionFactory());
        } else {
            client = new Client(host, port, (PacketProtocol) account, new TcpSessionFactory(proxyInfo));
        }
        this.session = client.getSession();

        // SessionService sessionService = new SessionService();
        // sessionService.setBaseUri(serviceServer.getSession());
        // session.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService); // TODO

        SessionEventBus bus = new SessionEventBus(options, logger, this);

        session.addListener(new SessionListener1_13(bus));

        session.connect();
    }

    public void sendMessage(String message) {
        session.send(new ChatPacket1_13(message));
    }

    public boolean isOnline() {
        return session != null && session.isConnected();
    }

    public Session getSession() {
        return session;
    }

    public Logger getLogger() {
        return logger;
    }

    public Proxy getProxy() {
        return proxyInfo;
    }

    public void disconnect() {
        if (session != null) {
            session.disconnect("Disconnect");
        }
    }
}