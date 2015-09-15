package rs.otvoreniparlament.api.service;

import java.util.List;

import rs.otvoreniparlament.api.domain.Speech;

public interface SpeechService {
	
	public List<Speech> getMemberSpeeches(int id, int limit, int page);
	
	public Speech getSpeech(int id);

	public List<Speech> getSpeeches(int limit, int page);

}
