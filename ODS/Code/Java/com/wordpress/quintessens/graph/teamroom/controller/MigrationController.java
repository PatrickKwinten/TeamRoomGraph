package com.wordpress.quintessens.graph.teamroom.controller;

import java.io.Serializable;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.View;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.XspOpenLogUtil;
//add our graph data modelling classes
import com.wordpress.quintessens.graph.teamroom.GraphHelper;
import com.wordpress.quintessens.graph.teamroom.Profile;
import com.wordpress.quintessens.graph.teamroom.Post;
import com.wordpress.quintessens.graph.teamroom.Response;

/**
 * @author admin
 *
 */
public class MigrationController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public void reset(){
		GraphHelper.clearGraphData();
	}

	public boolean migrateData() {
		System.out.println("=start========================================");
		System.out.println("**" + this.getClass().getSimpleName() + "** migrateData()");
		// get all user profiles to migrate
		boolean result = false;
		DocumentCollection profiles = Factory.getSession().getCurrentDatabase().search("SELECT Form=\"ParticipantProfile\"");
		for (Document profile : profiles) {
			result = migrateProfile(profile);
		}
		System.out.println("=end========================================");
		return result;
	}

	private boolean migrateProfile(Document doc) {
		System.out.println("*start******************************************");
		System.out.println("**" + this.getClass().getSimpleName() + "** migrateProfile(Document doc)");
		boolean result = false;
		try {
			DFramedTransactionalGraph<DGraph> profilesGraph = GraphHelper.getProfilesGraph();
			
			Profile profile = profilesGraph.addVertex(doc.getItemValueString("enterWho"), Profile.class);
			//Profile profile = profilesGraph.addVertex(doc.getUniversalID(), Profile.class);
			
			profile.setName(doc.getItemValueString("enterWho"));
			profile.setDepartment(doc.getItemValueString("Department"));
			profile.setLocation(doc.getItemValueString("Location"));
			profile.setJob(doc.getItemValueString("JobTitle"));
			profilesGraph.commit();

			migrateTopics(doc, profilesGraph, profile);
			migrateResponses(profilesGraph);
			result = true;
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
		System.out.println("*end******************************************");
		return result;
	}
	
	private void migrateTopics(Document doc, DFramedTransactionalGraph<DGraph> profilesGraph, Profile profile) {
		System.out.println("+start++++++++++++++++++++++++++++++++++++++++");
		try {
			System.out.println("**" + this.getClass().getSimpleName() + "** migrateTopics(Document doc, DFramedTransactionalGraph<DGraph> profilesGraph, Profile profile)");
			Database db = Factory.getSession().getCurrentDatabase();
		    View view = db.getView("postsbyAuthor");
		    String author = doc.getItemValueString("GetAlternateName");
		    DocumentCollection col = view.getAllDocumentsByKey(author, true);
		    if (col.size()>0){
		    	 System.out.println("found: " + col.size() + " of topics written by " + author);
		    }			
			for (Document post : col) {
				org.openntf.domino.ext.Document parent = post.getParentDocument();
	        	Post vertexPost = profilesGraph.addVertex(post.getUniversalID(), Post.class);
	        	vertexPost.setSubject(post.getItemValueString("Subject"));
	        	vertexPost.setAbstract(post.getItemValueString("Abstract"));
	        	profile.addTopic(vertexPost);
			}
			profilesGraph.commit();
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
		System.out.println("+end++++++++++++++++++++++++++++++++++++++++");
	}
	
	private void migrateResponses(DFramedTransactionalGraph<DGraph> profilesGraph) {
		System.out.println("-start--------------------------------------");
		try {
			System.out.println("**" + this.getClass().getSimpleName() + "** migrateResponses(DFramedTransactionalGraph<DGraph> profilesGraph)");
			Database db = Factory.getSession().getCurrentDatabase();
		    View view = db.getView("responsesOnly");
		    DocumentCollection col = view.getAllDocuments();
			for (Document response : col) {
				System.out.println("looking for parent for response with id:" + response.getUniversalID() + " and subject:" + response.getItemValueString("subject"));
	        	org.openntf.domino.ext.Document parent = response.getParentDocument();
	        	if (parent.getFormName().equalsIgnoreCase( "MainTopic")){
	        		System.out.println("parent found");
	        		System.out.println("parent has subject:" +parent.getItemValue("Subject",String.class));;
	        		//this is a response doc
	        		Post post = profilesGraph.getElement(response.getParentDocumentUNID(), Post.class);
	        		Response vertexResponse = profilesGraph.addVertex(response.getUniversalID(), Response.class);
		        	vertexResponse.setSubject(response.getItemValueString("Subject"));
		        	
		        	vertexResponse.setAbstract(response.getItemValueString("Abstract"));
		        	vertexResponse.setParent(response.getParentDocumentUNID());
		        	post.addResponse(vertexResponse);
	        	}
	        	else{
	        		//this is a response to a response doc
	        		Response prevResponse = profilesGraph.getElement(response.getParentDocumentUNID(), Response.class);
	        		Response vertexResponse = profilesGraph.addVertex(response.getUniversalID(), Response.class);
	        		vertexResponse.setSubject(response.getItemValueString("Subject"));
	        		prevResponse.addResponse(vertexResponse);
	        	}
	        	profilesGraph.commit();
			}
			
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
		System.out.println("-end--------------------------------------");
	}
}
