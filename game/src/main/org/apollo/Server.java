package org.apollo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.cache.IndexedFileSystem;
import org.apollo.game.model.World;
import org.apollo.game.plugin.PluginContext;
import org.apollo.game.plugin.PluginManager;
import org.apollo.game.release.r317.Release317;
import org.apollo.game.session.ApolloHandler;
import org.apollo.net.HttpChannelInitializer;
import org.apollo.net.JagGrabChannelInitializer;
import org.apollo.net.NetworkConstants;
import org.apollo.net.ServiceChannelInitializer;
import org.apollo.net.release.Release;

import com.google.common.base.Stopwatch;

/**
 * The core class of the Apollo server.
 *
 * @author Graham
 */
public final class Server {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	/**
	 * The entry point of the Apollo server application.
	 *
	 * @param args The command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		Stopwatch stopwatch = Stopwatch.createStarted();

		try {
			Server server = new Server();
			server.init(args.length == 1 ? args[0] : Release317.class.getName());

			SocketAddress service = new InetSocketAddress(NetworkConstants.SERVICE_PORT);
			SocketAddress http = new InetSocketAddress(NetworkConstants.HTTP_PORT);
			SocketAddress jaggrab = new InetSocketAddress(NetworkConstants.JAGGRAB_PORT);

			server.bind(service, http, jaggrab);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error whilst starting server.", t);
		}

		logger.fine("Starting apollo took " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
	}

	/**
	 * The {@link ServerBootstrap} for the HTTP listener.
	 */
	private final ServerBootstrap httpBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the JAGGRAB listener.
	 */
	private final ServerBootstrap jaggrabBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the service listener.
	 */
	private final ServerBootstrap serviceBootstrap = new ServerBootstrap();

	/**
	 * The event loop group.
	 */
	private final EventLoopGroup loopGroup = new NioEventLoopGroup();

	/**
	 * Creates the Apollo server.
	 */
	public Server() {
		logger.info("Starting Apollo...");
	}

	/**
	 * Binds the server to the specified address.
	 *
	 * @param service The service address to bind to.
	 * @param http The HTTP address to bind to.
	 * @param jaggrab The JAGGRAB address to bind to.
	 */
	public void bind(SocketAddress service, SocketAddress http, SocketAddress jaggrab) {
		try {
			logger.fine("Binding service listener to address: " + service + "...");
			serviceBootstrap.bind(service).sync();

			logger.fine("Binding HTTP listener to address: " + http + "...");
			httpBootstrap.bind(http).sync();

			logger.fine("Binding JAGGRAB listener to address: " + jaggrab + "...");
			jaggrabBootstrap.bind(jaggrab).sync();
		} catch (InterruptedException exception) {
			logger.log(Level.SEVERE, "Binding to a port failed: ensure apollo isn't already running.", exception);
			System.exit(1);
		}

		logger.info("Ready for connections.");
	}

	/**
	 * Initialises the server.
	 *
	 * @param releaseName The class name of the current active {@link Release}.
	 * @throws Exception If an error occurs.
	 */
	public void init(String releaseName) throws Exception {
		Class<?> clazz = Class.forName(releaseName);
		Release release = (Release) clazz.newInstance();
		int version = release.getReleaseNumber();

		logger.info("Initialized " + release + ".");

		serviceBootstrap.group(loopGroup);
		httpBootstrap.group(loopGroup);
		jaggrabBootstrap.group(loopGroup);

		World world = new World();
		ServiceManager services = new ServiceManager(world);
		IndexedFileSystem fs = new IndexedFileSystem(Paths.get("data/fs", Integer.toString(version)), true);
		ServerContext context = new ServerContext(release, services, fs);
		ApolloHandler handler = new ApolloHandler(context);

		ChannelInitializer<SocketChannel> service = new ServiceChannelInitializer(handler);
		serviceBootstrap.channel(NioServerSocketChannel.class);
		serviceBootstrap.childHandler(service);

		ChannelInitializer<SocketChannel> http = new HttpChannelInitializer(handler);
		httpBootstrap.channel(NioServerSocketChannel.class);
		httpBootstrap.childHandler(http);

		ChannelInitializer<SocketChannel> jaggrab = new JagGrabChannelInitializer(handler);
		jaggrabBootstrap.channel(NioServerSocketChannel.class);
		jaggrabBootstrap.childHandler(jaggrab);

		PluginManager manager = new PluginManager(world, new PluginContext(context));
		services.startAll();

		world.init(version, fs, manager);
	}

}