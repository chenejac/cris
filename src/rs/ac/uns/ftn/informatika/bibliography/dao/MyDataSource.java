package rs.ac.uns.ftn.informatika.bibliography.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Basic implementation of <code>javax.sql.DataSource</code> that is
 * configured via JavaBeans properties.
 * 
 * @author Joel Rosi-Schwartz (email: Joel.Rosi-Schwartz@Etish.org)
 * 
 * @version $Id: MckoidbDataSource.java,v 1.3 2004/06/27 10:22:01 joel Exp $
 */
public class MyDataSource implements DataSource {
	static {
		
		// Register the Mckoi JDBC Driver
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			System.err.println("Unable to register the JDBC Driver. "
					+ "Make sure the mysql JDBC driver is in the classpath.");

			throw new RuntimeException("Unable to register the JDBC Driver.");
		}
	}

	/**
	 * 
	 */
	public MyDataSource() {
		super();
		ResourceBundle rb = PropertyResourceBundle.getBundle("rs.ac.uns.ftn.informatika.bibliography.backup.connection");
		this.hostname = rb.getString("hostname");
		this.port = rb.getString("port");
		this.schema = rb.getString("schema") +  rb.getString("connectionParameters");
		this.username = rb.getString("username");
		this.password = rb.getString("password");
	}
	
	/**
	 * 
	 */
	public MyDataSource(String hostname, String port, String schema, String connectionParameters, String username, String password) {
		super();
		this.hostname = hostname;
		this.port = port;
		this.schema = schema +  connectionParameters;
		this.username = username;
		this.password = password;
	}

	/**
	 * The schema to use, if any, when building up the database url connection string.
	 */
	private String hostname = null;

	/**
	 * The login timeout (in seconds) for connecting to the database.
	 */
	private int loginTimeout = 0;

	/**
	 * The PrintWriter to which log messages should be directed.
	 */
	protected PrintWriter logWriter = new PrintWriter(System.err);

	/**
	 * The password to use for the database connection.
	 */
	private String password = null;

	/**
	 * The port to use, if any, when building up the database url connection string.
	 */
	private String port;

	/**
	 * The schema to use, if any, when building up the database url connection string.
	 */
	private String schema;

	/**
	 * The built up string to use to connect to the database.
	 */
	private String url;

	/**
	 * Comment for <code>username</code>
	 */
	private String username = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return getConnection(username, password);
	}

	/**
	 * (@inheritDoc}
	 * 
	 * It appears that in the Sun Application Server the server calls this method with
	 * both the username and password being set to the empty string
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		if (username == null || username.length() == 0) {
			if (this.username == null) {
				log("MckoidbDataSource: Warning, connectecting without username being set, using empty string.");
				password = "";
			}
			else {
				username = this.username;
			}
		}

		if (password == null || password.length() == 0) {
			if (this.password == null) {
				log("MckoidbDataSource: Warning, connectecting without password being set, using empty string.");
				password = "";
			}
			else {
				password = this.password;
			}
		}

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(getUrl(), username, password);
			connection.setAutoCommit(false);
		}
		catch (SQLException sqe) {
			StringBuffer message = new StringBuffer();

			message.append("MckoidbDataSource: Unable to make a connection to the database because:\n").append(
					sqe.getMessage());
			message.append("Connection parameters:");
			message.append("\turl: ").append(getUrl());
			message.append("\tusername: ").append((this.username == null ? "not set" : this.username));
			message.append("\tpassword: ").append((this.password == null ? "not set" : "not printed for security"));

			log(message.toString());

			throw sqe;
		}

		return connection;
	}

	/**
	 * @return Returns the hostname.
	 */
	public String getHostname() {
		return hostname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.DataSource#getLoginTimeout()
	 */
	public int getLoginTimeout() throws SQLException {
		return loginTimeout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.DataSource#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		if (password == null) {
			return "";
		}

		return password;
	}

	/**
	 * @return Returns the port.
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @return Returns the schema.
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * Build up a connection string for the Mckoidb. The required syntax
	 * is <code>jdbc:mysql://host[:port][/schema]/</code>.
	 * 
	 * @return The url to connect with.
	 */
	private String getUrl() {
		if (url == null) {
			StringBuffer buf = new StringBuffer("jdbc:mysql://");

			if (hostname == null) {
				log("MckoidbDataSource: Warning, connectecting to default host \"localhost\"");
				buf.append("localhost");
			}
			else {
				buf.append(hostname);
			}

			if (port != null) {
				buf.append(":").append(port);
			}

			buf.append("/");

			if (schema != null) {
				buf.append(schema);
			}

			url = buf.toString();

			log("Set connection url to " + url);
		}

		return url;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		if (username == null) {
			return "";
		}

		return username;
	}

	private void log(String message) {
		if (logWriter != null) {
			logWriter.println(message);
		}
	}

	/**
	 * The schema to use, if any, when building up the database url connection string.
	 * This is optional and if it is not set the connections will use the string
	 * <code>localhost</code> as the hostname.
	 * 
	 * @param username The hostname to use.
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.DataSource#setLoginTimeout(int)
	 */
	public void setLoginTimeout(int timeout) throws SQLException {
		loginTimeout = timeout;
	}

	/**
	 * Set the log writer being used by this data source.
	 *
	 * @param logWriter The new log writer
	 *
	 * @exception SQLException if a database access error occurs
	 */
	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		this.logWriter = logWriter;
	}

	/**
	 * The password to use, if any, when building up the database url connection string.
	 * This is optional and if it is not set the connections will use an empty string
	 * as the password.
	 * 
	 * @param username The username to use.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * The port to use, if any, when building up the database url connection string.
	 * This is optional and if it is not set the connections will use the
	 * default port that Mckoi uses.
	 * 
	 * @param port The port to connect to.
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * The schema to use, if any, when building up the database url connection string.
	 * This is optional and if it is not set the connections will use no schema.
	 * 
	 * @param schema The name of the schema to use.
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * The schema to use, if any, when building up the database url connection string.
	 * This is optional and if it is not set the connections will use an empty string
	 * as the username.
	 * 
	 * @param username The username to use.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return null;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
