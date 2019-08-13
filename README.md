Liquibase DependsOn Extension
-----------------------------
The extension provides the ability to declare dependencies between changeSets 
and run them in the order of resolving dependencies.

Installation
------------
Simply download the liquibase-dependson jar and add it to your classpath.

Usage
-----
```xml
<databaseChangeLog
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext/dependsOn"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.7.xsd">

    <changeSet id="user-table-creating" author="mikhail">
        <createTable tableName="user">
            ...
        </createTable>
    </changeSet>

    <changeSet id="user-table-adding-column" author="mikhail">
        <preConditions>
            <ext:dependsOn id="user-table-creating" author="mikhail" />
        </preConditions>

        <addColumn tableName="user">
            ...
        </addColumn>
    </changeSet>
</databaseChangeLog>

```