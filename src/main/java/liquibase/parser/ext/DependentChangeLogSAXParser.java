package liquibase.parser.ext;

import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.exception.LiquibaseException;
import liquibase.parser.core.xml.XMLChangeLogSAXParser;
import liquibase.resource.ResourceAccessor;
import liquibase.sorter.DatabaseChangeLogSorterFactory;
import liquibase.util.BooleanUtils;
import liquibase.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

import static liquibase.DependsOnExtensionProperties.USE_DEPENDS_ON_EXTENSION_PROPERTY;

/**
 * Extended {@code XMLChangeLogSAXParser} with {@code dependsOn} feature implementation.
 *
 * @author Mikhail Klishevskyi
 * @see liquibase.sorter.DatabaseChangeLogSorterFactory
 * @see liquibase.parser.ChangeLogParser
 * @since 1.0.0
 */
public class DependentChangeLogSAXParser extends XMLChangeLogSAXParser {

    @Override
    public int getPriority() {
        return super.getPriority() + 1;
    }

    @Override
    public boolean supports(String changeLogFile, ResourceAccessor resourceAccessor) {
        if (super.supports(changeLogFile, resourceAccessor)) {
            try (InputStream is = StreamUtil.singleInputStream(changeLogFile, resourceAccessor)) {
                String content = StreamUtil.getStreamContents(is);
                if (content.contains(USE_DEPENDS_ON_EXTENSION_PROPERTY)) {
                    return true;
                }
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }

    @Override
    public DatabaseChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        DatabaseChangeLog databaseChangeLog = super.parse(physicalChangeLogLocation, changeLogParameters, resourceAccessor);

        /* TODO use more elegant solution */
        String enableDependsOnExtension = String.valueOf(changeLogParameters.getValue(USE_DEPENDS_ON_EXTENSION_PROPERTY, databaseChangeLog));
        if (BooleanUtils.isTrue(Boolean.valueOf(enableDependsOnExtension))) {
            try {
                DatabaseChangeLogSorterFactory
                        .getDatabaseChangeLogSorter()
                        .applyDependentsSequence(databaseChangeLog);
            } catch (LiquibaseException e) {
                throw new ChangeLogParseException(e);
            }
        }

        return databaseChangeLog;
    }
}
