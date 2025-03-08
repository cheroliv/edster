#!/bin/bash

# Remplacez ces valeurs par vos informations réelles
DB_USER="edster";
DB_NAME="edster";
DB_HOST="localhost";
SCHEMA_NAME="public";

# Se connecter à la base de données et supprimer les tables
DROP_COMMANDS=$(psql -U "$DB_USER" -d "$DB_NAME" -h "$DB_HOST" -At -c "
SELECT string_agg(format('DROP TABLE %s.%s CASCADE;', schemaname, tablename), '; ')
FROM pg_tables
WHERE schemaname = '$SCHEMA_NAME'
  AND tableowner = '$DB_USER'
");

if [ -n "$DROP_COMMANDS" ]; then
  psql -U "$DB_USER" -d "$DB_NAME" -h "$DB_HOST" -c "$DROP_COMMANDS" 2>&1
  if [ $? -ne 0 ]; then
    echo "Error occurred while dropping tables!"
    exit 1
  fi
else
  echo "No tables found in schema '$SCHEMA_NAME' owned by '$DB_USER'."
fi;

echo "Tables in schema '$SCHEMA_NAME' been dropped.";

rm -R .devcontainer;
rm -R .goose;
rm -R .gradle;
rm -R .husky;
rm -R .jhipster;
rm -R build/reports;
rm -R build/tmp;
rm -R buildSrc;
rm -R gradle;
rm -R src;
rm -R webpack;
rm -R build/classes;
rm -R build/generated;
rm -R build/jacoco;
rm -R build/openapi;
rm -R build/resources;
rm -R build/test-results;
rm -R build/webpack;

rm build/resolvedMainClassName;
rm .editorconfig;
rm .gitattributes;
rm .lintstagedrc.cjs;
rm .prettierignore;
rm .prettierrc;
rm .yo-rc.json;
rm build.gradle;
rm checkstyle.xml;
rm cypress.config.ts;
rm cypress-audits.config.ts;
rm eslint.config.mjs;
rm gradle.properties;
rm gradlew;
rm gradlew.bat;
rm jest.conf.js;
rm pnpmw;
rm pnpmw.cmd;
rm npmw;
rm npmw.cmd;
rm package.json;
rm package-lock.json;
rm pnpm-lock.yaml;
rm postcss.config.js;
rm README.md;
rm settings.gradle;
rm sonar-project.properties;
rm tsconfig.json;
rm tsconfig.test.json;
