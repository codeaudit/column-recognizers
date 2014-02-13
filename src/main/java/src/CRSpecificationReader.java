import java.io.File;
import java.util.regex.Matcher;

/**
 * 
 * The CRSpecificationReader reads column recognizer specification files.
 * The format is indicated below.
 * 
 
   # This is a column recognizer specification file.
   #
   # It can include comment lines, blank lines, and specification lines.
   #
   # Comment lines start with the hash symbol.
   #
   # Specification lines consist of 
   # 	conceptID	type	model_string
   # according to one of the following patterns:
   #
   # <conceptID> REGEX <regex>
   # <conceptID> VALUE_SET <file_path>
   #
   # Each specification line produces a column recognizer. 
   #

   # Restaurant
   2001 REGEX		((trattoria)|(ristorante)|(ost[ae]ria)) [a-z]+
   
   # Town (comune)
   4001 VALUE_SET	comune.txt
   
 *
 * @author Simon
 *
 */
/**
 * @author Simon
 *
 */
public class CRSpecificationReader extends SyntaxPatternLineReader {
	final static String LINE_SYNTAX = "([0-9]+)\\s+([_A-Za-z0-9]+)\\s+(.*)";
	final static int CONCEPT_ID_POSITION = 1;
	final static int TYPE_POSITION = 2;
	final static int MODEL_POSITION = 3;
	FusionColumnRecognizer fusionCR = null;
	RowTable table = null;
	RowTable sample = null;
	
	/**
	 * Constructs the reader
	 * 
	 * @param file		The specification file
	 * @param fusionCR	The fusion recognizer to which the CRs will be attached
	 * @param table		The data table (or a large sample)
	 * @param sample	A smaller sample of rows from the table
	 */
	public CRSpecificationReader(File file, FusionColumnRecognizer fusionCR,
			RowTable table, RowTable sample) {
		super(file, LINE_SYNTAX);
		this.fusionCR = fusionCR;
		this.table = table;
		this.sample = sample;
	}
		
	/**
	 * Processes the matched line.
	 */
	@Override
	protected void processMatch(Matcher matcher) {
		// TODO We should have some error handling here
		long conceptID = Long.parseLong(matcher.group(CONCEPT_ID_POSITION));
		String type = matcher.group(TYPE_POSITION);
		String model = matcher.group(MODEL_POSITION);
		
		ColumnRecognizer newRecognizer 
			= ColumnRecognizerFactory.makeRecognizer(conceptID, type, model, table, sample);
		fusionCR.add(newRecognizer); 		
	}

}
