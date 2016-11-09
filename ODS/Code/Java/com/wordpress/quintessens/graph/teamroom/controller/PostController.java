package com.wordpress.quintessens.graph.teamroom.controller;

import java.io.Serializable;

import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DGraph;

import com.ibm.commons.util.io.json.JsonJavaArray;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.wordpress.quintessens.graph.teamroom.GraphHelper;
import com.wordpress.quintessens.graph.teamroom.Post;
import com.wordpress.quintessens.graph.teamroom.Response;

public class PostController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public JsonJavaArray getResponses(String id) {

		System.out.println("=start========================================");
		System.out.println("**" + this.getClass().getSimpleName() + "** getResponses()");
		System.out.println("looking for responses for:" + id);
		DFramedTransactionalGraph<DGraph> graph = GraphHelper.getProfilesGraph();
		System.out.println("DFramedTransactionalGraph<DGraph> graph = GraphHelper.getProfilesGraph(); OK");
		//Profile profile = graph.getElement(id,Profile.class);
		
		
		
		Post post = graph.getElement(id,Post.class);
		
		System.out.println("Post found. " + post.getSubject());
		
		Iterable<Response> all = post.getResponses();
		JsonJavaArray json = new JsonJavaArray();
		int count = 0;
		for (Response response : all) {
			JsonJavaObject jo = new JsonJavaObject();
			jo.putString("title", response.getSubject());
			jo.putString("summary", response.getAbstract());
			json.put(count, jo);
			count++;
		}
		System.out.println("=end========================================");
		return json;
	}
}
