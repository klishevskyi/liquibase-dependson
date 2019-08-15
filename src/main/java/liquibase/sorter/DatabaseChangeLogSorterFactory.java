package liquibase.sorter;

/**
 * Factory providing database change log sorter implementation.
 *
 * @author Mikhail Klishevskyi
 * @see DatabaseChangeLogSorter
 * @see SimpleDatabaseChangeLogSorterImpl
 * @since 1.0.0
 */
public class DatabaseChangeLogSorterFactory {

    /**
     * Method return current {@link DatabaseChangeLogSorter} implementation.
     *
     * @return suitable {@link DatabaseChangeLogSorter} implementation
     */
    public static DatabaseChangeLogSorter getDatabaseChangeLogSorter() {
        return new SimpleDatabaseChangeLogSorterImpl();
    }
}
