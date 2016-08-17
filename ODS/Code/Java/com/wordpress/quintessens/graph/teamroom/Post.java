package com.wordpress.quintessens.graph.teamroom;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;
//add our graph data modelling classes
import com.wordpress.quintessens.graph.teamroom.Profile;

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
	
	@AdjacencyUnique(label = "hasWritten", direction = Direction.OUT)
	public Iterable<Profile> getAuthors();
}
