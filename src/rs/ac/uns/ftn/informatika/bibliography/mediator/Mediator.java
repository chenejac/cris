package rs.ac.uns.ftn.informatika.bibliography.mediator;

import java.util.List;

import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;

public interface Mediator {
	public List<RecordDTO> getRecords(Object query);

}
