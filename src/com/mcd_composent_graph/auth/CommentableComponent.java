package com.mcd_composent_graph.auth;

import java.util.List;

public interface CommentableComponent extends NameableComponent{
	public String getCommentaire();
	public List<ProprieteGraph> getProprietesGraphList();
}
interface NameableComponent{
	public String getName();
}