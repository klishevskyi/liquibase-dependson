package liquibase.exception.ext;

import liquibase.exception.LiquibaseException;

/**
 * Thrown when cyclic dependencies in change sets are detected.
 *
 * @author Mikhail Klishevskyi
 * @since 1.0.0
 */
public class CircularDependencyException extends LiquibaseException {

    public CircularDependencyException(String message) {
        super(message);
    }
}
