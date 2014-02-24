package eu.trentorise.opendata.columnrecognizers;

/**
 * @author Simon
 *
 */
public interface Table {
	/**
	 * Returns the number of columns in the table.
	 * 
	 * @return The number of columns
	 */
	public int getColumnCount();

	/**
	 * Returns the number of rows in the table.
	 * 
	 * @return The number of rows
	 */
	public int getRowCount();

	/**
	 * Extracts a subset of rows for analysis.
	 * 
	 * @return A subset of the table rows
	 */
	public RowTable extractRowSample();

	/**
	 * Extracts the column indexed by the column number.
	 * 
	 * @param columnNumber	The one-based index of the column to extract
	 * @return				The column
	 */
	public Column extractColumn(int columnNumber);

}