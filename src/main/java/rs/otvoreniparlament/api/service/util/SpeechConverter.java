package rs.otvoreniparlament.api.service.util;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import rs.otvoreniparlament.api.domain.Member;
import rs.otvoreniparlament.api.domain.Speech;
import rs.otvoreniparlament.api.formatters.DateFormatter;

public class SpeechConverter {

	public static List<Speech> convertToSpeeches(SearchResponse speechData) {
		List<Speech> speeches = new LinkedList<>();
		
		for (SearchHit s : speechData.getHits()) {
			Map<String, Object> source = s.getSource();
			
			Speech speech = new Speech();
			speech.setId((int) source.get("speechid"));
			speech.setText((String)source.get("text"));
			speech.setSessionDate((Date) DateFormatter.parseFullTimeDate(source.get("sessiondate").toString()));
			speech.setMember((Member)source.get("speech-member"));
			
			speeches.add(speech);
		}
		return speeches;
	}
}