package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model;


import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

/**
 * A {@link AbstractRecord} representing a movie.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class Club implements Matchable {

	/*
	 * example entry <movie> <id>academy_awards_2</id> <title>True Grit</title>
	 * <director> <name>Joel Coen and Ethan Coen</name> </director> <actors>
	 * <actor> <name>Jeff Bridges</name> </actor> <actor> <name>Hailee
	 * Steinfeld</name> </actor> </actors> <date>2010-01-01</date> </movie>
	 */

	protected String id;
	protected String provenance;
	private String name;
	private String country;
	private String leagueLabel;
	// private List<Actor> actors;

	public Club(String identifier, String provenance) {
		id = identifier;
		this.provenance = provenance;
		// actors = new LinkedList<>();
	}

	@Override
	public String getIdentifier() {
		return id;
	}

	@Override
	public String getProvenance() {
		return provenance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLeagueLabel() {
		return leagueLabel;
	}

	public void setLeagueLabel(String leagueLabel) {
		this.leagueLabel = leagueLabel;
	}

//	public List<Actor> getActors() {
//		return actors;
//	}
//
//	public void setActors(List<Actor> actors) {
//		this.actors = actors;
//	}

	@Override
	public String toString() {
		return String.format("[Movie: %s / %s / %s]", getName(),
				getCountry(), getLeagueLabel());
	}

	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Club){
			return this.getIdentifier().equals(((Club) obj).getIdentifier());
		}else
			return false;
	}
	
	
	
}

