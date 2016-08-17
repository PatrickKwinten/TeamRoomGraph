package com.wordpress.quintessens.graph.teamroom;

import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("config")
public interface Configuration extends DVertexFrame {
	
	@Property("$$Key")
	public String getKey();

	@Property("ApplicationName")
	public String getApplicationName();
	
	@Property("ApplicationName")
	public void setApplicationName(String n);
}

