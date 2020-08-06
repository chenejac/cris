package rs.ac.uns.ftn.informatika.bibliography.evaluation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rs.ac.uns.ftn.informatika.bibliography.backup.Task;

/**
 * @author "chenejac@uns.ac.rs"
 *
 */
public class ReevaluateResultsTask implements Task {

	public ReevaluateResultsTask() {
	}

	@Override
	public boolean execute() {
		try {
			ResultEvaluator.reevaluateAll();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			return false;
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e);
			return false;
		}
	}
	
	private static Log log = LogFactory.getLog(ReevaluateResultsTask.class.getName());

}
