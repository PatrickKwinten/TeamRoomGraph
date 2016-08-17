package com.wordpress.quintessens.graph.teamroom;

import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DElementStore;
import org.openntf.domino.graph2.impl.DFramedGraphFactory;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

public class GraphHelper {

	/**
	 * get the graph for the configuration part
	 * @return
	 */
	public static DFramedTransactionalGraph<DGraph> getConfig() {
		try {
			// set element store for configuration
			DElementStore configStore = new DElementStore();
			configStore.setStoreKey(Factory.getSession(SessionType.CURRENT).getCurrentDatabase().getFilePath());

			// setup the type
			configStore.addType(Configuration.class);

			// create a graph config 
			DConfiguration config = new DConfiguration();
			DGraph graph = new DGraph(config);

			// add the config element store
			config.addElementStore(configStore);
			config.setDefaultElementStore(configStore.getStoreKey());

			// setup the graph
			DFramedGraphFactory factory = new DFramedGraphFactory(config);
			DFramedTransactionalGraph<DGraph> fg = (DFramedTransactionalGraph<DGraph>) factory.create(graph);

			// return the graph
			return fg;
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
			return null;
		}
	}

	/**
	 * get the graph for working data
	 * @return
	 */
	public static DFramedTransactionalGraph<DGraph> getProfilesGraph() {
		try {
			// set element store for configuration
			DElementStore commonStore = new DElementStore();
			String fileName = getGraphDatabaseFilepath();
			commonStore.setStoreKey(fileName);

			// setup the types
//			commonStore.addType(Profile.class);
//			commonStore.addType(Birthday.class);
//			
//			commonStore.addType(Project.class);
//			commonStore.addType(Skill.class);

			// create a graph config 
			DConfiguration config = new DConfiguration();
			DGraph graph = new DGraph(config);

			// add the config element store
			config.addElementStore(commonStore);
			config.setDefaultElementStore(commonStore.getStoreKey());

			// setup the graph
			DFramedGraphFactory factory = new DFramedGraphFactory(config);
			DFramedTransactionalGraph<DGraph> fg = (DFramedTransactionalGraph<DGraph>) factory.create(graph);

			// return the graph
			return fg;
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
			return null;
		}
	}

	/**
	 * delete all graph data
	 */
	public static void clearGraphData() {
		try {
			Database currentDb = Factory.getSession(SessionType.CURRENT).getCurrentDatabase();
			Database graphDb = Factory.getSession(SessionType.CURRENT).getDatabase(currentDb.getServer(), getGraphDatabaseFilepath());
			System.out.println(graphDb.getTitle());
			DocumentCollection col = graphDb.getAllDocuments();
			col.removeAll(true);
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
	}

	/**
	 * compute path to graph
	 * @return
	 */
	private static String getGraphDatabaseFilepath() {
		String fileName = Factory.getSession(SessionType.CURRENT).getCurrentDatabase().getFilePath();
		fileName = fileName.replace(".nsf", "") + "_graph.nsf";
		return fileName;
	}
}
