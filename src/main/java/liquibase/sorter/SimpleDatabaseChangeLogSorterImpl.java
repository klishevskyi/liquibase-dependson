package liquibase.sorter;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.exception.ext.CircularDependencyException;
import liquibase.exception.ext.UnresolvedDependencyException;
import liquibase.precondition.Precondition;
import liquibase.precondition.core.PreconditionContainer;
import liquibase.precondition.ext.DependsOnPrecondition;
import liquibase.sorter.utils.ChangeSetUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static liquibase.sorter.utils.ChangeSetUtils.incrementDependentChangeSetCount;

/**
 * Simple sorting implementation, based on count of dependent change sets.
 *
 * @author Mikhail Klishevskyi
 * @since 1.0.0
 */
public class SimpleDatabaseChangeLogSorterImpl implements DatabaseChangeLogSorter {

    @Override
    public void applyDependentsSequence(DatabaseChangeLog databaseChangeLog) throws LiquibaseException {
        List<ChangeSet> changeSets = databaseChangeLog.getChangeSets();

        for (ChangeSet changeSet : changeSets) {
            validateDatabaseChangeSet(databaseChangeLog, changeSet);
        }

        changeSets.sort(Comparator.comparingInt(ChangeSetUtils::getDependentChangeSetCount).reversed());
    }

    private void validateDatabaseChangeSet(DatabaseChangeLog databaseChangeLog, ChangeSet verifiableChangeSet)
            throws UnresolvedDependencyException, CircularDependencyException {

        PreconditionContainer preconditions = verifiableChangeSet.getPreconditions();
        if (Objects.nonNull(preconditions)) {
            for (Precondition precondition : preconditions.getNestedPreconditions()) {
                if (precondition instanceof DependsOnPrecondition) {
                    validateAndSetDependencyIndex(databaseChangeLog, verifiableChangeSet, verifiableChangeSet, (DependsOnPrecondition) precondition);
                }
            }
        }
    }

    private void validateAndSetDependencyIndex(DatabaseChangeLog databaseChangeLog, ChangeSet rootVerifiableChangeSet, ChangeSet currentVerifiableChangeSet, DependsOnPrecondition precondition)
            throws UnresolvedDependencyException, CircularDependencyException {

        ChangeSet targetChangeSet = getTargetChangeSet(databaseChangeLog, precondition);

        if (Objects.isNull(targetChangeSet)) {
            throw new UnresolvedDependencyException("Change set " + currentVerifiableChangeSet + " depends on nonexistent change set " + precondition + ".");
        }

        if (targetChangeSet == rootVerifiableChangeSet) {
            throw new CircularDependencyException("Change set " + currentVerifiableChangeSet + " depends on source change set " + rootVerifiableChangeSet + ".");
        }

        incrementDependentChangeSetCount(targetChangeSet);

        PreconditionContainer preconditions = targetChangeSet.getPreconditions();
        if (Objects.nonNull(preconditions)) {
            for (Precondition nestedPrecondition : preconditions.getNestedPreconditions()) {
                if (nestedPrecondition instanceof DependsOnPrecondition) {
                    validateAndSetDependencyIndex(databaseChangeLog, rootVerifiableChangeSet, targetChangeSet, (DependsOnPrecondition) nestedPrecondition);
                }
            }
        }
    }

    /**
     * Get a {@link ChangeSet} from {@link DatabaseChangeLog} for which the dependency is described in {@link DependsOnPrecondition}.
     *
     * @param databaseChangeLog database change log with enabled extension.
     * @param precondition      precondition containing key of change set.
     * @return {@link ChangeSet} if exists, or else return {@code null}
     */
    private ChangeSet getTargetChangeSet(DatabaseChangeLog databaseChangeLog, DependsOnPrecondition precondition) {
        for (ChangeSet changeSet : databaseChangeLog.getChangeSets()) {
            if (changeSet.getId().equals(precondition.getId())
                    && changeSet.getAuthor().equals(precondition.getAuthor())
                    && changeSet.getFilePath().endsWith(precondition.getChangeLogFile())) {
                return changeSet;
            }
        }

        return null;
    }
}
