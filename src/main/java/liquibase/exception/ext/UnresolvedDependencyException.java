package liquibase.exception.ext;

import liquibase.exception.LiquibaseException;

/**
 * Thrown when a change set depends on nonexistent change set.
 *
 * @author Mikhail Klishevskyi
 * @since 1.0.0
 */
public class UnresolvedDependencyException extends LiquibaseException {

    public UnresolvedDependencyException(String message) {
        super(message);
    }
}
