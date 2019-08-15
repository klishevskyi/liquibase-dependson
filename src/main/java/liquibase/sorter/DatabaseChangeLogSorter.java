package liquibase.sorter;

import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.precondition.ext.DependsOnPrecondition;

/**
 * @author Mikhail Klishevskyi
 * @see DatabaseChangeLogSorterFactory
 * @since 1.0.0
 */
public interface DatabaseChangeLogSorter {

    /**
     * Validates and sort change sets by {@link DependsOnPrecondition} declarations.
     *
     * @param databaseChangeLog database change log with enabled extension.
     * @throws LiquibaseException when {@link DatabaseChangeLog} contain nonexistent or cyclic dependencies.
     */
    void applyDependentsSequence(DatabaseChangeLog databaseChangeLog) throws LiquibaseException;
}
