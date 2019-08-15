package liquibase.sorter.utils;

import liquibase.changelog.ChangeSet;

import static java.util.Optional.ofNullable;

/**
 * @author Mikhail Klishevskyi
 * @since 1.0.0
 */
public class ChangeSetUtils {

    private static final String DEPENDENT_CHANGE_SET_COUNT_ATTRIBUTE = "ext:dependentChangeSetCount";

    /**
     * Increase the number of change sets that depend on the change set.
     *
     * @param changeSet target change set.
     */
    public static void incrementDependentChangeSetCount(ChangeSet changeSet) {
        int count = getDependentChangeSetCount(changeSet);
        changeSet.setAttribute(DEPENDENT_CHANGE_SET_COUNT_ATTRIBUTE, ++count);
    }

    /**
     * Return number of change sets that depend on the change set.
     *
     * @param changeSet target change set.
     * @return number of change sets
     */
    public static int getDependentChangeSetCount(ChangeSet changeSet) {
        return ofNullable(changeSet.getAttribute(DEPENDENT_CHANGE_SET_COUNT_ATTRIBUTE))
                .map(i -> (Integer) i)
                .orElse(0);
    }
}
