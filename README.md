![CircleCI](https://circleci.com/gh/klishevskyi/liquibase-dependson.svg?style=svg&circle-token=07929ff755e3cae447b8703839ee5f7832bb6a60)

Liquibase DependsOn Extension
-----------------------------
The extension provides the ability to declare dependencies between changeSets 
and run them in the order of resolving dependencies.

Installation
------------
Simply download the liquibase-dependson jar and add it to your classpath.

Usage
-------
The extension allows you to organize migration files relative to the database structure, which can simplifies the long-term support of migrations.

#### Common way to organize your changelogs is by major release 
```
db
├── changelog
│   ├── db.changelog-master.xml
│   ├── db.changelog-1.0.xml
│   ├── db.changelog-1.1.xml
│   └── db.changelog-2.0.xml
```

#### Way to organize your changes regarding the database structure
```
db
├── db.changelog-master.xml
├── tables
│   ├── dbo.basket.xml
│   ├── dbo.discount.xml
│   ├── dbo.product.xml
│   ├── dbo.user.xml
│   └── dbo.wallet.xml
├── triggers
```

#### Enable extension for `db.changelog-master.xml`:
_Ordering will be applied to child changeSets._

```xml
<databaseChangeLog
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.7.xsd">

    <!-- Enable depends on extension -->
    <property name="useDependsOnExtension" value="true"/>

    <includeAll path="./tables"/>
</databaseChangeLog>
```

#### 2: Define changeSets dependencies:
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
            <ext:dependsOn id="product-table-creating" author="mikhail" changeLogFile="dbo.product.xml"/>
        </preConditions>

        <addColumn tableName="user">
            ...
        </addColumn>
    </changeSet>
</databaseChangeLog>

```