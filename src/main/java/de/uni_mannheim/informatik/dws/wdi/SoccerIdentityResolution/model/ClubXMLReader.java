package de.uni_mannheim.informatik.dws.wdi.SoccerIdentityResolution.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;


public class ClubXMLReader extends XMLMatchableReader<Club, Attribute>  {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Club, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
	}
	
	@Override
	public Club createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Club club = new Club(id, provenanceInfo);

		// fill the attributes
		club.setName(getValueFromChildElement(node, "name"));
		club.setCountry(getValueFromChildElement(node, "country"));
		club.setNameOfStadium(getValueFromChildElement(node, "nameOfStadium"));
		club.setCityOfStadium(getValueFromChildElement(node, "cityOfStadium"));
		club.setLeague(getValueFromChildElement(node, "league"));
		
		// convert string to integer
		try {
			Integer stadiumCapacity = Integer.parseInt(getValueFromChildElement(node, "stadiumCapacity"));
			club.setStadiumCapacity(stadiumCapacity);
		} catch (Exception e) {
			club.setStadiumCapacity(null);
		}

		// load the list of players
		List<Player> players = getObjectListFromChildElement(node, "players",
				"player", new PlayerXMLReader(), provenanceInfo);
		club.setPlayers(players);

		return club;
	}

}
