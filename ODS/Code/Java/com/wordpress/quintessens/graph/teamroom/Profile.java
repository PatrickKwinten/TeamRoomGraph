package com.wordpress.quintessens.graph.teamroom;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

//import com.wordpress.quintessens.graph.*;
//import org.openntf.graph.Birthday;
//import org.openntf.graph.Project;
//import org.openntf.graph.Skill;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("profile")
public interface Profile extends DVertexFrame {

	@Property("$$Key")
	public String getKey();

	// optional fields
	@Property("Name")
	public String getName();

	@Property("Name")
	public void setName(String n);

	@Property("Department")
	public String getDepartment();

	@Property("Department")
	public void setDepartment(String n);

	@Property("Location")
	public String getLocation();

	@Property("Location")
	public void setLocation(String n);

	@Property("Email")
	public String getMail();

	@Property("Email")
	public void setMail(String n);
	
	// real edges!
	@AdjacencyUnique(label = "hasWritten", direction = Direction.IN)
	public void addTopic(Post post);

	@AdjacencyUnique(label = "hasWritten", direction = Direction.IN)
	public void removeTopic(Post post);

	@AdjacencyUnique(label = "hasWritten", direction = Direction.IN)
	public Iterable<Post> getPosts();

//	@AdjacencyUnique(label = "workedOnProject", direction = Direction.IN)
//	public void addProject(Project project);
//
//	@AdjacencyUnique(label = "workedOnProject", direction = Direction.IN)
//	public void removeProject(Project project);
//
//	@AdjacencyUnique(label = "workedOnProject", direction = Direction.IN)
//	public Iterable<Project> getProjects();
//
//	@AdjacencyUnique(label = "hasBirthday", direction = Direction.IN)
//	public void addBirthday(Birthday birthday);
//
//	@AdjacencyUnique(label = "hasBirthday", direction = Direction.IN)
//	public Birthday getBirthday();

}
