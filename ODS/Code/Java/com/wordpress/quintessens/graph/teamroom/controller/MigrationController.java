package com.wordpress.quintessens.graph.teamroom.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
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
		// get all user profiles to migrate
		boolean result = false;
		DocumentCollection profiles = Factory.getSession().getCurrentDatabase().search("SELECT Form=\"ParticipantProfile\"");
		for (Document profile : profiles) {
			result = migratePerson(profile);
		}
		return result;
	}

	private boolean migratePerson(Document doc) {
		boolean result = false;
		try {
			DFramedTransactionalGraph<DGraph> profilesGraph = GraphHelper.getProfilesGraph();
			Profile profile = profilesGraph.addVertex(doc.getUniversalID(), Profile.class);
			profile.setName(doc.getItemValueString("enterWho"));
			profile.setDepartment(doc.getItemValueString("Department"));
			profile.setLocation(doc.getItemValueString("Location"));
			profilesGraph.commit();

			migrateTopics(doc, profilesGraph, profile);
			//migrateSkills(doc, profilesGraph, profile);
			//migrateProjects(doc, profilesGraph, profile);
			//migrateBirthday(doc, profilesGraph, profile);
			result = true;
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
		return result;
	}
	
	private void migrateTopics(Document doc, DFramedTransactionalGraph<DGraph> profilesGraph, Profile profile) {
		try {
			Database db = Factory.getSession().getCurrentDatabase();
		    View view = db.getView("postsbyAuthor");
		    System.out.println("id:" + doc.getUniversalID());
		    System.out.println("form:" + doc.getFormName());
		    String author = doc.getItemValueString("GetAlternateName");
		    System.out.println("author:" + author);
		    DocumentCollection col = view.getAllDocumentsByKey(author, true);
			System.out.println("number of docs found for " + author + " : " + col.getCount());
			
			for (Document post : col) {
				
				System.out.println("form:" + post.getFormName());
	        	System.out.println("id:" + post.getUniversalID());
	        	Post vertexPost = profilesGraph.addVertex(post.getUniversalID(), Post.class);
	        	vertexPost.setSubject(post.getItemValueString("Subject"));
	        	profile.addTopic(vertexPost);
	        	//profilesGraph.commit();
			}
			
	       /* doc = (Document) dc.getFirstDocument();
	        while (dc!=null) {
	        	System.out.println("form:" + doc.getFormName());
	        	System.out.println("id:" + doc.getUniversalID());
	        	//Post vertexPost = profilesGraph.addVertex(doc.getUniversalID(), Post.class);
	        	//vertexPost.setSubject(doc.getItemValueString("Subject"));
	        	//profile.addTopic(vertexPost);

	        }*/
/*			Vector<String> authors = doc.getAuthors();
			for (String author : authors)
			{
				System.out.println("author:" + author);
				 DocumentCollection dc = view.getAllDocumentsByKey(author, true);
				 System.out.println("number of docs found for " + author + " : " + dc.getCount());
			        doc = (Document) dc.getFirstDocument();
			        while (dc!=null) {
			        	Post vertexTopic = profilesGraph.addVertex(doc.getUniversalID(), Post.class);
			        	vertexTopic.setSubject(author);
			        	profile.addTopic(vertexTopic);
			        }
			}*/
			
			profilesGraph.commit();
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
	}

/*	private void migrateSkills(Document doc, DFramedTransactionalGraph<DGraph> profilesGraph, Profile profile) {
		try {
			Vector<Object> skills = doc.getItemValue("userSkills");
			Iterator<Object> iterator = skills.iterator();
			while (iterator.hasNext()) {
				String skill = (String) iterator.next();
				Skill vertexSkill = profilesGraph.addVertex(skill, Skill.class);
				vertexSkill.setSkill(skill);
				profile.addSkill(vertexSkill);
			}
			profilesGraph.commit();
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
	}*/

/*	private void migrateProjects(Document doc, DFramedTransactionalGraph<DGraph> profilesGraph, Profile profile) {
		try {
			Vector<Object> skills = doc.getItemValue("userCurrent");
			Iterator<Object> iterator = skills.iterator();
			while (iterator.hasNext()) {
				String project = (String) iterator.next();
				Project vertexProject = profilesGraph.addVertex(project, Project.class);
				vertexProject.setName(project);
				person.addProject(vertexProject);
			}
			profilesGraph.commit();
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
	}*/

/*	private void migrateBirthday(Document doc, DFramedTransactionalGraph<DGraph> profilesGraph, Profile profile) {
		try {
			if (!doc.getItemValue("userBirthday").isEmpty()) {
				DateTime dt = doc.getItemValue("userBirthday", DateTime.class);
				SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
				Date birthdayDate = dt.toJavaDate();
				String birthdayDateString = f.format(birthdayDate);
				Birthday birthday = profilesGraph.addVertex(birthdayDateString, Birthday.class);
				birthday.setBirthday(birthdayDateString);
				profile.addBirthday(birthday);
				profilesGraph.commit();
			}
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
	}*/
}
