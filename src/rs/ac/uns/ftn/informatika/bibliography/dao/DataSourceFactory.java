/**
 * 
 */
package rs.ac.uns.ftn.informatika.bibliography.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;

/**
 * @author Dragan Ivanovic
 *
 */
public class DataSourceFactory {

	
	public static DataSource getDataSource()
		{
			if(DataSourceFactory.dataSource!=null)
				return DataSourceFactory.dataSource;
			else{
				try {
					Context ctx = new InitialContext();
					Context envCtx = (Context) ctx.lookup("java:comp/env");
					DataSourceFactory.dataSource = (DataSource) envCtx.lookup(DataSourceFactory
							.dataSourceName);
				} catch (NamingException ex) {
					LogFactory.getLog(DataSourceFactory.class.getName()).error(ex);
				} catch (Exception ex){
					LogFactory.getLog(DataSourceFactory.class.getName()).error(ex);
				}
				
				if(DataSourceFactory.dataSource == null){
					DataSourceFactory.dataSource = new MyDataSource();
				}
				return DataSourceFactory.dataSource;
			}
	}
	
	private static DataSource dataSource;
	
	public static String dataSourceName; 
	
}
