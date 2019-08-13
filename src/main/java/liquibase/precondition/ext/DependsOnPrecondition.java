package liquibase.precondition.ext;

import liquibase.DependsOnExtensionProperties;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.database.Database;
import liquibase.exception.ValidationErrors;
import liquibase.exception.Warnings;
import liquibase.precondition.AbstractPrecondition;

import java.util.Objects;

/**
 * Precondition for declaration {@code changeSet} on which the {@code changeSet} depends on.
 *
 * @author Mikhail Klishevskyi
 * @since 1.0.0
 */
public class DependsOnPrecondition extends AbstractPrecondition {

    private String changeLogFile;
    private String id;
    private String author;

    @Override
    public String getName() {
        return "dependsOn";
    }

    public String getChangeLogFile() {
        return changeLogFile;
    }

    public void setChangeLogFile(String changeLogFile) {
        this.changeLogFile = changeLogFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public Warnings warn(Database database) {
        return new Warnings();
    }

    @Override
    public ValidationErrors validate(Database database) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("id", getId());
        validationErrors.checkRequiredField("author", getAuthor());
        return validationErrors;
    }

    @Override
    public String getSerializedObjectNamespace() {
        return DependsOnExtensionProperties.DEPENDS_ON_CHANGELOG_EXTENSION_NAMESPACE;
    }

    @Override
    public void check(Database database, DatabaseChangeLog changeLog, ChangeSet changeSet, ChangeExecListener changeExecListener) {
        /* When changeLogFile property is missing depends on current file change sets */
        if (Objects.isNull(getChangeLogFile())) {
            setChangeLogFile(changeSet.getFilePath());
        }
    }

    @Override
    public String toString() {
        return "DependsOn Precondition: " + getChangeLogFile() + "::" + getId() + "::" + getAuthor();
    }
}
