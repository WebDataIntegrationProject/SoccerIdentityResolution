package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model;


import java.io.Serializable;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;


public class Club implements Matchable {

	/*
	 * <club>
	    <!-- Most common national name. -->
	    <name>FC Bayern München</name>
	    <!-- ISO 3166 alpha 3 country code -->
	    <country>DEU</country>
	    <nameOfStadium>Allianz Arena</nameOfStadium>
	    <!-- English name of city-->
	    <cityOfStadium>Munich</cityOfStadium>
	    <!-- integer, no thousand separator-->
	    <stadiumCapacity>75000</stadiumCapacity>
	    <!-- Name of league (national name) -->
	    <league>Bundesliga</league>
	    <players>...</players>
	   </club>
	 */

	protected String id;
	protected String provenance;
	private String name;
	private String country;
	private String nameOfStadium;
	private String cityOfStadium;
	private Integer stadiumCapacity;
	private String league;
	private List<Player> players;

	public Club(String identifier, String provenance) {
		id = identifier;
		this.provenance = provenance;
		players = new LinkedList<>();
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

	public String getNameOfStadium() {
		return nameOfStadium;
	}

	public void setNameOfStadium(String nameOfStadium) {
		this.nameOfStadium = nameOfStadium;
	}

	public String getCityOfStadium() {
		return cityOfStadium;
	}

	public void setCityOfStadium(String cityOfStadium) {
		this.cityOfStadium = cityOfStadium;
	}

	public Integer getStadiumCapacity() {
		return stadiumCapacity;
	}

	public void setStadiumCapacity(Integer stadiumCapacity) {
		this.stadiumCapacity = stadiumCapacity;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String leagueLabel) {
		this.league = leagueLabel;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	@Override
	public String toString() {
		return String.format("[Club: %s / %s / %s]", getName(),
				getCountry(), getLeague());
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

