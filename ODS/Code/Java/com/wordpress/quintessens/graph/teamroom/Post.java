package com.wordpress.quintessens.graph.teamroom;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("post")
public interface Post extends DVertexFrame {

	@Property("$$Key")
	public String getKey();

	@Property("subject")
	public String getSubject();

	@Property("subject")
	public void setSubject(String n);
	
	@Property("abstract")
	public String getAbstract();

	@Property("abstract")
	public void setAbstract(String n);
	
	// real edges!
	
	@AdjacencyUnique(label = "hasWritten", direction = Direction.OUT)
	public Iterable<Profile> getAuthors();
	
	@AdjacencyUnique(label = "hasReaction", direction = Direction.IN)
	public void addResponse(Response response);

	@AdjacencyUnique(label = "hasReaction", direction = Direction.IN)
	public void removeResponse(Response response);

	@AdjacencyUnique(label = "hasReaction", direction = Direction.IN)
	public Iterable<Response> getResponses();
}
