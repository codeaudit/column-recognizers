package eu.trentorise.opendata.columnrecognizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The OneBestFusionCR culls the candidate list, leaving only one candidate per
 * column.
 * 
 * @author Simon
 *
 */
public class OneBestFusionCR extends ColumnRecognizer {

	/**
	 * Constructs the OneBestFusionCR.
	 * 	
	 * @param id	A unique name for the recognizer instance
	 */
	public OneBestFusionCR(String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see eu.trentorise.opendata.columnrecognizers.ColumnRecognizer#computeScoredCandidates(java.util.List)
	 */
	@Override
	public void computeScoredCandidates(List<ColumnConceptCandidate> candidates) {
		Map<Integer, List<ColumnConceptCandidate>> columnMap
			= buildColumnMap(candidates);
		rebuildCandidateList(columnMap, candidates);
	}

	/**
	 * Creates a map of column numbers to lists of candidates corresponding to 
	 * each column. The input candidate list is emptied in the process.
	 * 
	 * @param candidates	The candidate list
	 * @return				The column number - candidate list map
	 */
	private Map<Integer, List<ColumnConceptCandidate>> buildColumnMap(
			List<ColumnConceptCandidate> candidates) {
		Map<Integer, List<ColumnConceptCandidate>> columnMap
			= new HashMap<Integer, List<ColumnConceptCandidate>>();
		Iterator<ColumnConceptCandidate> it = candidates.iterator();
		while (it.hasNext()) {
			ColumnConceptCandidate candidate = it.next();
			int columnNumber = candidate.getColumnNumber();
			if (columnMap.containsKey(columnNumber)) {
				columnMap.get(columnNumber).add(candidate);
			} else {
				List<ColumnConceptCandidate> columnCandidateList 
					= new ArrayList<ColumnConceptCandidate>();
				columnCandidateList.add(candidate);
				columnMap.put(columnNumber, columnCandidateList);
			}
			it.remove();
		}
		return columnMap;
	}

	/**
	 * Rebuilds the candidate list by adding to it the highest-scoring 
	 * candidate for each column.
	 * 
	 * @param columnMap		The map of columns and candidates
	 * @param candidates	The candidate list
	 */
	private void rebuildCandidateList(Map<Integer, List<ColumnConceptCandidate>> columnMap,
			List<ColumnConceptCandidate> candidates) {
		Collection<List<ColumnConceptCandidate>> candidateLists = columnMap.values();
		Iterator<List<ColumnConceptCandidate>> it = candidateLists.iterator();
		while (it.hasNext()) {
			List<ColumnConceptCandidate> candidateList = it.next();
			candidates.add(getMaxCandidate(candidateList));
		}
	}

	/**
	 * Find the highest-scoring candidate in a list of candidates.
	 * 
	 * @param candidateList	The list of candidates
	 * @return				The highest-scoring candidate
	 */
	private ColumnConceptCandidate getMaxCandidate(List<ColumnConceptCandidate> candidateList) {
		ColumnConceptCandidate maxCandidate = null;
		Iterator<ColumnConceptCandidate> it = candidateList.iterator();
		while (it.hasNext()) {
			ColumnConceptCandidate candidate = it.next();
//			if (maxCandidate == null || candidate.getScore() > maxCandidate.getScore()) {
//				maxCandidate = candidate;
//			}
			if (maxCandidate == null) {
				maxCandidate = candidate;
			} else {
				maxCandidate = pickMaxCandidate(maxCandidate, candidate);
			}
		}
		return maxCandidate;
	}

	/**
	 * Given two column concept candidates, pick the one with the higher score
	 * and in case of equal scores, break the tie by preferring the one with 
	 * the smaller concept ID.
	 * 
	 * @param candidate1	A column concept candidate
	 * @param candidate2	Another candidate
	 * @return				The candidate with the higher score
	 */
	public static ColumnConceptCandidate pickMaxCandidate(
			ColumnConceptCandidate candidate1, 
			ColumnConceptCandidate candidate2) {
		double score1 = candidate1.getScore();
		double score2 = candidate2.getScore();
		
		ColumnConceptCandidate maxCandidate = candidate1;
		
		if (score1 > score2) {
			maxCandidate = candidate1;
		} else if (score1 < score2) {
			maxCandidate = candidate2;
		} else if (candidate1.getConceptID() < candidate2.getConceptID()) {
			maxCandidate = candidate1;
		} else {
			maxCandidate = candidate2;
		}
		
		return maxCandidate;
	}

}
