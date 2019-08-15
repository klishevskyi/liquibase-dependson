package liquibase;

/**
 * Declaration of extension properties.
 *
 * @author Mikhail Klishevskyi
 * @since 1.0.0
 */
public final class DependsOnExtensionProperties {

    public static final String DEPENDS_ON_CHANGELOG_EXTENSION_NAMESPACE = "http://www.liquibase.org/xml/ns/dbchangelog-ext/dependsOn";

    /**
     * Property for enabling depends on extension. <br/>
     * Example: {@code <property name="useDependsOnExtension" value="true"/>}
     *
     * @see liquibase.parser.ext.DependentChangeLogSAXParser
     */
    public static final String USE_DEPENDS_ON_EXTENSION_PROPERTY = "useDependsOnExtension";
}
