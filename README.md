[![Codacy Badge](https://api.codacy.com/project/badge/Grade/753cdb0b87e34947b7bb6295ad861e21)](https://www.codacy.com/app/zim182/unified-test-core?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Invictum/unified-test-core&amp;utm_campaign=Badge_Grade)

Invictum Test
====

Enhanced version of Serenity TAF project.

Quick start
----
The easiest way to start with Invictum test framework is to use maven archetype. Just invoke command in terminal:
```
mvn archetype:generate -DarchetypeCatalog=https://oss.sonatype.org/content/groups/public/ -DarchetypeGroupId=com.github.invictum -DarchetypeArtifactId=invictum-junit-archetype -DarchetypeVersion=1.5
```
Project structure will be generated and ready for tests implementation.
For now only jUnit style archetype is prepared, but it is also possible just to add maven dependency manually.

Please refer to [Wiki section](https://github.com/Invictum/invictum-test/wiki) for more details.

Important notes
---------------
Starting from version 1.8 `allure-invictum-integration` module will not be supported anymore and scheduled for removal. Use [serenity-allure-integration](https://github.com/Invictum/serenity-allure-integration) module instead.