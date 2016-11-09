package com.wordpress.quintessens.graph.teamroom.controller;

import java.io.Serializable;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.io.json.JsonJavaArray;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.wordpress.quintessens.graph.teamroom.GraphHelper;
import com.wordpress.quintessens.graph.teamroom.Post;
import com.wordpress.quintessens.graph.teamroom.Profile;

public class ProfilesController  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public JsonJavaArray getProfiles() {
		DFramedTransactionalGraph<DGraph> graph = GraphHelper.getProfilesGraph();
		Iterable<Profile> all = graph.getElements(Profile.class);
		JsonJavaArray json = new JsonJavaArray();
		int count = 0;
		for (Profile profile : all) {
			JsonJavaObject jo = new JsonJavaObject();
			jo.putString("name", profile.getName());
			jo.putString("department", profile.getDepartment());
			jo.putString("location", profile.getLocation());
			jo.putString("job", profile.getJob());
			json.put(count, jo);
			count++;
		}
		return json;
	}
	
	public JsonJavaArray getTopics() {
		String fullname = "Patrick Kwinten/quintessens";
		String id = "4F280561215835D5C1258012006B10F5";
		System.out.println("looking for post for:" + fullname);
		DFramedTransactionalGraph<DGraph> graph = GraphHelper.getProfilesGraph();
		System.out.println("DFramedTransactionalGraph<DGraph> graph = GraphHelper.getProfilesGraph(); OK");
		//Profile profile = graph.getElement(id,Profile.class);
		Profile profile = graph.getElement(fullname,Profile.class);
		System.out.println("Profile found. " + profile.getName());
		
		Iterable<Post> all = profile.getPosts();
		JsonJavaArray json = new JsonJavaArray();
		int count = 0;
		for (Post post : all) {
			JsonJavaObject jo = new JsonJavaObject();
			jo.putString("title", post.getSubject());
			jo.putString("summary", post.getAbstract());
			json.put(count, jo);
			count++;
		}
		return json;
	}
	
	public JsonJavaArray getTopics(String name) {
		System.out.println("*start******************************************");
		System.out.println("**" + this.getClass().getSimpleName() + "** getTopics(String name)");
		String fullname = name;
		//String id = "4F280561215835D5C1258012006B10F5";
		System.out.println("looking for post for:" + fullname);
		DFramedTransactionalGraph<DGraph> graph = GraphHelper.getProfilesGraph();
		System.out.println("DFramedTransactionalGraph<DGraph> graph = GraphHelper.getProfilesGraph(); OK");
		//Profile profile = graph.getElement(id,Profile.class);
		Profile profile = graph.getElement(fullname,Profile.class);
		System.out.println("Profile found. " + profile.getName());
		
		Iterable<Post> all = profile.getPosts();
		
		JsonJavaArray json = new JsonJavaArray();
		int count = 0;
		for (Post post : all) {
			JsonJavaObject jo = new JsonJavaObject();
			jo.putString("id", post.getKey());
			jo.putString("title", post.getSubject());
			jo.putString("summary", post.getAbstract());
			json.put(count, jo);
			count++;
		}
		System.out.println("Number of post found:" + count);
		System.out.println("*end******************************************");
		return json;
	}


/*	public JsonJavaArray getTopics() {
		String fullname = "Patrick Kwinten/quintessens";
		System.out.println("looking for post for:" + fullname);
		DFramedTransactionalGraph<DGraph> graph = GraphHelper.getProfilesGraph();
		Profile profile = graph.getElement(fullname,Profile.class);
		Iterable<Post> all = profile.getPosts();
		JsonJavaArray json = new JsonJavaArray();
		int count = 0;
		for (Post post : all) {
			JsonJavaObject jo = new JsonJavaObject();
			jo.putString("subject", post.getSubject());
			json.put(count, jo);
			count++;
		}
		return json;
	}*/
	
	
	/*public JsonJavaArray getProfiles() {
		//List<String> profiles = new ArrayList<String>();
		JsonJavaObject data = null;
		try {
			DFramedTransactionalGraph<DGraph> profilesGraph = GraphHelper.getProfilesGraph();
			
			Iterable<Profile> all = profilesGraph.getElements(Profile.class);
			int count = 0;
			for (Profile profile : all) {
				JsonJavaObject jo = new JsonJavaObject();
				jo.putString("name", profile.getName());
				jo.putString("department", profile.getDepartment());
				jo.putString("location", profile.getLocation());
				data.put(count, jo);
				count++;
			}
			
		} catch (Throwable t) {
			XspOpenLogUtil.logError(t);
		}
		return data;
	}*/

	
	
	
	
}


