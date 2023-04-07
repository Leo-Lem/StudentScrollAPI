# StudentScroll API

This is the REST API behind the StudentScroll app ([OpenAPI specs](./openapi.yml)).

[![CI](https://github.com/Leo-Lem/StudentScrollAPI/actions/workflows/maven.yml/badge.svg)](https://github.com/Leo-Lem/StudentScrollAPI/actions/workflows/maven.yml)

## Setting up Maven

_Necessary for compiling, running, and everything in between_

1. (only macOS) Install using homebrew (`brew install maven`)
2. Now the maven commands should work (e.g., `mvn compile`, `mvn package`, [Docs](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html))

## Setting up a local PostgreSQL database

_Necessary for trying everything out_

1. Install [PostgreSQL](https://www.postgresql.org/download/).
2. Create a database named '**studentscroll**' (macOS: `createdb studentscroll`).
3. Add environment variables for (e.g., in ~/.zshrc)

- PSQL_URL: The url (including port) your psql is running on.
- PSQL_USERNAME: The username to access the database.
- PSQL_PASSWORD: The password to access the database.
