package rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.eval;

import rs.ac.uns.ftn.informatika.bibliography.dto.RecordDTO;
import rs.ac.uns.ftn.informatika.bibliography.dto.recordconverters.ANormativeRecordConverter;
import rs.ac.uns.ftn.informatika.bibliography.marc21.cerifentities.Record;
import rs.ac.uns.ftn.informatika.bibliography.marc21.records.MARC21Record;

/**
 * @author Sinisa Nikolic, sinisa_nikolic@uns.ac.rs
 *
 */
@SuppressWarnings("serial")
public class CommissionConverter extends ANormativeRecordConverter {

	@Override
	public boolean getDTO(Record rec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setDataFields(MARC21Record rec, RecordDTO dto) {
		// TODO Auto-generated method stub

	}

}
