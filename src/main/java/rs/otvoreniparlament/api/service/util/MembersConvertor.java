package rs.otvoreniparlament.api.service.util;

import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.google.gson.Gson;

import rs.otvoreniparlament.api.domain.Member;
import rs.otvoreniparlament.api.domain.Party;
import rs.otvoreniparlament.api.formatters.DateFormatter;
import rs.otvoreniparlament.api.index.ElasticSearchService;
import rs.otvoreniparlament.indexing.IndexName;
import rs.otvoreniparlament.indexing.IndexType;

public class MembersConvertor {
	
	private static final Logger logger = LogManager.getLogger(MembersConvertor.class);

	public static List<Member> convertToMembers(SearchResponse membersData) {
		List<Member> members = new LinkedList<>();

		for (SearchHit m : membersData.getHits()) {

			Member member = convertToMember(m);
			members.add(member);
		}

		return members;
	}

	public static Member convertToMember(SearchHit memberData) {

		Member member = new Member();

		Map<String, Object> source = memberData.getSource();

		member.setId((int) source.get("id"));
		member.setName((String) source.get("name"));

		member.setLastName((String) source.get("surname"));

		if (source.get("mail") != null) {
			member.setEmail((String) source.get("mail"));
		}
		if (source.get("biography") != null) {
			member.setBiography((String) source.get("biography"));
		}
		if (source.get("birth-town") != null) {
			member.getPlaceOfBirth().setName((String) source.get("birth-town"));
		}
		if (source.get("residence-town") != null) {
			member.getPlaceOfResidence().setName((String) source.get("residence-town"));
		}
		if (source.get("dateofbirth") != null) {
			member.setDateOfBirth((Date) DateFormatter.parseFullTimeDate(source.get("dateofbirth").toString()));
		}
		if (source.get("gender") != null) {
			member.setGender((String) source.get("gender"));
		}

		if (source.get("member-parties") != null) {
			List<Party> parties = new LinkedList<>();
			@SuppressWarnings("unchecked")
			ArrayList<Party> all = (ArrayList<Party>) source.get("member-parties");

			for (int i = 0; i < all.size(); i++) {
				String query = "";

				Gson gson=new Gson();
				String json = gson.toJson(all.get(i));
				try {
					JSONObject jsonObj = new JSONObject(json);
					query = jsonObj.getString("party-id");
				} catch (JSONException e) {
					logger.error(e);
				}
				ElasticSearchService es = new ElasticSearchService();
				SearchResponse search = es.searchSpecificID(IndexName.PARTY_INDEX, IndexType.PARTY_TYPE, "party-id",
						query);

				Party party = PartyConvertor.convertToParty(search.getHits().getAt(0));
				parties.add(party);
			}
			member.setParties(parties);

		}
		return member;
	}
	
	public static List<Member> convertToMembersOfParty(SearchResponse membersData) {
		List<Member> members = new LinkedList<>();

		for (SearchHit m : membersData.getHits()) {

			Member member = convertToMember(m);
			members.add(member);
		}

		return members;
	}

	public static Member convertToMemberOfParty(SearchHit memberData) {

		Member member = new Member();

		Map<String, Object> source = memberData.getSource();

		member.setId((int) source.get("id"));
		member.setName((String) source.get("name"));

		member.setLastName((String) source.get("surname"));

		if (source.get("mail") != null) {
			member.setEmail((String) source.get("mail"));
		}
		if (source.get("biography") != null) {
			member.setBiography((String) source.get("biography"));
		}
		if (source.get("birth-town") != null) {
			member.getPlaceOfBirth().setName((String) source.get("birth-town"));
		}
		if (source.get("residence-town") != null) {
			member.getPlaceOfResidence().setName((String) source.get("residence-town"));
		}
		if (source.get("dateofbirth") != null) {
			member.setDateOfBirth((Date) DateFormatter.parseFullTimeDate(source.get("dateofbirth").toString()));
		}
		if (source.get("gender") != null) {
			member.setGender((String) source.get("gender"));
		}

		return member;
	}
}
